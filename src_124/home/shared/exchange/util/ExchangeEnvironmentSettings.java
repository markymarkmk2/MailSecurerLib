/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package home.shared.exchange.util;

import com.microsoft.schemas.exchange.services._2006.types.*;
import javax.xml.ws.Holder;

/**
 * A utility class to make forming ExchangeServicePortType requests easier.
 *
 * @author Reid Miller
 */
public class ExchangeEnvironmentSettings
{
    private static String[] cultures = {"de-DE", "en-US"};

    public static ExchangeVersionType[] get_exch_versions()
    {
        return ExchangeVersionType.values();
    }
    public static String[] get_cultures()
    {
        return cultures;
    }

    String mailboxCulture; // = "de-DE";
    ExchangeVersionType ex_version; // = ExchangeVersionType.EXCHANGE_2007_SP_1;

    public ExchangeEnvironmentSettings( String mailboxCulture, ExchangeVersionType ex_version )
    {
        this.mailboxCulture = mailboxCulture;
        this.ex_version = ex_version;
    }

        
    /**
     * Get the mailbox culture "en-US" to keep things simple for now.
     *
     * @return String
     */
    public String getMailboxCulture()
    {
        return mailboxCulture;
    }

    /**
     * A Holder that holds the server's response for what server version
     * identifies iteself as.
     *
     * @return Holder<ServerVersionInfo>
     */
    public final Holder<ServerVersionInfo> getServerVersionInfoHolder()
    {
        final ServerVersionInfo serverVersionInfo = new ServerVersionInfo();
        final Holder<ServerVersionInfo> serverVersion = new Holder<ServerVersionInfo>(serverVersionInfo);
        return serverVersion;
    }

    /**
     * Creates a RequestServerVersion object that states we're looking to speak
     * with an Exchange 2007 Service Pack 1 server.
     *
     * @return RequestServerVersion
     */
    public final RequestServerVersion getRequestServerVersion()
    {
        final RequestServerVersion requestVersion = new RequestServerVersion();
        requestVersion.setVersion(ExchangeVersionType.EXCHANGE_2007_SP_1);
        return requestVersion;
    }
}
