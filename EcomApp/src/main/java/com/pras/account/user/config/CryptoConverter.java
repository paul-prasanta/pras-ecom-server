/*
 * Copyright (C) 2025- Prasanta Paul, https://github.com/paul-prasanta
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pras.account.user.config;

import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converter class to Encode data before storing into Database and Decode while retrieving data from Database
 * 
 * @author Prasanta Paul
 */
@Converter
public class CryptoConverter implements AttributeConverter<String, String> {
	
	// Secrete Key for encryption
	final String SECRETE_KEY = "SecurityApp@2024";
	// Encryption algorithm
	final String ENC = "AES";
	final String ALGORITHM = "AES/ECB/PKCS5Padding";
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public String convertToDatabaseColumn(String attribute) {
		// Encode data to store into database
		logger.info("Convert Application data to Database: {}", attribute);
		String value = null;
		try {
			Key key = new SecretKeySpec(SECRETE_KEY.getBytes(), ENC);
			Cipher c = Cipher.getInstance(ALGORITHM);
			c.init(Cipher.ENCRYPT_MODE, key);
			value = Base64.getEncoder().encodeToString(c.doFinal(attribute.getBytes()));
			
		} catch(Exception e) {
			logger.info("Failed to encode: "+ e.getMessage());
			e.printStackTrace();
		}
		return value;
	}

	@Override
	public String convertToEntityAttribute(String dbData) {
		// Decode data to use in Application
		logger.info("Convert Datbase to Application data: {}", dbData);
		String value = null;
		try {
			Key key = new SecretKeySpec(SECRETE_KEY.getBytes(), ENC);
			Cipher c = Cipher.getInstance(ALGORITHM);
			c.init(Cipher.DECRYPT_MODE, key);
			value = new String(c.doFinal(Base64.getDecoder().decode(dbData)));
			
		} catch(Exception e) {
			logger.info("Failed to decode: "+ e.getMessage());
			e.printStackTrace();
		}
		return value;
	}
}
