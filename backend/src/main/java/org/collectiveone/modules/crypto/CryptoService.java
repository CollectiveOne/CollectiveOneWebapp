package org.collectiveone.modules.crypto;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CryptoService {
	
	@Value("${PUSH_ENCRYPTION_KEY}")
	private String pushEncryptionKey;

	public byte[] encrypt(String text) {
		try {
	        Key aesKey = new SecretKeySpec(pushEncryptionKey.getBytes(), "AES");
	        Cipher cipher = Cipher.getInstance("AES");
	        // encrypt the text
	        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
	        return cipher.doFinal(text.getBytes());
		} catch(Exception e) {
            e.printStackTrace();
        }
        
        return null; 
	}
		
	
	public String decrypt(byte[] encrypted) {
		
		try {
			Key aesKey = new SecretKeySpec(pushEncryptionKey.getBytes(), "AES");
	        Cipher cipher = Cipher.getInstance("AES");
	        
			cipher.init(Cipher.DECRYPT_MODE, aesKey);
	        String decrypted = new String(cipher.doFinal(encrypted));
	        return decrypted;
	        
		} catch(Exception e) {
            e.printStackTrace();
        }
		
		return null;        
	}
	
}
