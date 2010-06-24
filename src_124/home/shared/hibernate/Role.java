package home.shared.hibernate;
// Generated 09.07.2009 10:42:43 by Hibernate Tools 3.2.1.GA

import java.util.HashSet;
import java.util.Set;




/**
 * Role generated by hbm2java
 */
public class Role  implements java.io.Serializable {


     private int id;
     private Mandant mandant;
     private String name;
     private String opts;
     private String accountmatch;
     private Integer license;
     private String flags;
     private String user4eyes;
     private String pwd4eyes;
     private AccountConnector accountConnector;
     private Set<RoleOption> roleOptions = new HashSet<RoleOption>(0);

    public Role()
    {
    }
    public Role(Role r)
    {
       this.id = r.id;
       this.mandant = r.mandant;
       this.name = r.name;
       this.license = r.license;
       this.opts = r.opts;
       this.accountmatch = r.accountmatch;
       this.flags = r.flags;
       this.accountConnector = r.accountConnector;
       this.roleOptions = r.roleOptions;
       this.user4eyes = r.user4eyes;
       this.pwd4eyes = r.pwd4eyes;
    }

	
    public Role(int id) {
        this.id = id;
    }
    public Role(int id, Mandant mandant, String name, String opts, String acm, Integer license, String flags, AccountConnector ac, Set<RoleOption> roleOptions, String user4eyes, String pwd4eyes) {
       this.id = id;
       this.mandant = mandant;
       this.name = name;
       this.license = license;
       this.opts = opts;
       accountmatch = acm;
       this.flags = flags;
       this.accountConnector = ac;
       this.roleOptions = roleOptions;
       this.user4eyes = user4eyes;
       this.pwd4eyes = pwd4eyes;
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
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public Integer getLicense() {
        return this.license;
    }
    
    public void setLicense(Integer license) {
        this.license = license;
    }
    public String getOpts() {
        return this.opts;
    }

    public void setOpts(String opts) {
        this.opts = opts;
    }

    public String getAccountmatch() {
        return this.accountmatch;
    }

    public void setAccountmatch(String ac) {
        this.accountmatch = ac;
    }

    public String getFlags() {
        return this.flags;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }

    public AccountConnector getAccountConnector() {
        return this.accountConnector;
    }

    public void setAccountConnector(AccountConnector ac) {
        this.accountConnector = ac;
    }

    public Set<RoleOption> getRoleOptions() {
        return this.roleOptions;
    }

    public void setRoleOptions(Set<RoleOption> roleOptions) {
        this.roleOptions = roleOptions;
    }

    public String getPwd4eyes()
    {
        return pwd4eyes;
    }

    public String getUser4eyes()
    {
        return user4eyes;
    }

    public void setPwd4eyes( String pwd4eyes )
    {
        this.pwd4eyes = pwd4eyes;
    }

    public void setUser4eyes( String user4eyes )
    {
        this.user4eyes = user4eyes;
    }


}


