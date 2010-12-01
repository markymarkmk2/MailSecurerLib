/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package home.shared.Utilities;

/**
 *
 * @author mw
 */
public interface LogListener
{
    public static final String TYP_EXCEXPTIONS = "exceptions";
    public static final String TYP_AUTH = "auth";
    public static final String TYP_EXTRACT = "extract";
    public static final String TYP_EXCHANGE = "exchange";
    public static final String TYP_PROXY = "proxy";
    public static final String TYP_HOTFOLDER = "hotfolder";
    public static final String TYP_IMPORT = "import";
    public static final String TYP_FETCHER = "fetcher";
    public static final String TYP_MILTER = "milter";
    public static final String TYP_IMAPS = "imaps";
    public static final String TYP_INDEX = "index";
    public static final String TYP_CMD = "cmd";
    public static final String TYP_VAULT = "vault";
    public static final String TYP_NOTIFICATION = "notification";
    public static final String TYP_SYSTEM = "system";
    public static final String TYP_SECURITY = "security";
    public static final String TYP_ARCHIVE = "archive";
    public static final String TYP_COMM = "comm";
    public static final String TYP_BACKUP = "backup";
    public static final String TYP_LICENSE = "license";

    public static final int LVL_INFO = -1;
    public static final int LVL_VERBOSE = 0;
    public static final int LVL_DEBUG = 1;
    public static final int LVL_WARN = 2;
    public static final int LVL_ERR = 3;

    public static final String L4J = "L4J";
    public static final String ERR = "ERR";
    public static final String INFO = "INFO";
    public static final String WRN = "WRN";
    public static final String DBG = "DBG";
    public static final String SYS = "SYS";
    public static final String SYNC = "SYNC";
   


    public void log_msg( int lvl, String typ, String txt );
    public void log_msg( int lvl, String typ, String txt, Exception ex );
    public boolean log_has_lvl(String typ, int lvl);


}
