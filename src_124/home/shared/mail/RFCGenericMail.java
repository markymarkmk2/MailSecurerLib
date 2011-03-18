/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package home.shared.mail;

import home.shared.CS_Constants;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.mail.Address;



/**
 *
 * @author mw
 */
public abstract class RFCGenericMail
{
    Date date;

    public static final int ENC_NONE = 0;
    public static final int ENC_AES = 1;

    public static final String MATTR_LUCENE = "luce";
    public static final String MATTR_ENVELOPE = "envl";


    
    public enum FILENAME_MODE
    {
        HMS_FILE,
        H_DIR_MS_FILE,  // THIS WAS BUGGY, KEEP CAMPATIBLE
        H_OK_DIR_MS_FILE
    };
    
 /*   public static boolean dflt_encoded = false;
    public static boolean dflt_encrypted = false;
    public static int dflt_encoding = ENC_NONE;
*/
    public static boolean dflt_encoded = true;
    public static boolean dflt_encrypted = true;
    public static int dflt_encoding = ENC_AES;

    ArrayList<MailAttribute> attributes;

    public ArrayList<MailAttribute> getAttributes()
    {
        return attributes;
    }

    public void setAttributes( ArrayList<MailAttribute> attributes )
    {
        this.attributes = attributes;
    }


    public ArrayList<String> get_attribute( String type, String name )
    {
        ArrayList<String> ret = new ArrayList<String>();

        for (int i = 0; i < attributes.size(); i++)
        {
            MailAttribute mailAttribute = attributes.get(i);
            if (mailAttribute.isType(type) && mailAttribute.isName(name))
            {
                ret.add( mailAttribute.getValue() );
            }
        }
        return ret;
    }
    public ArrayList<String[]> get_attribute_arr( String type )
    {
        if (attributes.size() == 0)
            return null;
        
        ArrayList<String[]> ret = new ArrayList<String[]>();

        for (int i = 0; i < attributes.size(); i++)
        {
            MailAttribute mailAttribute = attributes.get(i);
            if (mailAttribute.isType(type))
            {
                String[] entry = {mailAttribute.getName(), mailAttribute.getValue()};
                ret.add( entry );
            }
        }
        return ret;
    }

    public static String get_suffix_for_encoded( )
    {
        return ".enc";
    }
    public static String get_suffix_for_encrypt_mode( int enc_mode )
    {
        switch (enc_mode)
        {
            case ENC_NONE: return "";
            case ENC_AES: return ".sae";
        }
        return ".vni";
    }
    public static int get_encrypt_mode_for_suffix( String filename )
    {
        if (filename.endsWith(".sae"))
            return ENC_AES;

        return ENC_NONE;
    }

    static SimpleDateFormat hms_mailpath_sdf = new SimpleDateFormat("/yyyy/MM/dd/HHmmss.SSS");
    static SimpleDateFormat broken_h_dir_ms_mailpath_sdf = new SimpleDateFormat("/yyyy/MM/dd/HHmmss.SSS");
    static SimpleDateFormat h_dir_ms_mailpath_sdf = new SimpleDateFormat("/yyyy/MM/dd/HH/mmss.SSS");
    ArrayList<Address> bcc_list;

    public RFCGenericMail( Date _date )
    {
        date = _date;
        bcc_list = new ArrayList<Address>();
        attributes = new ArrayList<MailAttribute>();
    }

    
    public void add_attribute( String type, String name, String value )
    {
        MailAttribute attr = new MailAttribute(type, name, value);
        attributes.add(attr);
    }
    public void add_attribute(MailAttribute attr )
    {
        for (int i = 0; i < attributes.size(); i++)
        {
            MailAttribute mailAttribute = attributes.get(i);
            if (mailAttribute.equals(attr))
                return;

        }
        attributes.add(attr);
    }

    public void set_bcc( ArrayList<Address> bcc_list )
    {
        for (int i = 0; i < bcc_list.size(); i++)
        {
            Address address = bcc_list.get(i);
            MailAttribute attr = new MailAttribute(MATTR_LUCENE, CS_Constants.FLD_BCC, address.toString());
            attributes.add(attr);
        }
    }
    

    public abstract long get_length();
    public abstract long get_attr_length();
    public abstract byte[] get_hash();

    public abstract InputStream open_inputstream() throws IOException;
    public abstract boolean read_attributes() throws IOException;
    public abstract boolean write_attributes() throws IOException;

    public Date getDate()
    {
        return date;
    }
/*
    public String get_unique_id()
    {
        return mailpath_sdf.format(date);
    }
*/
    public static long get_time_from_mailfile( String path, int enc_mode, FILENAME_MODE mode) throws ParseException
    {
        String suffix = get_suffix_for_encrypt_mode( enc_mode );
        if (suffix.length() > 0)
        {
            path = path.substring(0, path.length() - suffix.length());
        }
        
        path = path.substring("/data".length());
        if (mode == FILENAME_MODE.HMS_FILE)
        {
            Date d = hms_mailpath_sdf.parse(path);
            return d.getTime();
        }
        if (mode == FILENAME_MODE.H_DIR_MS_FILE)
        {
            Date d = broken_h_dir_ms_mailpath_sdf.parse(path);
            return d.getTime();
        }
        if (mode == FILENAME_MODE.H_OK_DIR_MS_FILE)
        {
            Date d = h_dir_ms_mailpath_sdf.parse(path);
            return d.getTime();
        }
        return 0;
    }

    public static String get_mailpath_from_time( String parent_path, long time, int enc_mode, FILENAME_MODE mode )
    {       
        Date d = new Date(time);
        return get_mailpath_from_time(parent_path, d, enc_mode, mode);
    }
    public static String get_mailpath_from_time( String parent_path, Date d, int enc_mode, FILENAME_MODE mode)
    {
        String trg_file = null;
        String suffix = get_suffix_for_encrypt_mode( enc_mode );
        if (mode == FILENAME_MODE.HMS_FILE)
        {
            trg_file = parent_path + "/data" + hms_mailpath_sdf.format(d) + suffix;
        }
        if (mode == FILENAME_MODE.H_DIR_MS_FILE)
        {
            trg_file = parent_path + "/data" + broken_h_dir_ms_mailpath_sdf.format(d) + suffix;
        }
        if (mode == FILENAME_MODE.H_OK_DIR_MS_FILE)
        {
            trg_file = parent_path + "/data" + h_dir_ms_mailpath_sdf.format(d) + suffix;
        }
        return trg_file;
    }


    // THIS ONE IS THE LAST TIME WE SET THE TIMESTAMP, AFTER THIS WE HAVE A UUID
    synchronized public File create_unique_mailfile( String parent_path, int enc_mode, FILENAME_MODE fmode )
    {
        File trg_file = null;
        int retries = 100;
        File pp = new File( parent_path);
        if (!pp.exists())
        {
            if (pp.getParentFile().exists())
            {
                pp.mkdir();
            }
        }
        if (!pp.exists())
            return null;

        
        do
        {
            trg_file = new File( get_mailpath_from_time(parent_path, date, enc_mode, fmode) );

            if (!trg_file.exists())
            {
                File parent = trg_file.getParentFile();
                if (!parent.exists())
                    parent.mkdirs();

                try
                {
                    FileWriter fw = new FileWriter(trg_file);
                    fw.close();
                    break; // REGULAR EXIT
                }
                catch (IOException iOException)
                {
                }
            }

            trg_file = null;
            try
            {
                Thread.sleep(5);
            }
            catch (InterruptedException ex)
            {}

            date = new Date( System.currentTimeMillis() );

        } while (true && retries-- > 0);

        return trg_file;
    }

    public abstract void delete();
    public abstract void move_to(File targ) throws Exception;

    public abstract boolean isEncoded();
}
