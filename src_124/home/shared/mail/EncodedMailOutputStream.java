/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package home.shared.mail;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author mw
 */
public class EncodedMailOutputStream extends OutputStream
{
    OutputStream os;

    public EncodedMailOutputStream( OutputStream os )
    {
        this.os = os;
    }

    @Override
    public void write( int b ) throws IOException
    {
        // CHECK AGAINST CALL FROM OURSELF
        os.write( ~b);
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
        os.write(b, off, len);
    }

    @Override
    public void close() throws IOException
    {
        os.close();
    }

    @Override
    public void flush() throws IOException
    {
        os.flush();
    }
    


    
}
