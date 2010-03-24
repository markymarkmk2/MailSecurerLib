/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package home.shared.mail;

/**
 *
 * @author mw
 */

// VERY VERY SIMPLE IMPLEMENTATION OF VALIDITY CHECK, IS ONLY FOR INTERNAL USAGE
public class RFCMailAddress
{
    String adr;

    private String parse_mail( String adr )
    {
        int start_idx = adr.indexOf('<');
        int end_idx = adr.indexOf('>');
        if (start_idx < 0)
        {
            return adr.trim().toLowerCase();
        }
        if (end_idx < 0)
        {
            return adr.substring(start_idx + 1).trim().toLowerCase();
        }
        if (start_idx < end_idx)
            return adr.substring(start_idx + 1, end_idx).trim().toLowerCase();
        
        // IN CASE OF FAILURE...
        return adr;
    }
    
    public enum ADR_TYPE
    {
        FROM,
        TO,
        CC,
        BCC
    };
    ADR_TYPE adr_type;
    boolean is_from;

    public RFCMailAddress( String adr, ADR_TYPE _type )
    {
        this.adr = parse_mail(adr);
        adr_type = _type;
    }

    public boolean is_from()
    {
        if (adr_type == ADR_TYPE.FROM)
            return true;
        return false;
    }
    public boolean is_valid()
    {
        return is_valid(adr);
    }
    public static boolean is_valid( String adr)
    {
        int amp_idx = adr.indexOf('@');
        int dot_idx = adr.indexOf('.');
        int space_idx = adr.indexOf(' ');
        if (space_idx >= 0)
            return false;
        
        if (dot_idx <= 0 || amp_idx <= 0)
            return false;
        if (dot_idx < amp_idx)
            return false;
        return true;
    }

    public String get_domain()
    {
        int idx = adr.indexOf('@') + 1;
        return adr.substring(idx);
    }
    public String get_user()
    {
        int idx = adr.indexOf('@');
        return adr.substring(0, idx);
    }
    public String get_mail()
    {
        return adr;
    }

    @Override
    public String toString()
    {
        return get_mail();
    }

    public ADR_TYPE getAdr_type()
    {
        return adr_type;
    }

}
