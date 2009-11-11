
package home.shared.Utilities;

import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;


public class DefaultTrustManager implements X509TrustManager {

    public void checkClientTrusted(X509Certificate[] cert, String authType) 
    {
        System.out.println("C: trusting " + authType);
	// everything is trusted
    }

    public void checkServerTrusted(X509Certificate[] cert, String authType)
    {
        System.out.println("S: trusting " + authType);
	// everything is trusted
    }

    public X509Certificate[] getAcceptedIssuers()
    {
	return new X509Certificate[0];
    }
}