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
    byte[] buffer;

    public EncodedMailOutputStream( OutputStream os )
    {
        this.os = os;
        buffer = new byte[8192];
    }

    @Override
    public void write( int b ) throws IOException
    {
        // CHECK AGAINST CALL FROM OURSELF
        b = (byte)(~b & 0xFF);
        os.write( b);
    }

    @Override
    public void write( byte[] b ) throws IOException
    {
        write(b, 0, b.length);
    }

    @Override
    public void write( byte[] b, int off, int len ) throws IOException
    {
        // WE CANNOT CHANGE AN EXTERNAL BUFFER -> USE OUR OWN
        if (b.length > buffer.length)
        {
             buffer = new byte[b.length];
        }

        for (int i = off; i < len; i++)
        {
            byte ch = b[i];
            buffer[i] = (byte)(~ch & 0xFF);
        }
        os.write(buffer, off, len);
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
