/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package home.shared.mail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author mw
 */
public abstract class RFCGenericMail
{
    Date date;


    static SimpleDateFormat sdf = new SimpleDateFormat("/yyyy/MM/dd/HHmmss.SSS");


    public RFCGenericMail( Date _date )
    {
        date = _date;
    }
   

    public abstract long get_length();

    public abstract InputStream open_inputstream() throws FileNotFoundException;

    public Date getDate()
    {
        return date;
    }

    public String get_unique_id()
    {
        return sdf.format(date);
    }

    public static long get_time_from_mailfile( String path) throws ParseException
    {
        Date d = sdf.parse(path);
        return d.getTime();
    }

    public static String get_mailpath_from_time( String parent_path, long time)
    {
        Date d = new Date(time);
        return get_mailpath_from_time(parent_path, d);
    }
    public static String get_mailpath_from_time( String parent_path, Date d)
    {
        String trg_file = parent_path + "/data" + sdf.format(d);
        return trg_file;
    }



    synchronized public File create_unique_mailfile( String parent_path)
    {
        File trg_file = null;
        int retries = 100;
        do
        {
            trg_file = new File( get_mailpath_from_time(parent_path, date) );

            if (!trg_file.exists())
            {
                File parent = trg_file.getParentFile();
                if (!parent.exists())
                    parent.mkdirs();

                break;
            }
            trg_file = null;
            try
            {
                Thread.sleep(5);
            }
            catch (InterruptedException ex)
            {}

        } while (true && retries-- > 0);

        return trg_file;
    }

    public abstract void delete();
    public abstract void move_to(File targ) throws Exception;
}