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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
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
public class CryptAESOutputStream extends FilterOutputStream
{
    public static boolean lame_security = false;
    static String digest_strength = "SHA-256";
    static String strong_pbe_mode ="PBEWithSHAAnd256BitAES-CBC-BC"; // "PBEWithSHAAndTwofish-CBC"

    public CryptAESOutputStream( OutputStream os, int iterationCount, byte[] salt, String passPhrase ) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException
    {
        super(create_crypt_AES_outstream(iterationCount, salt, os, passPhrase));
    }

    static OutputStream create_crypt_AES_outstream( int iterationCount, byte[] salt, OutputStream os, String passPhrase ) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException
    {
        // registering Bouncy Castle
        // Create the salt.
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
            cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        }
        

        os = new CipherOutputStream(os, cipher);

        return os;
    }
}
