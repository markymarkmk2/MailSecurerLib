/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package home.shared.mail;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 *
 * @author mw
 */
public class RFCFileMail extends RFCGenericMail
{
    File msg;
    boolean encoded;

    public static boolean dflt_encoded = false;

    public RFCFileMail( File _msg, Date _date, boolean _encoded )
    {
        super(_date);
        msg = _msg;
        encoded = _encoded;
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


    public static InputStream open_inputstream(File file, boolean encoded) throws FileNotFoundException
    {
        if (encoded)
            return new BufferedInputStream( new FileInputStream( file));
        else
            return new BufferedInputStream( new EncodedMailInputStream( new FileInputStream( file)));
    }
    public static OutputStream open_outputstream(File file, boolean encoded) throws FileNotFoundException
    {
        if (encoded)
            return new BufferedOutputStream( new FileOutputStream( file));
        else
            return new BufferedOutputStream( new EncodedMailOutputStream( new FileOutputStream( file)));
    }


}
