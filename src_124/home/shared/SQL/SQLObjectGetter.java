/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package home.shared.SQL;

import home.shared.hibernate.AccountConnector;
import home.shared.hibernate.DiskArchive;
import home.shared.hibernate.Mandant;
import home.shared.hibernate.Role;

/**
 *
 * @author mw
 */
public interface SQLObjectGetter
{

     Mandant get_mandant( int id );
     DiskArchive get_disk_archive( int id );
     AccountConnector get_account_connector( int id );
     Role get_role( int id );
}
