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
public class FilterMatcher
{
    ArrayList<LogicEntry> list;

    FilterValProvider f_provider;

    public FilterMatcher( ArrayList<LogicEntry> list, FilterValProvider f_provider )
    {
        this.list = list;
        this.f_provider = f_provider;
    }



    public boolean eval()
    {
        GroupEntry ge = new GroupEntry();
        ge.children = list;
        
        boolean ret = ge.eval( f_provider );

        return ret;
    }


}
