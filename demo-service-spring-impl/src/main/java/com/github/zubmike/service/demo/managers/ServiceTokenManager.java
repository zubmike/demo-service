package com.github.zubmike.service.demo.managers;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.zubmike.core.utils.IOUtils;
import com.github.zubmike.core.utils.InternalException;
import com.github.zubmike.service.demo.conf.JwtTokenProperties;
import com.github.zubmike.service.demo.types.AccessToken;
import com.github.zubmike.service.demo.utils.AuthException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@Component
public class ServiceTokenManager {

	private static final String KEY_ALIAS = "jwt-sign-key";
	private static final String HMAC_SHA_256_ALGORITHM = "HmacSHA256";
	private static final int KEY_SIZE = 256;

	private static final String CLAIM_SUB_KEY = "sub";
	private static final String CLAIM_EXP_KEY = "exp";


	private final ObjectReader jsonReader;
	private final ObjectWriter jsonWriter;

	private final FileKeyStoreManager fileKeyStoreManager;
	private final JwtTokenProperties jwtTokenProperties;

	@Autowired
	public ServiceTokenManager(FileKeyStoreManager fileKeyStoreManager, JwtTokenProperties jwtTokenProperties) {
		this.jwtTokenProperties = jwtTokenProperties;
		this.fileKeyStoreManager = fileKeyStoreManager;
		this.jsonReader = new ObjectMapper().readerFor(AccessToken.class);
		this.jsonWriter = new ObjectMapper().writerFor(AccessToken.class);
	}

	public String createAccessToken(AccessToken accessToken) {
		long exp = LocalDateTime.now().atZone(ZoneId.systemDefault())
				.plus(jwtTokenProperties.getAccessTokenLiveTime(), jwtTokenProperties.getTokenLiveTimeUnit())
				.toInstant().toEpochMilli();
		return createToken(accessToken, exp);
	}


	public String createRefreshToken(AccessToken accessToken) {
		long exp = LocalDateTime.now().atZone(ZoneId.systemDefault())
				.plus(jwtTokenProperties.getRefreshTokenLiveTime(), jwtTokenProperties.getTokenLiveTimeUnit())
				.toInstant().toEpochMilli();
		return createToken(accessToken, exp);
	}

	protected String createToken(AccessToken accessToken, Long exp) {
		try {
			JWSObject jwsObject = new JWSObject(
					new JWSHeader(JWSAlgorithm.HS256),
					new Payload(createClaims(accessToken, exp)));
			signToken(jwsObject);
			return jwsObject.serialize();
		} catch (JOSEException | JsonProcessingException e) {
			throw new InternalException(e);
		}
	}

	private void signToken(JWSObject jwsObject) throws JOSEException {
		jwsObject.sign(new MACSigner(getSignKey()));
	}

	private Map<String, Object> createClaims(AccessToken userAccess, Long exp) throws JsonProcessingException {
		var claimsMap = new HashMap<String, Object>();
		if (exp != null) {
			claimsMap.put(CLAIM_EXP_KEY, exp);
		}
		byte[] bytes = jsonWriter.writeValueAsBytes(userAccess);
		String sub = IOUtils.encodeBase64(bytes);
		claimsMap.put(CLAIM_SUB_KEY, sub);
		return claimsMap;
	}

	public AccessToken getAccess(String token) {
		try {
			var claimsMap = getClaimsAndVerifySign(JWSObject.parse(token));
			Long exp = getExp(claimsMap);
			if (exp != null && exp <= System.currentTimeMillis()) {
				throw new AuthException("Invalid JWT token");
			}
			String sub = getSub(claimsMap);
			if (sub == null) {
				throw new AuthException("Invalid JWT token");
			}
			return parseUserAccess(sub);
		} catch (ParseException | JsonParseException | JsonMappingException e) {
			throw new AuthException("Invalid JWT token", e);
		} catch (JOSEException | IOException e) {
			throw new InternalException(e);
		}
	}

	private Map<String, Object> getClaimsAndVerifySign(JWSObject jwsObject) throws JOSEException {
		boolean verify = verifyToken(jwsObject);
		if (verify && jwsObject.getPayload() != null) {
			var claimsMap = jwsObject.getPayload().toJSONObject();
			if (claimsMap != null) {
				return claimsMap;
			}
		}
		throw new AuthException("Invalid JWT token");
	}

	private boolean verifyToken(JWSObject jwsObject) throws JOSEException {
		return jwsObject.verify(new MACVerifier(getSignKey()));
	}

	private SecretKey getSignKey() {
		return fileKeyStoreManager.getOrCreateKey(KEY_ALIAS, HMAC_SHA_256_ALGORITHM, KEY_SIZE);
	}

	private static String getSub(Map<String, Object> claimsMap) {
		return (String) claimsMap.get(CLAIM_SUB_KEY);
	}

	private static Long getExp(Map<String, Object> claimsMap) {
		return (Long) claimsMap.get(CLAIM_EXP_KEY);
	}

	private AccessToken parseUserAccess(String sub) throws IOException {
		byte[] subBytes = IOUtils.decodeBase64(sub);
		return jsonReader.readValue(subBytes);
	}

}
