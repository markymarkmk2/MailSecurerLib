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

    OptCBEntry( String t, int _id )
    {
        txt = t;
        token = t;
        id = _id;
    }
    public void setCb( JCheckBox _cb )
    {
        cb = _cb;
    }
    

    public static final OptCBEntry[] opt_list =
    {
        new OptCBEntry( "DEL_MAIL", 0 ),
        new OptCBEntry( "READ", 1 ),
        new OptCBEntry( "4EYES", 2 ),
        new OptCBEntry( "ADMIN", 3 )
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
    public int getId()
    {
        return id;
    }

    /**
     * @return the cb
     */
    public JCheckBox getCb()
    {
        return cb;
    }

}
