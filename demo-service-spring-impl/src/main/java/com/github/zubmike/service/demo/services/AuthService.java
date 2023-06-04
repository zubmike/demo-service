package com.github.zubmike.service.demo.services;

import com.github.zubmike.core.utils.IOUtils;
import com.github.zubmike.service.demo.ServiceResource;
import com.github.zubmike.service.demo.api.types.AuthEntry;
import com.github.zubmike.service.demo.api.types.AuthUserInfo;
import com.github.zubmike.service.demo.dao.UserRepository;
import com.github.zubmike.service.demo.managers.ServiceTokenManager;
import com.github.zubmike.service.demo.types.AccessToken;
import com.github.zubmike.service.demo.types.ServiceUserContext;
import com.github.zubmike.service.demo.types.User;
import com.github.zubmike.service.demo.utils.AuthException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Service
public class AuthService {

	private static final String JWT_HEADER_PREFIX = "Bearer";
	private static final String BASIC_HEADER_PREFIX = "Basic";

	private final UserRepository userRepository;
	private final ServiceTokenManager tokenManager;

	@Autowired
	public AuthService(UserRepository userRepository, ServiceTokenManager tokenManager) {
		this.userRepository = userRepository;
		this.tokenManager = tokenManager;
	}

	public Optional<ServiceUserContext> getServiceUserContext(Locale locale, String authHeader) {
		if (!Strings.isEmpty(authHeader)) {
			var headerParts = authHeader.split(" ");
			if (headerParts.length == 2) {
				var method = headerParts[0];
				var credentials = headerParts[1];
				if (JWT_HEADER_PREFIX.equalsIgnoreCase(method)) {
					return Optional.of(authorizeByJwt(locale, credentials));
				} else if (BASIC_HEADER_PREFIX.equalsIgnoreCase(method)) {
					return Optional.of(authorizeByBasic(locale, credentials));
				}
			}
		}
		return Optional.empty();
	}

	private ServiceUserContext authorizeByJwt(Locale locale, String token) {
		var accessToken = tokenManager.getAccess(token);
		return new ServiceUserContext(accessToken.getUserId(), locale);
	}

	private ServiceUserContext authorizeByBasic(Locale locale, String credentials) {
		var decodedCredentials = IOUtils.decodeBase64ToString(credentials);
		var pair = decodedCredentials.split(":");
		if (pair.length != 2) {
			throw new AuthException(ServiceResource.getString(locale, "res.string.invalidCredentials"));
		}
		var login = pair[0];
		var password = pair[1];
		var user = userRepository.findByLogin(login)
				.orElseThrow(() -> new AuthException(ServiceResource.getString(locale, "res.string.invalidCredentials")));
		checkPassword(locale, user, password);
		return new ServiceUserContext(user.getId(), locale);
	}

	private void checkPassword(Locale locale, User user, String password) {
		if (!Objects.equals(user.getPassword(), IOUtils.hashSha256(password))) {
			throw new AuthException(ServiceResource.getString(locale, "res.string.invalidCredentials"));
		}
	}

	public AuthUserInfo authenticate(Locale locale, AuthEntry authEntry) {
		var user = userRepository.findByLogin(authEntry.getLogin())
				.orElseThrow(() -> new AuthException(ServiceResource.getString(locale, "res.string.invalidCredentials")));
		checkPassword(locale, user, authEntry.getPassword());
		var accessToken = tokenManager.createAccessToken(new AccessToken(user.getId()));
		return new AuthUserInfo(
				user.getId(),
				user.getName(),
				accessToken);
	}

}
