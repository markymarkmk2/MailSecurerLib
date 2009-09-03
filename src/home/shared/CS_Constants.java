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
    public static final String TYPE_OLEXP = "OLEXP";
    public static final String TYPE_OUTLOOK = "OUTLK";
    public static final String TYPE_EML = "EML";
    public static final String TYPE_TBIRD = "TBIRD";
    public static final String TYPE_UNKNOWN = "UNKNOWN";

    public static final int ITYPE_OLEXP = 1;
    public static final int ITYPE_OUTLOOK = 2;
    public static final int ITYPE_EML = 3;
    public static final int ITYPE_TBIRD = 4;
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



    public static int STREAM_BUFFER_LEN = 128*1024;

    static EMTypeEntry[] typelist =
    {
        new EMTypeEntry( TYPE_OLEXP, "dbx", ITYPE_OLEXP),
        new EMTypeEntry( TYPE_OUTLOOK, "pst", ITYPE_OUTLOOK),
        new EMTypeEntry( TYPE_EML, "eml", ITYPE_EML),
        new EMTypeEntry( TYPE_TBIRD, "mbox", ITYPE_TBIRD),
        new EMTypeEntry( TYPE_UNKNOWN, "dat", ITYPE_UNKNOWN)
    };

    public static final String get_suffix_from_type(String type)
    {
        for (int i = 0; i < typelist.length; i++)
        {
            EMTypeEntry elem = typelist[i];
            if (type.compareTo(elem.type) == 0)
                return elem.suffix;
        }
        return "dat";
    }
    public static final String get_type_from_name(String name)
    {
        String suffix = name;
        int idx = name.lastIndexOf('.');
        if (idx >= 0 && idx < name.length())
            suffix = name.substring(idx + 1);

        for (int i = 0; i < typelist.length; i++)
        {
            EMTypeEntry elem = typelist[i];
            if (suffix.compareTo(elem.suffix) == 0)
                return elem.type;
        }

        return TYPE_UNKNOWN;
    }
    public static final int get_itype_from_name(String name)
    {
        String suffix = name;
        int idx = name.lastIndexOf('.');
        if (idx >= 0 && idx < name.length())
            suffix = name.substring(idx + 1);

        for (int i = 0; i < typelist.length; i++)
        {
            EMTypeEntry elem = typelist[i];
            if (suffix.compareTo(elem.suffix) == 0)
                return elem.itype;
        }

        return ITYPE_UNKNOWN;
    }

}
