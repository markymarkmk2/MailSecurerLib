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
public class GroupEntry extends LogicEntry
{
    protected ArrayList<LogicEntry> children;

    public GroupEntry(  )
    {
        super(null, false,  true);
        children = new ArrayList<LogicEntry>();
    }
    public GroupEntry( ArrayList<LogicEntry> parent_list, boolean neg, boolean previuos_is_or )
    {
        super(parent_list, neg,  previuos_is_or);
        children = new ArrayList<LogicEntry>();
    }
    @Override
    public boolean eval()
    {
        return true;
    }

    /**
     * @return the children
     */
    public ArrayList<LogicEntry> getChildren()
    {
        return children;
    }

}