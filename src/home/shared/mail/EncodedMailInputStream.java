/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package home.shared.mail;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author mw
 */
public class EncodedMailInputStream extends FilterInputStream
{

    public EncodedMailInputStream( InputStream is )
    {
        super(is);
    }

    @Override
    public int read() throws IOException
    {
        int ch = super.read();
        ch = ~ch;
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
        int ret = super.read(b, off, len);
        if (ret <= 0)
            return ret;

        for (int i = 0; i < ret; i++)
        {
            byte ch = b[i];
            b[i] = (byte)(~ch & 0xFF);
        }
        return ret;
    }
}
