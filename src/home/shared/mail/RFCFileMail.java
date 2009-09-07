/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package home.shared.mail;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

/**
 *
 * @author mw
 */
public class RFCFileMail extends RFCGenericMail
{
    File msg;

    public RFCFileMail( File _msg, Date _date )
    {
        super(_date);
        msg = _msg;
    }
    public RFCFileMail( File _msg )
    {
        this( _msg, new Date() );
    }

    @Override
    public long get_length()
    {
        return msg.length();
    }

    @Override
    public InputStream open_inputstream() throws FileNotFoundException
    {
        return new BufferedInputStream( new FileInputStream( msg));
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
}
