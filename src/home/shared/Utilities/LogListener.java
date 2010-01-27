/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package home.shared.Utilities;

/**
 *
 * @author mw
 */
public interface LogListener
{
    public void err_log( String txt );
    public void err_log( String txt, Exception ex );
    public void warn_log( String txt );
    public void info_log( String txt );
    public void debug_log( String txt );
    public boolean is_debug();


}
