/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package home.shared;

/**
 *
 * @author mw
 */
public class AccountConnectorTypeEntry
{

    String type;
    String name;

    AccountConnectorTypeEntry( String t, String n )
    {
        type = t;
        name = n;
    }

    @Override
    public String toString()
    {
        return name;
    }
    public String getType()
    {
        return type;
    }
    public String getName()
    {
        return name;
    }

}
