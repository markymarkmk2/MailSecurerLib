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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author mw
 */
public class CryptAESInputStream extends FilterInputStream
{
    public static boolean lame_security = false;
    static String digest_strength = "SHA-256";
    static String strong_pbe_mode ="PBEWithSHAAnd256BitAES-CBC-BC"; // "PBEWithSHAAndTwofish-CBC"

    public CryptAESInputStream(InputStream is, int iterationCount, byte[] salt,  String passPhrase) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException
    {
        super( create_crypt_AES_instream( iterationCount, salt, is, passPhrase) );
    }

    static InputStream create_crypt_AES_instream( int iterationCount, byte[] salt, InputStream is, String passPhrase ) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException
    {
        Cipher cipher = null;
        if (lame_security)
        {
            MessageDigest digester = MessageDigest.getInstance(digest_strength);
            byte[] password = passPhrase.getBytes();
            for (int i = 0; i < password.length; i++)
            {
                digester.update((byte) password[i]);
            }
            byte[] passwordData = digester.digest();
            Key key = new SecretKeySpec(passwordData, "AES");


            cipher = Cipher.getInstance(key.getAlgorithm());
            cipher.init( Cipher.DECRYPT_MODE, key);
        }
        else
        {
             // Create a PBE key and cipher.
            PBEKeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(strong_pbe_mode);
            SecretKey key = keyFactory.generateSecret(keySpec);
            PBEParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

            cipher = Cipher.getInstance(strong_pbe_mode);
            cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        }

        is = new CipherInputStream(is, cipher);

        return is;
    }
}
