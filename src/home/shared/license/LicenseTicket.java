/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package home.shared.license;

import home.shared.Utilities.LogListener;
import java.io.IOException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author mw
 */
public abstract class LicenseTicket
{
    public static final String PRODUCT_BASE = "MailSecurer";
    public static final int LTM_4EYES = 0x0001;

    public static final int LT_DEMO = 1;
    public static final int LT_VALID = 2;
    protected String product;
    protected int units;
    protected int serial;
    protected long modules;
    protected int type;
    private String key;
    String lastErrMessage;
    
    protected LogListener ll;

    public void setLogListener( LogListener ll)
    {
        this.ll = ll;
    }

    public String getLastErrMessage()
    {
        return lastErrMessage;
    }


    /**
     * @return the product
     */
    public String getProduct()
    {
        return product;
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

    public int getUnits()
    {
        return units;
    }

    public boolean hasModule( int mc )
    {
        return (mc & modules) == mc;
    }

    public boolean isValid()
    {
        boolean ret = false;
        try
        {
            String act_key = calculate_key();
            ret = key.equals(act_key);
            if (!ret)
                lastErrMessage = "Key_does_not_match";

        }
        catch (IOException iOException)
        {
            if (ll != null)
                ll.err_log("cannot calculate key", iOException);
        }
        return ret;
    }

    /**
     * @return the key
     */
    public String getKey()
    {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey( String key )
    {
        this.key = key;
    }

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private static final String hash_key = "penner42";


    // USED FOR HASH CREATION
    String get_license_hash_str()
    {
        return product + "," +modules + "," + type + "," + units + "," + serial;
    }

    public String calculate_key() throws IOException
    {
        
        String data = get_license_hash_str();
        try
        {

            // get an hmac_sha1 key from the raw key bytes
            SecretKeySpec signingKey = new SecretKeySpec(hash_key.getBytes(), HMAC_SHA1_ALGORITHM);

            // get an hmac_sha1 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);

            // compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(data.getBytes());

            // base64-encode the hmac is atleast 20 byte long
            String result = new String(Base64.encodeBase64(rawHmac)).toUpperCase();
            StringBuffer sb = new StringBuffer();
            int idx = 0;
            for (int blocks = 0; blocks < 5; blocks++)
            {
                if (blocks > 0)
                    sb.append('-');
                for (int chars = 0; chars < 4; chars++)
                {
                    // DO NOT ALLOW NONLITERALS OR '0'
                    while (!Character.isLetterOrDigit(result.charAt(idx)) || result.charAt(idx) == '0')
                    {
                        idx++;
                        // WRAPAROUND
                        if (idx >= result.length())
                            idx = 0;
                    }


                    sb.append( result.charAt(idx));
                    idx++;
                    if (idx >= result.length())
                        idx = 0;
                }
            }
            return sb.toString();

        }
        catch (Exception e)
        {
            throw new IOException("Failed to generate license key : " + e.getMessage());
        }        
    }

    @Override
    public String toString()
    {
        return product + " serial:" + serial + " units:" + units + " module:" + Long.toHexString(modules);
    }



    
}
