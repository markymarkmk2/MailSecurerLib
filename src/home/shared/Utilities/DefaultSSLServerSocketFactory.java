/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package home.shared.Utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

/**
 *
 * @author mw
 */
public class DefaultSSLServerSocketFactory extends SSLServerSocketFactory
{

    SSLServerSocketFactory factory;

    public DefaultSSLServerSocketFactory()
    {
        try
        {
            SSLContext sslcontext = SSLContext.getInstance("TLS", "SunJSSE");

            sslcontext.init(null, new TrustManager[]
                    {
                        new DefaultTrustManager()
                    }, null);
            factory = sslcontext.getServerSocketFactory();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public String[] getDefaultCipherSuites()
    {
        return factory.getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites()
    {
        return factory.getSupportedCipherSuites();
    }


    @Override
    public ServerSocket createServerSocket( int port ) throws IOException
    {
        return factory.createServerSocket(port);
    }

    @Override
    public ServerSocket createServerSocket( int port, int backlog ) throws IOException
    {
        return factory.createServerSocket(port, backlog);
    }

    @Override
    public ServerSocket createServerSocket( int port, int backlog, InetAddress ifAddress ) throws IOException
    {
        return factory.createServerSocket(port, backlog, ifAddress);
    }

}
