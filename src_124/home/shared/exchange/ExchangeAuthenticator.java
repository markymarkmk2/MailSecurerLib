/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package home.shared.exchange;

import com.microsoft.schemas.exchange.services._2006.messages.ExchangeServicePortType;
import com.microsoft.schemas.exchange.services._2006.messages.ExchangeWebService;
import com.sun.xml.ws.developer.JAXWSProperties;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.security.Security;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.handler.MessageContext;

class DefaultTrueHostnameVerifier implements  HostnameVerifier
{

    @Override
    public boolean verify( String hostname, SSLSession session )
    {
       return true;
    }
}

class ExchangeWSDLAuthenticator extends Authenticator
{
        private String username, password;

        public ExchangeWSDLAuthenticator(String user, String pass)
        {
            username = user;
            password = pass;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication()
        {
            return new PasswordAuthentication(username, password.toCharArray());
        }
}

class AllHostnameVerifier implements HostnameVerifier
{
    @Override
    public boolean verify(String urlHostName, SSLSession session)
    {
        System.out.println("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
        return true;
    }
};
/**
 * This class manages web services authentication requests to the Exchange
 * server.
 *
 * @author Reid Miller
 */
public class ExchangeAuthenticator
{

    /**
     * Obtains an authenticated ExchangeServicePortType with given credentials.
     *
     * See https://jax-ws.dev.java.net/faq/index.html#auth
     *
     * @param username
     * @param password
     * @param domain
     * @param wsdlURL
     * @return ExchangeServicePortType
     * @throws MalformedURLException
     */
    public ExchangeServicePortType getExchangeServicePort( String username, String password, String domain, String endpoint, URL wsdlURL ) throws MalformedURLException
    {
        // Concatinate our domain and username for the UID needed in authentication.
        String uid = domain + "\\" + username;

        WebServiceClient ann = ExchangeWebService.class.getAnnotation(WebServiceClient.class);

        // Create an ExchangeWebService object that uses the supplied WSDL file, wsdlURL with Annotations.
        ExchangeWebService exchangeWebService = new ExchangeWebService(wsdlURL, new QName(ann.targetNamespace(), ann.name()) );

        ExchangeServicePortType port = exchangeWebService.getExchangeWebPort();

        BindingProvider bp = (BindingProvider)port;

        // Supply your username and password when the ExchangeServicePortType is used for binding in the SOAP request.
        Map m = bp.getRequestContext();
        bp.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, uid);
        bp.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, password);
        bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);

        Map<String,List<String>> map = new HashMap<String,List<String>>();
        map.put("Content-Type", Collections.singletonList("text/xml; charset=\"utf-8\"") );

        bp.getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, map);
        bp.getRequestContext().put(JAXWSProperties.HOSTNAME_VERIFIER, new DefaultTrueHostnameVerifier());


        return port;
    }

    public static ExchangeServicePortType open_exchange_port( String user, String pwd, String domain, String ip ) throws MalformedURLException
    {
        Authenticator.setDefault(new ExchangeWSDLAuthenticator( domain + "\\" + user, pwd));

        // USE LOCAL REPAIRED WSDL DEFINITION, M-SOFT FUCKED IT UP
        URL baseUrl = wsdl.ews.ServiceNub.class.getResource(".");
        URL wsdlURL = new URL(baseUrl, "Services.wsdl");

        // SET WSDL ENDPOINT FOR COMM
        String endpoint = "https://" + ip + "/EWS/exchange.asmx";

        // OPEN PORT
        ExchangeAuthenticator exchangeAuthenticator = new ExchangeAuthenticator();
        ExchangeServicePortType port = exchangeAuthenticator.getExchangeServicePort(user, pwd, domain, endpoint, wsdlURL);

        return port;
    }

    public static void reduce_ssl_security()
    {
        // LOOSEN SECURITY FULL EXTENT, HOSTNAME DOESNT CARE, CERTIFICATE NEITHER
        Security.setProperty( "ssl.SocketFactory.provider", "dimm.home.exchange.DefaultExchangeSSLSocketFactory");
        Security.addProvider( new com.sun.net.ssl.internal.ssl.Provider());
        HttpsURLConnection.setDefaultHostnameVerifier(new AllHostnameVerifier());
    }
}
