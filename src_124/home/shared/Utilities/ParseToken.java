package home.shared.Utilities;

import com.thoughtworks.xstream.XStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.cert.X509Certificate;
import java.util.ArrayList;


public class ParseToken extends Object
{
    String str;
    int err;
    int from_index;
    boolean _is_ordered;
    
    public boolean is_ordered()
    {
        return _is_ordered;
    }
    
    public void set_ordered( boolean f )
    {
        _is_ordered = f;
    }

    public ParseToken()
    {
    }
    
    public ParseToken( String _str)
    {
        from_index = 0;
        err = 0;
        str = _str;
    }
    
    public String GetString(String token)
    {
        err = 0;
        boolean found_escaped_chars = false;
        int pos = str.indexOf( token, from_index );
        
        if ((pos >= 0) && ((pos + token.length()) < str.length()))
        {
            pos += token.length();
            char delim = str.charAt( pos );
            int end = pos;
            if (delim == '\'' || delim == '\"')
            {
                pos++;
                end = pos;
                // FIND END OF 
                while(end != -1)
                {                
                    end = str.indexOf( delim, end );
                    
                    // IS THIS DELIMITER ESCAPED ?
                    if (str.charAt(end-1) != '\\')
                        break;
                    
                    found_escaped_chars = true;
                    // OK, LOOK FOR NEXT ONE
                    end++;
                }
                if (end == -1)
                {
                    err = 2;                
                    return "";
                }
            }
            else
            {
                end = pos;
                while( end < str.length() )
                {
                    char ch = str.charAt( end );
                    if (ch == ' ')
                        break;
                    if (ch == '\n')
                        break;
                    if (ch == '\t')
                        break;
                    end++;
                }
            }
            // SHIFT STARTINDEX IF WE WANT TO SCAN ORDERED
            if (is_ordered())
                from_index = end;
            
            if (found_escaped_chars)
            {
                StringBuffer sb = new StringBuffer(str.substring( pos, end ));
                int l = sb.length();
                for (int i = 0; i < l; i++)
                {
                    if ((sb.charAt(i) == '\\') && sb.charAt(i+1) != '\\')
                    {
                        sb.replace(i, i+1, "");
                        l = sb.length();
                    }
                }
                return sb.toString();
            }
                
            return str.substring( pos, end );
        }
        err = 1;
        return "";
    }
    public String GetStringDelim(String token, char token_delim)
    {
        err = 0;
        boolean found_escaped_chars = false;
        int pos = str.indexOf( token, from_index );
        
        if ((pos >= 0) && ((pos + token.length()) < str.length()))
        {
            pos += token.length();
            while (pos < str.length() && str.charAt( pos ) == token_delim)
                pos++;
            
            char delim = str.charAt( pos );
            int end = pos;
            if (delim == '\'' || delim == '\"')
            {
                pos++;
                end = pos;
                // FIND END OF 
                while(end != -1)
                {                
                    end = str.indexOf( delim, end );
                    
                    // IS THIS DELIMITER ESCAPED ?
                    if (str.charAt(end-1) != '\\')
                        break;
                    
                    found_escaped_chars = true;
                    // OK, LOOK FOR NEXT ONE
                    end++;
                }
                if (end == -1)
                {
                    err = 2;                
                    return "";
                }
            }
            else
            {
                end = pos;
                while( end < str.length() )
                {
                    char ch = str.charAt( end );
                    if (ch == ' ')
                        break;
                    if (ch == '\n')
                        break;
                    if (ch == '\t')
                        break;
                    end++;
                }
            }
            // SHIFT STARTINDEX IF WE WANT TO SCAN ORDERED
            if (is_ordered())
                from_index = end;
            
            if (found_escaped_chars)
            {
                StringBuffer sb = new StringBuffer(str.substring( pos, end ));
                int l = sb.length();
                for (int i = 0; i < l; i++)
                {
                    if ((sb.charAt(i) == '\\') && sb.charAt(i+1) != '\\')
                    {
                        sb.replace(i, i+1, "");
                        l = sb.length();
                    }
                }
                return sb.toString();
            }
                
            return str.substring( pos, end );
        }
        err = 1;
        return "";
    }
    
    
    public Long GetLong(String token)
    {
        String tmp = GetString(token);
        if (tmp.length() == 0)
            return new Long(0);
        
        return new Long(tmp);
    }
    public long GetLongValue(String token)
    {
        String tmp = GetString(token);
        if (tmp.length() == 0)
            return 0;
        
        return Long.parseLong(tmp);
    }
        
    public Double GetDouble(String token)
    {
        String tmp = GetString(token);
        if (tmp.length() == 0)
            return new Double(0);
        
        return new Double(tmp);
    }
    
    public double GetDoubleValue(String token)
    {
        String tmp = GetString(token);
        if (tmp.length() == 0)
            return 0.0;
        
        return Double.parseDouble(tmp);
    }
        
    
        
    public boolean GetBoolean(String token)
    {
        String tmp = GetString(token);
        if (tmp.length() == 0)
            return false;
        
        if (tmp.charAt(0) == '1')
            return true;
        
        return false;
    }
    
    public int getErr()
    {
        return err;
    }
    
    public String get_remaining_string()
    {
        return str.substring(from_index, str.length());
    }
    public String GetCompressedString(String token)
    {
        String cstr = GetString(token);
        return ZipUtilities.uncompress(cstr);
    }

    public Object GetCompressedObject( String token ) {
        String cstr = GetString(token);
        return DeCompressObject(cstr);
    }

    public ArrayList<X509Certificate[]> GetCompressed509CertArrayList( String token ) {
        String cstr = GetString(token);
        return DeCompressX509CertArrayList(cstr);
    }

    public static String BuildCompressedX509CertArrayList( ArrayList<X509Certificate[]> cert_list ) {

        ArrayList<ArrayList<String>> certStringList = new ArrayList<>();

        for (X509Certificate[] x509Certificates : cert_list) {
            ArrayList<String> stringList = new ArrayList<>();
            certStringList.add(stringList);
            for (X509Certificate x509Certificate : x509Certificates) {
                ByteArrayOutputStream byos = new ByteArrayOutputStream();
                try {
                    ObjectOutputStream out = new ObjectOutputStream(byos);
                    out.writeObject(x509Certificate);
                    out.close();
                }
                catch (Exception exception) {
                    System.err.println("Abort in BuildCompressedX509CertArrayList: " + exception.getMessage());
                }
                byte[] d = byos.toByteArray();
                XStream xs = new XStream();
                String xml = xs.toXML(d);
                stringList.add(xml);
//
//            ByteArrayInputStream byis = new ByteArrayInputStream(d);
//            ObjectInputStream in = new ObjectInputStream(byis);
//            Object o = in.readObject();
            /*
             * XStream xs = new XStream();
             * String xml = xs.toXML(x509Certificate);
             * Object o = xs.fromXML(xml);
             *
             */
                //System.out.print(o);
            }
        }
        XStream xs = new XStream();
        return ZipUtilities.compress(xs.toXML(certStringList));
    }

    public static ArrayList<X509Certificate[]> DeCompressX509CertArrayList( String cstr ) {
        String xml = ZipUtilities.uncompress(cstr);
        XStream xs = new XStream();
        @SuppressWarnings("unchecked")
        ArrayList<ArrayList<String>> certStringList = (ArrayList<ArrayList<String>>) xs.fromXML(xml);

        ArrayList<X509Certificate[]> certArrayList = new ArrayList<>();
        for (ArrayList<String> stringList : certStringList) {
            ArrayList<X509Certificate> certList = new ArrayList<>();
            for (String string : stringList) {

                byte[] d = (byte[]) xs.fromXML(string);
                ByteArrayInputStream byis = new ByteArrayInputStream(d);
                try {
                    try (ObjectInputStream in = new ObjectInputStream(byis)) {
                        X509Certificate cert = (X509Certificate) in.readObject();
                        certList.add(cert);
                    }
                }
                catch (Exception exception) {
                    System.err.println("Abort in DeCompressX509CertArrayList: " + exception.getMessage());
                }
            }
            certArrayList.add(certList.toArray(new X509Certificate[0]));

        }
        return certArrayList;
    }

    public static String BuildCompressedObjectString( Object o )
    {
        XStream xs = new XStream();
        String xml = xs.toXML(o);
        String cxml = ZipUtilities.compress(xml);
        return cxml;
    }
    public static Object DeCompressObject( String cstr )
    {
        String xml = ZipUtilities.uncompress(cstr);
        XStream xs = new XStream();
        return xs.fromXML(xml);
    }
    public <T> T GetObject( String token, Class t ) {
        Object o = GetCompressedObject(token);
        if (t.isInstance(o)) {
            return (T) o;
        }
        System.err.println("Wrong class in GetObject, expected: " + t.getName() + " got: " + o.getClass().getName());
        return null;
    }

    public ArrayList<X509Certificate[]> GetX509CertArrayList( String token ) {
        return GetCompressed509CertArrayList(token);
    }


}

