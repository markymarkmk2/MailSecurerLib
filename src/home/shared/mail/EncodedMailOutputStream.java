/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package home.shared.mail;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author mw
 */
public class EncodedMailOutputStream extends FilterOutputStream
{
    boolean is_in_write;

    public EncodedMailOutputStream( OutputStream is )
    {
        super(is);
    }

    @Override
    public void write( int b ) throws IOException
    {
        // CHECK AGAINST CALL FROM OURSELF
        if (is_in_write)
            super.write( b);
        else
            super.write( ~b);
    }

    @Override
    public void write( byte[] b ) throws IOException
    {
        write(b, 0, b.length);
    }

    @Override
    public void write( byte[] b, int off, int len ) throws IOException
    {
        for (int i = off; i < len; i++)
        {
            byte ch = b[i];
            b[i] = (byte)(~ch & 0xFF);
        }
        is_in_write = true;
        super.write(b, off, len);
        is_in_write = false;
    }
    
}
