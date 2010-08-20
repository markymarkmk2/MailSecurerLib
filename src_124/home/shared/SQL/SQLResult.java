/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package home.shared.SQL;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mw
 */
public class SQLResult<T> extends ArrayList<T>
{
    private String qry;
    private Class retClass;
    private long duration;
    private Exception exception;
    private int errCode;
    private String errText;

    private SQLArrayResult res;
    SQLObjectGetter object_getter;


    public SQLResult( SQLObjectGetter object_getter, SQLArrayResult resarr, Class cl )
    {
        duration = 0;
        res = resarr;
        this.object_getter = object_getter;

        init_member_list( cl );
    }

    void init_member_list( Class cl)
    {
        if (res == null)
            return;

        for (int i = 0; i < res.getRows(); i++)
        {
            try
            {
                Constructor<T> con = (Constructor<T>) cl.getConstructor();

                T new_o = con.newInstance();

                new_o = get_object(i, new_o);
                add(new_o);
            }
            catch (InstantiationException ex)
            {
                Logger.getLogger(SQLResult.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (IllegalAccessException ex)
            {
                Logger.getLogger(SQLResult.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (IllegalArgumentException ex)
            {
                Logger.getLogger(SQLResult.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (InvocationTargetException ex)
            {
                Logger.getLogger(SQLResult.class.getName()).log(Level.SEVERE, null, ex);
            }            catch (NoSuchMethodException ex)
            {
                Logger.getLogger(SQLResult.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (SecurityException ex)
            {
                Logger.getLogger(SQLResult.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public int get_row_by_id( int id)
    {
        for (int i = 0; i < size(); i++)
        {
            try
            {
                Object o = get(i);
                Method m = o.getClass().getMethod("getId");
                int m_id = ((Integer) m.invoke(o)).intValue();
                if (id == m_id)
                {
                    return i;
                }
            }
            catch (IllegalAccessException ex)
            {
                Logger.getLogger(SQLResult.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (IllegalArgumentException ex)
            {
                Logger.getLogger(SQLResult.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (InvocationTargetException ex)
            {
                Logger.getLogger(SQLResult.class.getName()).log(Level.SEVERE, null, ex);
            }            catch (NoSuchMethodException ex)
            {
                Logger.getLogger(SQLResult.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (SecurityException ex)
            {
                Logger.getLogger(SQLResult.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return -1;
    }

    public T get_obj_by_id( int id)
    {
        int row = get_row_by_id( id );
        if (row == -1)
            return null;

        return get(row);
    }

    /**
     * @return the qry
     */
    public String getQry()
    {
        return qry;
    }

    /**
     * @param qry the qry to set
     */
    public void setQry( String qry )
    {
        this.qry = qry;
    }

    /**
     * @return the retClass
     */
    public Class getRetClass()
    {
        return retClass;
    }

    /**
     * @param retClass the retClass to set
     */
    public void setRetClass( Class retClass )
    {
        this.retClass = retClass;
    }

    /**
     * @return the duration
     */
    public long getDuration()
    {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration( long duration )
    {
        this.duration = duration;
    }

    /**
     * @return the ex
     */
    public Exception getEx()
    {
        return exception;
    }

    /**
     * @param ex the ex to set
     */
    public void setEx( Exception ex )
    {
        this.exception = ex;
    }



    /**
     * @return the errCode
     */
    public int getErrCode()
    {
        return errCode;
    }

    /**
     * @param errCode the errCode to set
     */
    public void setErrCode( int errCode )
    {
        this.errCode = errCode;
    }

    /**
     * @return the errText
     */
    public String getErrText()
    {
        return errText;
    }

    /**
     * @param errText the errText to set
     */
    public void setErrText( String errText )
    {
        this.errText = errText;
    }
    String  get_name_from_hibernate_class( String rec_name )
    {

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < rec_name.length(); i++)
        {
            char ch = new Character( rec_name.charAt(i) );

            if (i == 0)
            {
                ch = Character.toLowerCase(ch);
            }
            else
            {
                if (Character.isUpperCase(ch))
                {
                    sb.append('_');
                    ch = Character.toLowerCase(ch);
                }
            }
            sb.append(ch);
        }


        return sb.toString();
    }

    private T get_object( int row, Object o)
    {
        Method[] ms = o.getClass().getDeclaredMethods();

        try
        {
            for (int i = 0; i < ms.length; i++)
            {
                Method method = ms[i];
                String ret_type = method.getReturnType().getName();
                // SKIP GETTERS
                if (method.getName().substring(0, 3).compareTo("set") != 0)
                {
                    continue;


                }
                String type = method.getParameterTypes()[0].getName();
                String field_name = get_name_from_hibernate_class( method.getName().substring(3) );

                if (type.compareTo("java.lang.String") == 0)
                {
                    String val = res.getString(row, field_name);
                    method.invoke(o, val);
                }
                else if (type.compareTo("java.lang.Integer") == 0)
                {
                    Integer val = new Integer( res.getInt(row, field_name) );
                    method.invoke(o, val);
                }
                else if (type.compareTo("int") == 0)
                {
                    int val = res.getInt(row, field_name);
                    method.invoke(o, val);
                }
                else if (ret_type.compareTo("long") == 0)
                {
                    long val = res.getLong(row, field_name);
                    method.invoke(o, val);
                }
                else if (object_getter != null && type.compareTo("home.shared.hibernate.Mandant") == 0)
                {
                    int id = res.getInt(row, "mid");
                    method.invoke(o, object_getter.get_mandant( id ));
                }
                else if (object_getter != null && type.compareTo("home.shared.hibernate.DiskArchive") == 0)
                {
                    int id = res.getInt(row, "da_id");
                    method.invoke(o, object_getter.get_disk_archive( id ));
                }
                else if (object_getter != null && type.compareTo("home.shared.hibernate.AccountConnector") == 0)
                {
                    int id = res.getInt(row, "ac_id");
                    method.invoke(o, object_getter.get_account_connector( id ));
                }
                else if (object_getter != null && type.compareTo("home.shared.hibernate.Role") == 0)
                {
                    int id = res.getInt(row, "ro_id");
                    method.invoke(o, object_getter.get_role( id ));
                }
                else if (!type.contains("java.util.Set"))
                {
                    Logger.getLogger(SQLResult.class.getName()).log(Level.SEVERE, "unresolved Object type " + type + " in SQLResult.get_object()");
                }
            }
        }
        catch (IllegalAccessException illegalAccessException)
        {
            illegalAccessException.printStackTrace();
            return null;
        }
        catch (IllegalArgumentException illegalArgumentException)
        {
            illegalArgumentException.printStackTrace();
            return null;
        }
        catch (InvocationTargetException invocationTargetException)
        {
            invocationTargetException.printStackTrace();
            return null;
        }

        return (T)o;
    }
    

    public static String html_to_native( String txt )
    {
        if (txt == null)
            return null;
        
        if (txt.indexOf('&') == -1)
            return txt;

        txt = txt.replaceAll("&amp;", "&");
        txt = txt.replaceAll("&auml;", "ä");
        txt = txt.replaceAll("&ouml;", "ö");
        txt = txt.replaceAll("&uuml;", "ü");
        txt = txt.replaceAll("&Auml;", "Ä");
        txt = txt.replaceAll("&Ouml;", "Ö");
        txt = txt.replaceAll("&Uuml;", "Ü");
        txt = txt.replaceAll("&quot;", "\"");
        txt = txt.replaceAll("&lt;", "<");
        txt = txt.replaceAll("&gt;", ">");
        txt = txt.replaceAll("&ccedil;", "c");
        txt = txt.replaceAll("&eacute;", "é");
        txt = txt.replaceAll("&egrave;", "è");
        txt = txt.replaceAll("&aacute;", "á");
        txt = txt.replaceAll("&agrave;", "à");
        txt = txt.replaceAll("&ugrave;", "ù");
        txt = txt.replaceAll("&Egrave;", "È");
        txt = txt.replaceAll("&Agrave;", "À");
        txt = txt.replaceAll("&acute;", "á");
        txt = txt.replaceAll("&szlig;", "ß");
        txt = txt.replaceAll("&euml;", "e");
        txt = txt.replaceAll("&atilde;", "a" );
        txt = txt.replaceAll("&aring;", "a" );


        return txt;
    }
    public int getRows()
    {
        if (res == null)
            return 0;

        return res.getRows();
    }
    public String getRawString(int row, String col )
    {
        String r = res.getString( row, col );
        return r;
    }
    public String getString(int row, String col )
    {
        String r = res.getString( row, col );
        return html_to_native(r);
    }
    public int getInt(int row, String col )
    {
        int r = res.getInt( row, col );
        return r;
    }
    public long getLong(int row, String col )
    {
        long r = res.getLong( row, col );
        return r;
    }
    public boolean getBooleanValue(int row, String col )
    {
        boolean  r = res.getBooleanValue( row, col );
        return r;
    }

    public void set_array( SQLArrayResult resarr )
    {
        res = resarr;
    }
    public int getFieldNumer( String fname )
    {
        return res.getField(fname);
    }
    
}
