/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package home.shared.mail;

import home.shared.CS_Constants;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author mw
 */
public class RFCFileMail extends RFCGenericMail
{
    File msg;
    boolean encoded;
    boolean encrypted;
    MessageDigest digest;

    int iterationCount;
    byte[] salt;
    String passPhrase;


    public static boolean create_hash = true;
    public static final String HASH_MODE = "SHA-1";


    public RFCFileMail( File _msg, Date _date, boolean _encoded )
    {
        super(_date);
        msg = _msg;
        encoded = _encoded;
        digest = null;
        encrypted = false;
    }
    public RFCFileMail( File _msg, boolean _encoded )
    {
        this( _msg, new Date(), _encoded );
    }

    public void set_encryption( String passPhrase, int iterationCount, byte[] salt)
    {
        this.iterationCount = iterationCount;
        this.salt = salt;
        this.passPhrase = passPhrase;
        encrypted = true;
    }

    @Override
    public long get_length()
    {
        return msg.length();
    }

    @Override
    public InputStream open_inputstream() throws IOException
    {
        return open_inputstream(msg, encoded);
    }

    public OutputStream open_outputstream() throws IOException
    {
        return open_outputstream(msg, encoded);
    }



   
    @Override
    public void delete()
    {
        msg.delete();
    }

    @Override
    public void move_to( File index_msg ) throws Exception
    {
        try
        {
            if (msg.renameTo(index_msg))
                msg = index_msg;
        }
        catch (Exception exc)
        {            
            throw new Exception("Cannot rename mail for index: " + exc.getMessage());
        }
    }

    public File getFile()
    {
        return msg;
    }



    private InputStream open_inputstream(File file, boolean encoded) throws IOException
    {
        InputStream is = new FileInputStream(file);


        InputStream enc_is = is;

        if (encoded)
        {
            if (encrypted)
            {
                try
                {
                    enc_is = new CryptAESInputStream(is, iterationCount, salt, passPhrase);
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException)
                {
                    throw new IOException( noSuchAlgorithmException.getMessage() );
                }
                catch (InvalidKeySpecException invalidKeySpecException)
                {
                    throw new IOException( invalidKeySpecException.getMessage() );
                }
                catch (NoSuchPaddingException noSuchPaddingException)
                {
                    throw new IOException( noSuchPaddingException.getMessage() );
                }
                catch (InvalidKeyException invalidKeyException)
                {
                    throw new IOException( invalidKeyException.getMessage() );
                }
                catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException)
                {
                    throw new IOException( invalidAlgorithmParameterException.getMessage() );
                }
            }
            else
            {
                enc_is = new EncodedMailInputStream( is );
            }
            enc_is = new BufferedInputStream( enc_is );
 
        }
        else
        {
            enc_is = new BufferedInputStream( is);
        }

        // READ HASH
        try
        {
            if (create_hash && digest == null)
            {
                digest = MessageDigest.getInstance(HASH_MODE);
                enc_is = new DigestInputStream(enc_is, digest);
            }
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException)
        {
            System.out.println("Cannot open " + HASH_MODE + " Digest: " + noSuchAlgorithmException.getMessage());
            noSuchAlgorithmException.printStackTrace();
        }
        return enc_is;

    }

    private OutputStream open_outputstream(File file, boolean encoded) throws IOException
    {
        OutputStream os;

        try
        {
             if (create_hash && digest == null)
             {
                digest = MessageDigest.getInstance(HASH_MODE);
                os = new DigestOutputStream(new FileOutputStream(file), digest);
             }
             else
             {
                 os = new FileOutputStream(file);
             }
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException)
        {
            System.out.println("Cannot open " + HASH_MODE  +" Digest: " + noSuchAlgorithmException.getMessage());
            noSuchAlgorithmException.printStackTrace();

            os = new FileOutputStream(file);
        }

        OutputStream enc_os = os;

        if (encoded)
        {
            if (encrypted)
            {
                try
                {
                    enc_os = new CryptAESOutputStream(os, iterationCount, salt, passPhrase);
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException)
                {
                    throw new IOException( noSuchAlgorithmException.getMessage() );
                }
                catch (InvalidKeySpecException invalidKeySpecException)
                {
                    throw new IOException( invalidKeySpecException.getMessage() );
                }
                catch (NoSuchPaddingException noSuchPaddingException)
                {
                    throw new IOException( noSuchPaddingException.getMessage() );
                }
                catch (InvalidKeyException invalidKeyException)
                {
                    throw new IOException( invalidKeyException.getMessage() );
                }
                catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException)
                {
                    throw new IOException( invalidAlgorithmParameterException.getMessage() );
                }
            }
            else
            {
                enc_os = new EncodedMailOutputStream( os );
            }
            return new BufferedOutputStream( enc_os );

        }
        else
        {
            return new BufferedOutputStream( os);
        }
    }

    public byte[] get_hash()
    {
        if (create_hash && digest == null)
        {
            try
            {
                InputStream is = open_inputstream();
                byte[] buff = new byte[CS_Constants.STREAM_BUFFER_LEN];

                while (true)
                {
                    int rlen = is.read(buff);
                    if (rlen == -1)
                    {
                        break;
                    }
                }
                is.close();
            }
            catch (Exception iOException)
            {
            }
        }
        if (digest != null)
            return digest.digest();

        return null;
    }
}
