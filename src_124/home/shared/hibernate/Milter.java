package home.shared.hibernate;
// Generated 09.07.2009 10:42:43 by Hibernate Tools 3.2.1.GA



/**
 * Milter generated by hbm2java
 */
public class Milter  implements java.io.Serializable {


     private int id;
     private DiskArchive diskArchive;
     private Mandant mandant;
     private String inServer;
     private Integer inPort;
     private String outServer;
     private Integer outPort;
     private String type;
     private String flags;

    public Milter() {
    }

	
    public Milter(int id) {
        this.id = id;
    }
    public Milter(int id, DiskArchive diskArchive, Mandant mandant, String inServer, Integer inPort, String outServer, Integer outPort, String type, String flags) {
       this.id = id;
       this.diskArchive = diskArchive;
       this.mandant = mandant;
       this.inServer = inServer;
       this.inPort = inPort;
       this.outServer = outServer;
       this.outPort = outPort;
       this.type = type;
       this.flags = flags;
    }

    public Milter( Milter m )
    {
       this.id = m.id;
       this.diskArchive = m.diskArchive;
       this.mandant = m.mandant;
       this.inServer = m.inServer;
       this.inPort = m.inPort;
       this.outServer = m.outServer;
       this.outPort = m.outPort;
       this.type = m.type;
       this.flags = m.flags;
    }
   
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    public DiskArchive getDiskArchive() {
        return this.diskArchive;
    }
    
    public void setDiskArchive(DiskArchive diskArchive) {
        this.diskArchive = diskArchive;
    }
    public Mandant getMandant() {
        return this.mandant;
    }
    
    public void setMandant(Mandant mandant) {
        this.mandant = mandant;
    }
    public String getInServer() {
        return this.inServer;
    }
    
    public void setInServer(String inServer) {
        this.inServer = inServer;
    }
    public Integer getInPort() {
        return this.inPort;
    }
    
    public void setInPort(Integer inPort) {
        this.inPort = inPort;
    }
    public String getOutServer() {
        return this.outServer;
    }
    
    public void setOutServer(String outServer) {
        this.outServer = outServer;
    }
    public Integer getOutPort() {
        return this.outPort;
    }
    
    public void setOutPort(Integer outPort) {
        this.outPort = outPort;
    }
    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    public String getFlags() {
        return this.flags;
    }
    
    public void setFlags(String flags) {
        this.flags = flags;
    }




}


