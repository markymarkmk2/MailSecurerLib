/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package home.shared.filter;

import java.util.ArrayList;

/**
 *
 * @author mw
 */
public class ExprEntry extends LogicEntry
{

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName( String name )
    {
        this.name = name;
    }

    /**
     * @return the value
     */
    public String getValue()
    {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue( String value )
    {
        this.value = value;
    }

    /**
     * @return the operation
     */
    public OPERATION getOperation()
    {
        return operation;
    }

    /**
     * @param operation the operation to set
     */
    public void setOperation( OPERATION operation )
    {
        this.operation = operation;
    }

    public enum OPERATION
    {
        BEGINS_WITH,
        ENDS_WITH,
        CONTAINS,
        EXACTLY,
        REGEXP
    }

    public ExprEntry( ArrayList<LogicEntry> parent_list, String name, String value, OPERATION operation, boolean neg, boolean previous_is_or )
    {
        super(parent_list, neg,  previous_is_or);
        this.name = name;
        this.value = value;
        this.operation = operation;
    }
    private String name;
    private String value;
    private OPERATION operation;

    @Override
    public boolean eval()
    {
        return true;
    }
}
