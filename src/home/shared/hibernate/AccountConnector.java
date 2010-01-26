package home.shared.hibernate;
// Generated 09.07.2009 10:42:43 by Hibernate Tools 3.2.1.GA

import java.util.HashSet;
import java.util.Set;




/**
 * AccountConnector generated by hbm2java
 */
public class AccountConnector  implements java.io.Serializable {


     private int id;
     private Mandant mandant;
     private String type;
     private String ip;
     private Integer port;
     private String username;
     private String pwd;
     private String searchbase;
     private String searchattribute;
     private String mailattribute;
     private String domainlist;
     private String excludefilter;
     
     private int flags;
     private Set<Role> roles = new HashSet<Role>(0);


    public AccountConnector()
    {
    }

	
    public AccountConnector(int id) {
        this.id = id;
    }

    public AccountConnector( int id, Mandant mandant, String type, String ip, Integer port, String user, String pwd, String searchbase, int flags, Set<Role> roles, String sa, String ma, String domainlist, String excludefilter )
    {
        this.id = id;
        this.mandant = mandant;
        this.type = type;
        this.ip = ip;
        this.port = port;
        this.username = user;
        this.pwd = pwd;
        this.flags = flags;
        this.roles = roles;
        this.searchbase = searchbase;
        searchattribute = sa;
        mailattribute = ma;
        this.domainlist = domainlist;
        this.excludefilter = excludefilter;
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
    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    public String getIp() {
        return this.ip;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
    }
    public Integer getPort() {
        return this.port;
    }
    
    public void setPort(Integer port) {
        this.port = port;
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
    public Set<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    /**
     * @return the searchbase
     */
    public String getSearchbase()
    {
        return searchbase;
    }

    /**
     * @param searchbase the searchbase to set
     */
    public void setSearchbase( String searchbase )
    {
        this.searchbase = searchbase;
    }

    /**
     * @return the searchattribute
     */
    public String getSearchattribute()
    {
        return searchattribute;
    }

    /**
     * @param searchattribute the searchattribute to set
     */
    public void setSearchattribute( String searchattribute )
    {
        this.searchattribute = searchattribute;
    }

    /**
     * @return the mailattribute
     */
    public String getMailattribute()
    {
        if (mailattribute == null)
            return "";
        return mailattribute;
    }

    /**
     * @param mailattribute the mailattribute to set
     */
    public void setMailattribute( String mailattribute )
    {
        this.mailattribute = mailattribute;
    }

    /**
     * @return the domainlist
     */
    public String getDomainlist()
    {
        if (domainlist == null)
            return "";
        return domainlist;
    }

    /**
     * @param domainlist the domainlist to set
     */
    public void setDomainlist( String domainlist )
    {
        this.domainlist = domainlist;
    }

    /**
     * @return the excludefilter
     */
    public String getExcludefilter()
    {
        if (excludefilter == null)
            return "";
        return excludefilter;
    }

    /**
     * @param excludefilter the excludefilter to set
     */
    public void setExcludefilter( String excludefilter )
    {
        this.excludefilter = excludefilter;
    }





}


