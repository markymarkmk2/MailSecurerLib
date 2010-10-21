package home.shared.exchange;

import home.shared.Utilities.DefaultTrustManager;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

public class DefaultExchangeSSLSocketFactory extends SSLSocketFactory
{

    private SSLSocketFactory factory;

    public DefaultExchangeSSLSocketFactory()
    {
        try
        {

//            System.setProperty("javax.net.debug","ssl");

            SSLContext sslcontext = SSLContext.getInstance("SSL");


            sslcontext.init(null, new TrustManager[]
                    {
                        new DefaultTrustManager()
                    }, null);

            javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory( sslcontext.getSocketFactory() );

            factory = sslcontext.getSocketFactory();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    @Override
    public Socket createSocket() throws IOException
    {
        return factory.createSocket();
    }

    public static SocketFactory getDefault()
    {
        return new DefaultExchangeSSLSocketFactory();
    }

    @Override
    public Socket createSocket( Socket socket, String s, int i, boolean flag )
            throws IOException
    {
        return factory.createSocket(socket, s, i, flag);
    }

    @Override
    public Socket createSocket( InetAddress inaddr, int i,
            InetAddress inaddr1, int j ) throws IOException
    {
        return factory.createSocket(inaddr, i, inaddr1, j);
    }

    @Override
    public Socket createSocket( InetAddress inaddr, int i )
            throws IOException
    {
        return factory.createSocket(inaddr, i);
    }

    @Override
    public Socket createSocket( String s, int i, InetAddress inaddr, int j )
            throws IOException
    {
        return factory.createSocket(s, i, inaddr, j);
    }

    @Override
    public Socket createSocket( String s, int i ) throws IOException
    {
        return factory.createSocket(s, i);
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
}

