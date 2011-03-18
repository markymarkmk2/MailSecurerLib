/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package home.shared.mail;

/**
 *
 * @author Administrator
 */
public class MailAttribute 
{
    private String type;
    private String name;
    private String value;

    public MailAttribute( String type, String name, String value )
    {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public boolean isName(String n)
    {
        return name.equals(n);
    }

    public boolean isType(String t)
    {
        return type.equals(t);
    }

    public boolean isValue(String v)
    {
        return value.equals(v);
    }

    public String getValue()
    {
        return value;
    }

    String getName()
    {
        return name;
    }

    @Override
    public boolean equals( Object obj )
    {
        if (obj instanceof MailAttribute)
        {
            MailAttribute ma = (MailAttribute) obj;
            if (ma.type.equals(type) && ma.name.equals(name) && ma.value.equals(value))
                return true;
            return false;
        }
        else
            return super.equals(obj);
    }



}