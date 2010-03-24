/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package home.shared.license;

import java.io.IOException;

/**
 *
 * @author mw
 */
public class HWIDLicenseTicket extends LicenseTicket
{    
    protected String hwid;
   

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



 

    @Override
    protected String get_license_hash_str()
    {
        return super.get_license_hash_str() + "," +hwid;
    }
    @Override
    public String toString()
    {
        return super.toString() + " HWID:" + hwid;
    }

  
}