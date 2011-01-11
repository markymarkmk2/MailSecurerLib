/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package home.shared.SQL;

import home.shared.hibernate.AccountConnector;
import home.shared.hibernate.Role;
import home.shared.hibernate.RoleOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author mw
 */
public class UserSSOEntry
{

    String user;
    String pwd;
    Role role;
    AccountConnector acct;
    long checked;
    long last_auth;
    private ArrayList<String> mail_list;
    int user_sso_id;
    int ma_id;


    public UserSSOEntry()
    {
        user_sso_id = -1;
        ma_id = -1;
    }

    public UserSSOEntry( String user, String pwd, Role role, AccountConnector acct, long checked, long last_auth, int _id, int _ma_id )
    {
        this.user = user;
        this.pwd = pwd;
        this.role = role;
        this.acct = acct;
        this.checked = checked;
        this.last_auth = last_auth;
        user_sso_id = _id;
        ma_id = _ma_id;
    }

    public AccountConnector getAcct()
    {
        return acct;
    }

    public Role getRole()
    {
        return role;
    }

    public final ArrayList<String> getMail_list()
    {
        return mail_list;
    }
    
    // ADMIN HAS NO REGULAR AUTH
    public boolean is_admin()
    {
        // ADMIN FROM MANDANT LOGIN
        if (role == null && acct == null)
            return true;

        // CHECK FOR ADMIN FLAG IN ROLE
        if (role != null)
        {
            Set<RoleOption> ros = role.getRoleOptions();
            for (Iterator<RoleOption> it = ros.iterator(); it.hasNext();)
            {
                RoleOption roleOption = it.next();
                if (roleOption.getToken().equals(OptCBEntry.ADMIN))
                    return true;
            }
        }
        return false;
    }
    public boolean role_has_option(String option)
    {
        if (role != null)
        {
            Set<RoleOption> ros = role.getRoleOptions();
            for (Iterator<RoleOption> it = ros.iterator(); it.hasNext();)
            {
                RoleOption roleOption = it.next();
                if (roleOption.getToken().equals(option))
                    return true;
            }
        }
        return false;
    }
    public boolean is_auditor()
    {
        return role_has_option( OptCBEntry.AUDIT );
    }

    public String getUser()
    {
        return user;
    }

    public String getPwd()
    {
        return pwd;
    }

    public int getUser_sso_id()
    {
        return user_sso_id;
    }

    public long getLast_auth()
    {
        return last_auth;
    }

    public void setLast_auth( long last_auth )
    {
        this.last_auth = last_auth;
    }

    public long getChecked()
    {
        return checked;
    }

    public void setChecked( long checked )
    {
        this.checked = checked;
    }

    public void setMail_list( ArrayList<String> mail_list )
    {
        this.mail_list = mail_list;
    }

    public int get_ma_id()
    {
        return ma_id;
    }
    public String get_sso_str_id()
    {
        return ma_id + "." + user_sso_id;
    }
    
}
