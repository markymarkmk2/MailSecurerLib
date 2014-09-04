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
public class RemoveStuffedDotsMailOutputStream extends OutputStream
{
    OutputStream os;
    boolean lastCharCR = false;
    boolean lastCharLF = false;
    boolean lastCharDot = false;
    private static final int CR = '\r';
    private static final int LF = '\n';
    private static final int DOT = '.';

    byte[] buffer;

    private void resetDotDetector()
    {
        lastCharCR = false;
        lastCharLF = false;
        lastCharDot = false;
    }
    public RemoveStuffedDotsMailOutputStream( OutputStream os )
    {
        this.os = os;
        buffer = new byte[8192];
        resetDotDetector();
    }

    private boolean handleSkipDotLogic( int b )
    {       
        if (b == CR)
        {
            resetDotDetector();
            lastCharCR = true;
        }
        else if (b == LF && lastCharCR)
        {
            lastCharLF = true;
        }
        else if(b == DOT && lastCharCR && lastCharLF)
        {
            if (lastCharDot)
            {
                resetDotDetector();
                return true;  // SKIP 2. DOT
            }
            else
            {
                lastCharDot = true;
            }
        }
        else
        {
            resetDotDetector();
        }
        return false;
    }

    @Override
    public void write( int b ) throws IOException
    {
        if (handleSkipDotLogic( b ))
        {
            // Detected 2 Dots in a row on a new line, skip second
            return;
        }
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

        int newLen = 0;
        for (int i = off; i < len; i++)
        {
            if (handleSkipDotLogic( b[i] ))
            {
                // Detected 2 Dots in a row on a new line, skip second
                continue;
            }

            buffer[newLen++] = b[i];
        }

        os.write(buffer, 0, newLen);
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
