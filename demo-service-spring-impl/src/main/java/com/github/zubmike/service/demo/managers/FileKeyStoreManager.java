package com.github.zubmike.service.demo.managers;

import com.github.zubmike.core.utils.InternalException;
import com.github.zubmike.service.demo.conf.FileKeyStoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class FileKeyStoreManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileKeyStoreManager.class);

	private final Map<String, SecretKey> cachedKeyMap = new HashMap<>();

	private File file;
	private char[] password;
	private KeyStore keyStore;

	@Autowired
	public FileKeyStoreManager(FileKeyStoreProperties fileKeyStoreProperties) {
		init(fileKeyStoreProperties);
	}

	private void init(FileKeyStoreProperties fileKeyStoreProperties) {
		this.file = new File(fileKeyStoreProperties.getPath());
		this.password = fileKeyStoreProperties.getPassword().toCharArray();
		LOGGER.info("key store file: {}", file.getAbsoluteFile());
		try {
			keyStore = KeyStore.getInstance(fileKeyStoreProperties.getType());
			keyStore.load(null, null);
			if (!file.exists()) {
				save();
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new InternalException(e);
		}
	}

	public void setKey(String alias, SecretKey key) {
		try {
			keyStore.setKeyEntry(alias, key, password, null);
			cachedKeyMap.put(alias, key);
			save();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new InternalException(e);
		}
	}

	private void save() throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
		try (var fileOutputStream = new FileOutputStream(file)) {
			keyStore.store(fileOutputStream, password);
		}
	}

	public Optional<SecretKey> getKey(String alias) {
		SecretKey key = cachedKeyMap.get(alias);
		if (key == null) {
			key = loadKey(alias);
			if (key != null) {
				cachedKeyMap.put(alias, key);
			}
		}
		return Optional.ofNullable(key);
	}

	private SecretKey loadKey(String alias) {
		try (FileInputStream fileInputStream = new FileInputStream(file)) {
			keyStore.load(fileInputStream, password);
			return (SecretKey) keyStore.getKey(alias, password);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new InternalException(e);
		}
	}

	public SecretKey getOrCreateKey(String alias, String algorithm, int size) {
		return getKey(alias).orElseGet(() -> {
			try {
				var keyGenerator = KeyGenerator.getInstance(algorithm);
				keyGenerator.init(size);
				var secretKey = keyGenerator.generateKey();
				setKey(alias, secretKey);
				return secretKey;
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
				throw new InternalException(e);
			}
		});
	}

}
