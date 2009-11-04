/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package home.shared.mail;

import java.io.FilterOutputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author mw
 */
public class CryptAESOutputStream extends FilterOutputStream
{

    public CryptAESOutputStream(OutputStream os,  int iterationCount, byte[] salt, String passPhrase) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException
    {
        super( create_crypt_AES_outstream(iterationCount, salt, os, null) );
    }

    static OutputStream create_crypt_AES_outstream( int iterationCount, byte[] salt, OutputStream os, String passPhrase ) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException
    {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("AES");
        byte[] keyData = passPhrase.getBytes(); // 16 bytes for AES
        SecretKeySpec keySpec = new SecretKeySpec(keyData, "AES");
        Key key = keyFactory.generateSecret(keySpec);

        Cipher cipher = Cipher.getInstance(key.getAlgorithm());
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

        cipher.init( Cipher.ENCRYPT_MODE, key, paramSpec);

        os = new CipherOutputStream(os, cipher);

        return os;
    }

}
