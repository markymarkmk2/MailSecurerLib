/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package home.shared.mail;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author mw
 */
public class EncodedMailInputStream extends InputStream
{
    InputStream is;

    public EncodedMailInputStream( InputStream is )
    {
        this.is = is;

    }

    @Override
    public int read() throws IOException
    {
        int ch = is.read();
        ch = (byte)(~ch & 0xFF);
        return ch;
    }

    @Override
    public int read( byte[] b ) throws IOException
    {
        return read(b, 0, b.length);
    }

    @Override
    public int read( byte[] b, int off, int len ) throws IOException
    {
        int ret = is.read(b, off, len);
        if (ret <= 0)
            return ret;

        for (int i = 0; i < ret; i++)
        {
            byte ch = b[i];
            b[i] = (byte)(~ch & 0xFF);
        }
        return ret;
    }

    @Override
    public void close() throws IOException
    {
        is.close();
    }

    @Override
    public int available() throws IOException
    {
        return is.available();
    }

    @Override
    public synchronized void reset() throws IOException
    {
        is.reset();
    }

    @Override
    public long skip( long n ) throws IOException
    {
        return is.skip(n);
    }




}
