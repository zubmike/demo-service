package com.github.zubmike.service.demo.logic;

import com.github.zubmike.core.utils.IOUtils;
import com.github.zubmike.service.demo.ServiceResource;
import com.github.zubmike.service.demo.api.types.AuthEntry;
import com.github.zubmike.service.demo.api.types.AuthUserInfo;
import com.github.zubmike.service.demo.dao.UserDao;
import com.github.zubmike.service.demo.manager.ServiceTokenManager;
import com.github.zubmike.service.demo.types.AccessToken;
import com.github.zubmike.service.demo.types.ServiceUserContext;
import com.github.zubmike.service.demo.types.User;
import com.github.zubmike.service.utils.AuthException;
import com.google.common.base.Strings;

import javax.inject.Inject;
import java.util.Locale;
import java.util.Objects;

public class AuthLogic {

	private static final String JWT_HEADER_PREFIX = "Bearer";
	private static final String BASIC_HEADER_PREFIX = "Basic";

	private final UserDao userDao;

	private final ServiceTokenManager tokenManager;

	@Inject
	public AuthLogic(UserDao userDao, ServiceTokenManager tokenManager) {
		this.userDao = userDao;
		this.tokenManager = tokenManager;
	}

	public ServiceUserContext authorize(Locale locale, String auth) throws AuthException {
		if (!Strings.isNullOrEmpty(auth)) {
			var headerParts = auth.split(" ");
			if (headerParts.length == 2) {
				var method = headerParts[0];
				var credentials = headerParts[1];
				if (JWT_HEADER_PREFIX.equalsIgnoreCase(method)) {
					return authorizeByJwt(locale, credentials);
				} else if (BASIC_HEADER_PREFIX.equalsIgnoreCase(method)) {
					return authorizeByBasic(locale, credentials);
				}
			}
		}
		throw new AuthException(ServiceResource.getString(locale, "res.string.unableToAuthorize"));
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
		var user = userDao.findByLogin(login)
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
		var user = userDao.findByLogin(authEntry.getLogin())
				.orElseThrow(() -> new AuthException(ServiceResource.getString(locale, "res.string.invalidCredentials")));
		checkPassword(locale, user, authEntry.getPassword());
		var accessToken = tokenManager.createAccessToken(new AccessToken(user.getId()));
		return new AuthUserInfo(
				user.getId(),
				user.getName(),
				accessToken);
	}

}
