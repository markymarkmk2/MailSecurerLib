/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package home.shared.SQL;

import java.io.File;

/**
 *
 * @author mw
 */
public class RMXFile
{
    public enum SRC_OS
    {
        WIN,
        OSX,
        SOL,
        LNX
    };

    char path_deli;
    SRC_OS os;
    String path;
    long len;
    long mtime;
    long size;
    boolean is_dir;


    public RMXFile( File f )
    {
        path = f.getAbsolutePath();
        if (System.getProperty("os.name").startsWith("Windows"))
        {
            os = SRC_OS.WIN;
            path_deli = '\\';
        }
        if (System.getProperty("os.name").startsWith("Mac"))
        {
            os = SRC_OS.OSX;
            path_deli = '/';
        }
        if (System.getProperty("os.name").startsWith("Linux"))
        {
            os = SRC_OS.LNX;
            path_deli = '/';
        }
        if (System.getProperty("os.name").startsWith("Sol"))
        {
            os = SRC_OS.SOL;
            path_deli = '/';
        }

        len = f.length();
        mtime = f.lastModified();
        is_dir = f.isDirectory();
    }

    public long length()
    {
        return len;
    }

    public long lastModified()
    {
        return mtime;
    }

    public String getAbsolutePath()
    {
        return path;
    }

    public boolean isDirectory()
    {
        return is_dir;
    }
    public String getName()
    {
        int idx = path.lastIndexOf(path_deli);
        if (idx >= 0)
            return path.substring(idx + 1);
        return path;
    }

    @Override
    public String toString()
    {
        return path;
    }

    public char getPathDeli()
    {
        return path_deli;
    }

    public SRC_OS getOs()
    {
        return os;
    }
    


}
