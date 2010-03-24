/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package home.shared.SQL;

import javax.swing.JCheckBox;

/**
 *
 * @author mw
 */
public class OptCBEntry
{
    private String txt;
    private String token;
    private int id;
    private JCheckBox cb;

    public static final String READ = "READ";
    public static final String RESTORE = "RESTORE";
    public static final String EXPORT = "EXPORT";
    public static final String DELETE = "DELETE";
    public static final String AUDIT = "AUDIT";
    public static final String _4EYES = "4EYES";
    public static final String ADMIN = "ADMIN";
    public static final String IMAP_BROWSE = "IMAPBROWSE";

    OptCBEntry( String t, int _id )
    {
        txt = t;
        token = t;
//        id = _id;
    }
    public void setCb( JCheckBox _cb )
    {
        cb = _cb;
    }
    

    
    public static final OptCBEntry[] opt_list =
    {
        new OptCBEntry( READ, 1 ),
        new OptCBEntry( RESTORE, 2 ),
        new OptCBEntry( EXPORT, 3 ),
        new OptCBEntry( DELETE, 4 ),
        new OptCBEntry( AUDIT, 5 ),
        new OptCBEntry( _4EYES, 6 ),
        new OptCBEntry( ADMIN, 7 ),
        new OptCBEntry( IMAP_BROWSE, 8 )
    };

    /**
     * @return the txt
     */
    public String getTxt()
    {
        return txt;
    }

    /**
     * @param txt the txt to set
     */
    public void setTxt( String txt )
    {
        this.txt = txt;
    }

    /**
     * @return the token
     */
    public String getToken()
    {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken( String token )
    {
        this.token = token;
    }

    /**
     * @return the id
     */
  /*  public int getId()
    {
        return id;
    }
*/
    /**
     * @return the cb
     */
    public JCheckBox getCb()
    {
        return cb;
    }

}
