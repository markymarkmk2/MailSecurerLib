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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 *
 * @author mw
 */
public class RFCFileMail extends RFCGenericMail
{
    File msg;
    boolean encoded;
    MessageDigest digest;

    public static boolean dflt_encoded = false;

    public RFCFileMail( File _msg, Date _date, boolean _encoded )
    {
        super(_date);
        msg = _msg;
        encoded = _encoded;
        digest = null;
    }
    public RFCFileMail( File _msg, boolean _encoded )
    {
        this( _msg, new Date(), _encoded );
    }

    @Override
    public long get_length()
    {
        return msg.length();
    }

    @Override
    public InputStream open_inputstream() throws FileNotFoundException
    {
        return open_inputstream(msg, encoded);
    }

    public OutputStream open_outputstream() throws FileNotFoundException
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



    private InputStream open_inputstream(File file, boolean encoded) throws FileNotFoundException
    {
        InputStream is;

        try
        {
            digest = MessageDigest.getInstance("SHA");
            is = new DigestInputStream(new FileInputStream(file), digest);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException)
        {
            System.out.println("Cannot open SHA Digest: " + noSuchAlgorithmException.getMessage());
            noSuchAlgorithmException.printStackTrace();

            is = new FileInputStream(file);
        }

        if (!encoded)
            return new BufferedInputStream( is );
        else
            return new BufferedInputStream( new EncodedMailInputStream( is ));
    }
    private OutputStream open_outputstream(File file, boolean encoded) throws FileNotFoundException
    {
        OutputStream os;

        try
        {
            digest = MessageDigest.getInstance("SHA");
            os = new DigestOutputStream(new FileOutputStream(file), digest);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException)
        {
            System.out.println("Cannot open SHA Digest: " + noSuchAlgorithmException.getMessage());
            noSuchAlgorithmException.printStackTrace();

            os = new FileOutputStream(file);
        }

        if (!encoded)
            return new BufferedOutputStream( os );
        else
            return new BufferedOutputStream( new EncodedMailOutputStream(os));
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
            return digest.digest();

        return null;
    }
}
