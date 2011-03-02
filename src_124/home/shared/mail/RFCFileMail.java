/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package home.shared.mail;

import com.thoughtworks.xstream.XStream;
import home.shared.CS_Constants;
import java.io.BufferedInputStream;
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
import java.util.ArrayList;
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
    


    //public static boolean create_hash = true;
    public static final String HASH_MODE = "SHA-1";

    public static final String ATTR_SUFFIX = ".attr";


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
        File attr = new File(msg.getAbsolutePath() + ATTR_SUFFIX);
        if (attr.exists())
            attr.delete();

    }

    @Override
    public void move_to( File index_msg ) throws Exception
    {
        try
        {
            File attr = new File(msg.getAbsolutePath() + ATTR_SUFFIX);
            if (attr.exists())
            {
                File new_attr = new File(index_msg.getAbsolutePath() + ATTR_SUFFIX);
                attr.renameTo(new_attr);
            }

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
            //enc_is = new BufferedInputStream( enc_is );
 
        }
        else
        {
            enc_is = new BufferedInputStream( is);
        }

        // READ HASH
        try
        {
            digest = MessageDigest.getInstance(HASH_MODE);
            enc_is = new DigestInputStream(enc_is, digest);
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
        OutputStream os = new FileOutputStream(file);

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
        }

        // CREATE DIGEST FOR HASH
        try
        {
            digest = MessageDigest.getInstance(HASH_MODE);
            enc_os = new DigestOutputStream(enc_os, digest);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException)
        {
            System.out.println("Cannot open " + HASH_MODE  +" Digest: " + noSuchAlgorithmException.getMessage());
            noSuchAlgorithmException.printStackTrace();
        }
        return enc_os;

    }

    public byte[] get_hash()
    {
        if (digest == null)
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
        {
            return digest.digest();
        }

        return null;
    }

    public boolean isEncoded()
    {
        return encoded;
    }

    public boolean isEncrypted()
    {
        return encrypted;
    }

    @Override
    public String toString()
    {
        return msg.getName();
    }

    @Override
    public boolean read_attributes() throws IOException
    {
        File attr = new File(msg.getAbsolutePath() + ATTR_SUFFIX);
        attributes.clear();

        if (!attr.exists())
            return true;

        XStream xs = new XStream();

        InputStream is = null;
        try
        {
            is = open_inputstream(attr, false);
            Object o = xs.fromXML(is);
            is.close();
            if (o instanceof ArrayList)
            {
                attributes = (ArrayList<MailAttribute>) o;
            }
            else
                return false;
        }
        catch (Exception iOException)
        {
            throw new IOException("Cannot read attributes of " + msg.getAbsolutePath() + ": " + iOException.getMessage());
        }
        finally
        {
            if (is != null)
                is.close();
        }
        return true;
    }

    @Override
    public boolean  write_attributes() throws IOException
    {
        // EMPTY, DO NOTING
        if (attributes.isEmpty())
            return true;


        File attr = new File(msg.getAbsolutePath() + ATTR_SUFFIX);

        XStream xs = new XStream();
        OutputStream is = null;
        try
        {
            is = open_outputstream(attr, false);
            xs.toXML( attributes, is);
        }
        catch (Exception iOException)
        {
            throw new IOException("Cannot write attributes of " + msg.getAbsolutePath() + ": " + iOException.getMessage());
        }
        finally
        {
            if (is != null)
                is.close();
        }
        return true;
    }

    @Override
    public long get_attr_length()
    {
        File attr = new File(msg.getAbsolutePath() + ATTR_SUFFIX);
        if (attr.exists())
            return attr.length();

        return 0;
   }
}
