/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package home.shared.license;

import java.io.IOException;
import java.net.NetworkInterface;
import java.util.Enumeration;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author mw
 */
public class HWIDLicenseTicket extends LicenseTicket
{    
    private String hwid;
   

    /**
     * @return the hwid
     */
    public String getHwid()
    {
        return hwid;
    }

    public void createTicket( String p, int _serial, int un, int mod, String _hw_id ) throws IOException
    {
        hwid = _hw_id;
        product = p;
        modules = mod;
        units = un;
        serial = _serial;
        type = LT_DEMO;
        setKey( calculate_key() );
    }


    public static String generate_hwid() throws IOException
    {
        try
        {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();

            while (en.hasMoreElements())
            {
                NetworkInterface ni = en.nextElement();
                if (ni.getName().startsWith("lo") || ni.getHardwareAddress() == null || ni.getHardwareAddress().length == 0)
                    continue;

                byte[] mac = ni.getHardwareAddress();
                String str_mac = new String(Base64.encodeBase64(mac), "UTF-8");
                return str_mac;
            }
        }
        catch (Exception exc)
        {
            throw new IOException(exc.getLocalizedMessage());
        }

        return null;
    }

    @Override
    public boolean isValid()
    {
        if (!super.isValid())
        {
            return false;
        }
        try
        {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();

            while (en.hasMoreElements())
            {
                byte[] mac = en.nextElement().getHardwareAddress();
                if (mac != null)
                {
                    String str_mac = new String(Base64.encodeBase64(mac), "UTF-8");
                    if (str_mac.compareToIgnoreCase(hwid) == 0)
                    {
                        return true;
                    }
                }
            }
            lastErrMessage = "HWID_does_not_match";
        }
        catch (Exception exc)
        {
            lastErrMessage = "Cannot_check_HWID: " + exc.getLocalizedMessage();
            if (ll != null)
                ll.error_log( lastErrMessage);
        }
        return false;
    }

    @Override
    String get_license_hash_str()
    {
        return super.get_license_hash_str() + "," +hwid;
    }
    @Override
    public String toString()
    {
        return super.toString() + " HWID:" + hwid;
    }

  
}