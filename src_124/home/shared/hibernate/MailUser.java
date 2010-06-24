package home.shared.hibernate;
// Generated 09.07.2009 10:42:43 by Hibernate Tools 3.2.1.GA

import java.util.HashSet;
import java.util.Set;




/**
 * AccountConnector generated by hbm2java
 */
public class MailUser  implements java.io.Serializable {


     private int id;
     private Mandant mandant;
     private String email;
     private String username;
     private String pwd;
     private int flags;
     private Set<MailAddress> addMailAddresses = new HashSet<MailAddress>(0);
     private Set<MailAddress> allowedViewers = new HashSet<MailAddress>(0);


    public MailUser() {
    }

	
    public MailUser(int id) {
        this.id = id;
    }

    public MailUser( int id, Mandant mandant, String email, String user, String pwd, int flags, Set<MailAddress> add_mail_addresses, Set<MailAddress> allowed_viewers )
    {
        this.id = id;
        this.mandant = mandant;
        this.email = email;
        this.username = user;
        this.pwd = pwd;
        this.flags = flags;
        this.addMailAddresses = add_mail_addresses;
        this.allowedViewers = allowed_viewers;
    }

    public MailUser( MailUser m )
    {
        this.id = m.id;
        this.mandant = m.mandant;
        this.email = m.email;
        this.username = m.username;
        this.pwd = m.pwd;
        this.flags = m.flags;
        this.addMailAddresses = m.addMailAddresses;
        this.allowedViewers = m.allowedViewers;
    }
   
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    public Mandant getMandant() {
        return this.mandant;
    }
    
    public void setMandant(Mandant mandant) {
        this.mandant = mandant;
    }
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
        /**
     * @return the user
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * @param user the user to set
     */
    public void setUsername( String user )
    {
        this.username = user;
    }

    /**
     * @return the pwd
     */
    public String getPwd()
    {
        return pwd;
    }

    /**
     * @param pwd the pwd to set
     */
    public void setPwd( String pwd )
    {
        this.pwd = pwd;
    }

    /**
     * @return the flags
     */
    public int getFlags()
    {
        return flags;
    }

    /**
     * @param flags the flags to set
     */
    public void setFlags( int flags )
    {
        this.flags = flags;
    }

    /**
     * @return the addMailAddresses
     */
    public Set<MailAddress> getAddMailAddresses()
    {
        return addMailAddresses;
    }

    /**
     * @param addMailAddresses the addMailAddresses to set
     */
    public void setAddMailAddresses( Set<MailAddress> addMailAddresses )
    {
        this.addMailAddresses = addMailAddresses;
    }

    /**
     * @return the allowedViewers
     */
    public Set<MailAddress> getAllowedViewers()
    {
        return allowedViewers;
    }

    /**
     * @param allowedViewers the allowedViewers to set
     */
    public void setAllowedViewers( Set<MailAddress> allowedViewers )
    {
        this.allowedViewers = allowedViewers;
    }
 


}


