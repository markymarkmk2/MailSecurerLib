/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package home.shared;

class EMTypeEntry
{
    EMTypeEntry( String t, String s, int i )
    {
        type = t;
        suffix = s;
        itype = i;
    }
    String type;
    String suffix;
    int itype;
}



/**
 *
 * @author mw
 */
public class CS_Constants
{
    public static final String TEXTLIST_DELIM = ",";

    public static final String TYPE_OLEXP = "OLEXP";
    public static final String TYPE_OUTLOOK = "OUTLK";
    public static final String TYPE_EML = "EML";
    public static final String TYPE_TBIRD = "TBIRD";
    public static final String TYPE_MBOX = "MBOX";
    public static final String TYPE_UNKNOWN = "UNKNOWN";

    public static final int ITYPE_OLEXP = 1;
    public static final int ITYPE_OUTLOOK = 2;
    public static final int ITYPE_EML = 3;
    public static final int ITYPE_TBIRD = 4;
    public static final int ITYPE_MBOX = 5;
    public static final int ITYPE_UNKNOWN = -1;


    // DISK SPACE FLAGS
    public static final int DS_DISABLED  = 0x001;
    public static final int DS_MODE_DATA  = 0x002;
    public static final int DS_MODE_INDEX = 0x004;
    public static final int DS_MODE_BOTH  = 0x006;

    public static final String DS_EMPTY =        "empty";
    public static final String DS_DATA =         "data";
    public static final String DS_FULL =         "full";
    public static final String DS_ERROR =        "error";
    public static final String DS_OFFLINE =      "offline";

    // FIELDNAMES
    public static final String FLD_ATTACHMENT = "FLDN_ATTACHMENT";
    public static final String FLD_ATTACHMENT_NAME = "FLDN_ATTNAME";
    public static final String FLD_BODY = "FLDN_BODY";
    public static final String FLD_UID_NAME = "FLDN_UID";
    public static final String FLD_LANG = "FLDN_LANG";
    public static final String FLD_HEADERVAR_VALUE = "FLDN_HEADERVAR_VALUE";
    public static final String FLD_HEADERVAR_NAME = "FLDN_HEADERVAR_NAME";
    public static final String FLD_MA = "FLDN_MA";
    public static final String FLD_DA = "FLDN_DA";
    public static final String FLD_DS = "FLDN_DS";
    public static final String FLD_TM = "FLDN_TM";
    public static final String FLD_SIZE = "FLDN_SZ";
    public static final String FLD_SUBJECT = "FLDN_SJ";
    public static final String FLD_DATE = "FLDN_DT";
    public static final String FLD_HAS_ATTACHMENT = "FLDN_HATT";
    public static final String FLD_META_ADDRESS = "FLDN_ADDR";
    public static final String FLD_HASH = "FLDN_HASH";
    public static final String FLD_TO = "To";
    public static final String FLD_FROM = "From";
    public static final String FLD_CC = "CC";
    public static final String FLD_BCC = "BCC";
    public static final String FLD_DELIVEREDTO = "DeliveredTo";
    public static final String FLD_CONTENT_TYPE = "Content-Type";
    public static final String FLD_CHARSET = "FLDN_CS";

    // VIRTUAL FIELDNAMES
    public static final String  VFLD_MAIL = "FLDV_MAIL";  // From To CC BCC
    public static final String[] VFLD_MAIL_FIELDS = {FLD_TO, FLD_FROM, FLD_CC, FLD_BCC, FLD_DELIVEREDTO};
    public static final String   VFLD_TXT =  "FLDV_TEXT";  // Subject Body Attachment
    public static final String[] VFLD_TXT_FIELDS = {FLD_SUBJECT, FLD_BODY, FLD_ATTACHMENT};
    public static final String   VFLD_ALL =  "FLDV_ALL";   // V_MAIL + V_TEXT + FLD_ATTACHMENT_NAME
    public static final String[] VFLD_ALL_FIELDS = {FLD_TO, FLD_FROM, FLD_CC, FLD_BCC, FLD_DELIVEREDTO,FLD_SUBJECT, FLD_BODY, FLD_ATTACHMENT, FLD_ATTACHMENT_NAME };
    

    public static final String IFETCHER_TYPE_IMAP = "imap";
    public static final String IFETCHER_TYPE_ENVELOPE = "exchange";

    public static int STREAM_BUFFER_LEN = 128*1024;

    // ACCOUNTCONNECTOR FLAGS // MANDANT SMTP_FLAGS
    public static int ACCT_DISABLED = 0x001;
    public static int ACCT_USE_SSL = 0x002;
    public static int ACCT_USE_TLS_IF_AVAIL = 0x004;
    public static int ACCT_USE_TLS_FORCE = 0x008;
    public static int ACCT_HAS_TLS_CERT = 0x010;
    public static int ACCT_ANONYMOUS = 0x020;
    public static int ACCT_USER_IS_MAIL = 0x040;

    // ROLE FLAGS
    public static final int ROLE_DISABLED = 0x01;
    public static final int ROLE_ACM_COMPRESSED = 0x02;

    // HF
    public static final int HF_FLAG_DISABLED = 0x01;

    // IMAPFETCHER
    public static int IMF_DISABLED = 0x01;
    public static int IMF_USE_SSL = 0x002;
    public static int IMF_USE_TLS_IF_AVAIL = 0x004;
    public static int IMF_USE_TLS_FORCE = 0x008;
    public static int IMF_HAS_TLS_CERT = 0x010;
    public static int IMF_POP3 = 0x020;
    public static int IMF_USE_IDLE = 0x040;

    // DISKARCHIVE
    public static int DA_DISABLED = 0x01;


    // USERMODE
    public enum USERMODE
    {
        UL_INVALID,
        UL_DUMMY,
        UL_USER,
        UL_ADMIN,
        UL_SYSADMIN
    };

    static EMTypeEntry[] em_typelist =
    {
        new EMTypeEntry( TYPE_OLEXP, "dbx", ITYPE_OLEXP),
        new EMTypeEntry( TYPE_OUTLOOK, "pst", ITYPE_OUTLOOK),
        new EMTypeEntry( TYPE_EML, "eml", ITYPE_EML),
        new EMTypeEntry( TYPE_TBIRD, "mbox", ITYPE_TBIRD),
        new EMTypeEntry( TYPE_MBOX, "mbox", ITYPE_MBOX),
        new EMTypeEntry( TYPE_UNKNOWN, "dat", ITYPE_UNKNOWN)
    };
    static AccountConnectorTypeEntry[] act_typelist =
    {
        new AccountConnectorTypeEntry("ad","ActiveDirectory"),
        new AccountConnectorTypeEntry("ldap","LDAP"),
        new AccountConnectorTypeEntry("smtp","SMTP"),
        new AccountConnectorTypeEntry("pop","POP3"),
        new AccountConnectorTypeEntry("imap","IMAP"),
        new AccountConnectorTypeEntry("dbs","Database")
    };

    public static final String get_suffix_from_em_type(String type)
    {
        for (int i = 0; i < em_typelist.length; i++)
        {
            EMTypeEntry elem = em_typelist[i];
            if (type.compareTo(elem.type) == 0)
                return elem.suffix;
        }
        return "dat";
    }
    public static final String get_type_from_em_name(String name)
    {
        String suffix = name;
        int idx = name.lastIndexOf('.');
        if (idx >= 0 && idx < name.length())
            suffix = name.substring(idx + 1);

        for (int i = 0; i < em_typelist.length; i++)
        {
            EMTypeEntry elem = em_typelist[i];
            if (suffix.compareTo(elem.suffix) == 0)
                return elem.type;
        }

        return TYPE_UNKNOWN;
    }
    public static final int get_itype_from_em_name(String name)
    {
        String suffix = name;
        int idx = name.lastIndexOf('.');
        if (idx >= 0 && idx < name.length())
            suffix = name.substring(idx + 1);

        for (int i = 0; i < em_typelist.length; i++)
        {
            EMTypeEntry elem = em_typelist[i];
            if (suffix.toLowerCase().compareTo(elem.suffix) == 0)
                return elem.itype;
        }

        return ITYPE_UNKNOWN;
    }

    public static final String get_type_from_ac_name(String name)
    {
        for (int i = 0; i < act_typelist.length; i++)
        {
            AccountConnectorTypeEntry elem = act_typelist[i];
            if (name.compareTo(elem.name) == 0)
                return elem.type;
        }

        return "????";
    }
    public static final String get_name_from_ac_type(String type)
    {
        for (int i = 0; i < act_typelist.length; i++)
        {
            AccountConnectorTypeEntry elem = act_typelist[i];
            if (type.compareTo(elem.type) == 0)
                return elem.name;
        }

        return "????";
    }
    public static final int get_ac_list_count()
    {
        return act_typelist.length;
    }
    public static final String get_ac_name(int idx)
    {
        AccountConnectorTypeEntry elem = act_typelist[idx];
        return elem.name;
    }
    public static final String get_ac_type(int idx)
    {
        AccountConnectorTypeEntry elem = act_typelist[idx];
        return elem.type;
    }
    public static final AccountConnectorTypeEntry get_ac(int idx)
    {
        return act_typelist[idx];
    }
   


}
