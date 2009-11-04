/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package home.shared.mail;

import java.io.FilterInputStream;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author mw
 */
public class CryptAESInputStream extends FilterInputStream
{

    public CryptAESInputStream(InputStream is,  int iterationCount, byte[] salt, String passPhrase) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException
    {
        super( create_crypt_AES_instream(iterationCount, salt, is, passPhrase) );
    }

    static InputStream create_crypt_AES_instream( int iterationCount, byte[] salt, InputStream is, String passPhrase ) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException
    {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("AES");
        byte[] keyData = passPhrase.getBytes(); // 16 bytes for AES
        SecretKeySpec keySpec = new SecretKeySpec(keyData, "AES");
        Key key = keyFactory.generateSecret(keySpec);

        Cipher cipher = Cipher.getInstance(key.getAlgorithm());
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

        cipher.init( Cipher.DECRYPT_MODE, key, paramSpec);

        is = new CipherInputStream(is, cipher);

        return is;
    }

}
