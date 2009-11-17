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
    public enum OPERATION
    {
        BEGINS_WITH,
        ENDS_WITH,
        CONTAINS,
        CONTAINS_SUBSTR,
        REGEXP
    }

    private String name;
    private String value;
    private OPERATION operation;

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


    public ExprEntry( ArrayList<LogicEntry> parent_list, String name, String value, OPERATION operation, boolean neg, boolean previous_is_or )
    {
        super(parent_list, neg,  previous_is_or);
        this.name = name;
        this.value = value.toLowerCase();
        this.operation = operation;
    }

    @Override
    public boolean eval(FilterValProvider f_provider)
    {
        boolean ret = false;

        ArrayList<String> val_list = f_provider.get_val_vor_name(name);

        // EVAL ALL RESULT STRINGS
        for (int i = 0; i < val_list.size(); i++)
        {
            String val = val_list.get(i);

            switch( operation )
            {
                case BEGINS_WITH:   ret = val.startsWith(value); break;
                case ENDS_WITH:     ret = val.endsWith(value); break;
                case CONTAINS_SUBSTR:      ret = val.indexOf(value) >= 0; break;
                case CONTAINS:       ret = val.compareTo(value) == 0; break;
                case REGEXP:        ret = val.matches(value); break;
            }

            if (ret)
                break;

        }

        if (isNeg())
            ret = !ret;

        return ret;
    }
}
