/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package home.shared.Utilities;

import java.util.Date;

/**
 *
 * @author mw
 */
public class LicenseTicket
{
    public static final int LTM_4EYES = 0x0001;

    public static final int LT_DEMO = 1;
    public static final int LT_VALID = 2;
    private String product;
    private int units;
    private int serial;
    private long modules;
    private String hwid;
    private Date expires;
    private int type;

    /**
     * @return the product
     */
    public String getProduct()
    {
        return product;
    }

    /**
     * @return the units
     */
    public int getUnits()
    {
        return units;
    }

    /**
     * @return the serial
     */
    public int getSerial()
    {
        return serial;
    }

    /**
     * @return the modules
     */
    public long getModules()
    {
        return modules;
    }

    /**
     * @return the hwid
     */
    public String getHwid()
    {
        return hwid;
    }

    /**
     * @return the expires
     */
    public Date getExpires()
    {
        return expires;
    }

    /**
     * @return the type
     */
    public int getType()
    {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType( int type )
    {
        this.type = type;
    }
    public boolean has_module( int mc )
    {
        return (mc & modules) == mc;
    }
}