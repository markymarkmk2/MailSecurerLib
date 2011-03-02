/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package home.shared.mail;

import home.shared.CS_Constants;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.StringTokenizer;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author mw
 */
public class RFCMimeMail
{

    private MimeMessage msg;
    private Session session;
    ArrayList<RFCMailAddress> email_list;

    public RFCMimeMail()
    {
        java.util.Properties props = new java.util.Properties();
        props.put("mail.smtp.host", "localhost");
        session = Session.getDefaultInstance(props, null);
        msg = new MimeMessage(getSession());
        email_list = new ArrayList<RFCMailAddress>();
    }

    public RFCMimeMail( java.util.Properties props )
    {
        session = Session.getDefaultInstance(props, null);
        msg = new MimeMessage(getSession());
        email_list = new ArrayList<RFCMailAddress>();
    }

    public RFCMimeMail( MimeMessage _msg )
    {
        msg = _msg;
        java.util.Properties props = new java.util.Properties();
        props.put("mail.smtp.host", "localhost");
        session = Session.getDefaultInstance(props, null);
        email_list = parse_email_list(msg);
    }

    public ArrayList<RFCMailAddress> getEmail_list()
    {
        return email_list;
    }
    

    public void create( String from, String to, String[] cc, String subject, String text, File attachment ) throws MessagingException
    {
        email_list.add(new RFCMailAddress(from, RFCMailAddress.ADR_TYPE.FROM));
        email_list.add(new RFCMailAddress(to, RFCMailAddress.ADR_TYPE.TO));


        // Construct the message
        InternetAddress[] ia_from =
        {
            new InternetAddress(from)
        };
        InternetAddress[] ia_to =
        {
            new InternetAddress(to)
        };

        msg.addFrom(ia_from);
        msg.addRecipients(Message.RecipientType.TO, ia_to);

        msg.setSentDate( new Date() );

        if (subject != null)
        {
            msg.setSubject(subject);
        }
        if (cc != null && cc.length > 0)
        {
            InternetAddress[] ia_cc = new InternetAddress[cc.length];
            for (int i = 0; i < cc.length; i++)
            {
                email_list.add(new RFCMailAddress(cc[i], RFCMailAddress.ADR_TYPE.CC));
                ia_cc[i] = new InternetAddress( cc[i] );
            }
            msg.addRecipients(Message.RecipientType.CC, ia_cc);
        }

        // CREATE MULTIPART
        Multipart multipart = new MimeMultipart();

        // CREATE MESSAGE PART
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(text);
        messageBodyPart.setDisposition(MimeBodyPart.INLINE);

        // ADD TO MULTIPART
        multipart.addBodyPart(messageBodyPart);


        if (attachment != null)
        {
            // CREATE ATTACHMENT PART
            MimeBodyPart dataBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(attachment);
            dataBodyPart.setDataHandler(new DataHandler(source));
            dataBodyPart.setFileName(attachment.getName());
            dataBodyPart.setDisposition(MimeBodyPart.ATTACHMENT);
            // ADD TO MULTIPART
            multipart.addBodyPart(dataBodyPart);
        }

        // ADD MULTIPART TO MESSAGE
        msg.setContent(multipart);

    }

    public void parse( RFCGenericMail mail_file ) throws FileNotFoundException, MessagingException, IOException
    {
        InputStream bis = mail_file.open_inputstream();

        msg = new MimeMessage(getSession(),bis);
        bis.close();
        bis = null;

        email_list = parse_email_list(msg);

    }

    public void parse( InputStream bis ) throws FileNotFoundException, MessagingException, IOException
    {
        msg = new MimeMessage(getSession(),bis);
        email_list = parse_email_list(msg);
    }

    public void send() throws MessagingException
    {
        Transport.send(msg);

    }

    /**
     * @return the msg
     */
    public MimeMessage getMsg()
    {
        return msg;
    }
    Part html_part = null;
    Part text_part = null;

    private void check_part_content( Part p ) throws IOException, MessagingException
    {
        // SELECT THE PLAIN PART OF AN ALTERNATIVE MP
        if (p.isMimeType("multipart/*"))
        {
            Multipart mp = (Multipart) p.getContent();
            for (int i = 0; i < mp.getCount(); i++)
            {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain"))
                {
                    text_part = bp;
                }
                if (bp.isMimeType("text/html"))
                {
                    html_part = bp;
                }
                if (bp.isMimeType("multipart/*"))
                {
                    check_part_content( bp );
                }
            }
        }
        else if (p.isMimeType("text/plain"))
        {
            text_part = p;
        }
        else if (p.isMimeType("text/html"))
        {
            html_part = p;
        }
    }

    private void check_mp_content( Multipart mp ) throws MessagingException, IOException
    {

        for (int i = 0; i < mp.getCount(); i++)
        {
            // WE DONT WANT TO LOOK DEEPER INTO MAIL
            if (html_part != null && text_part != null)
            {
                break;
            }

            Part p = mp.getBodyPart(i);
            // WE GO DOWN TO NEXT LEVEL ONLY IF WE HAVE MP/ALTERNATIVE, OTHERWISE WE WOULD DETECT TEXT IN INLINE ATTACHMENTS
            if (p instanceof Multipart && p.isMimeType("multipart/*"))
            {
                check_mp_content((Multipart) p);
            }
            else
            {
                check_part_content(p);
            }
        }
    }

    public int mp_count( Multipart mp, int sum ) throws MessagingException, IOException
    {
        for (int i = 0; i < mp.getCount(); i++)
        {
            Part p = mp.getBodyPart(i);
            if (p instanceof Multipart)
            {
                if (!p.isMimeType("multipart/alternative"))
                {
                    sum = mp_count((Multipart) p, sum);
                }
            }
            else
            {
                String disposition = p.getDisposition();

                if (disposition == null)
                {
                    // VIEW INLINE IMAGES AS ATTACHMENTS
                    if ( p.getFileName() == null)
                        System.err.println("GOT No attachement contentTye : " + p.getContentType());
                    else 
                        sum++;

                }
                else if (disposition.equalsIgnoreCase(Part.INLINE))
                {
                    sum++;
                }
                else if (disposition.equalsIgnoreCase(Part.ATTACHMENT))
                {
                    sum++;
                }
                else
                {
                    System.err.println("we got unknown disposition :" + disposition); //shouldn't happen !
                }
            }
        }
        return sum;
    }

    private Part get_attachment( Multipart mp, int idx ) throws MessagingException, IOException
    {
        for (int i = 0; i < mp.getCount(); i++)
        {
            Part p = mp.getBodyPart(i);
            if (p instanceof Multipart)
            {
                if (!p.isMimeType("multipart/alternative"))
                {
                    Part pp = get_attachment((Multipart) p, idx);
                    if (pp != null)
                    {
                        return pp;
                    }
                }
            }
            else
            {
                String disposition = p.getDisposition();
                if (disposition == null)
                {
                    if ( p.getFileName() == null)
                        System.err.println("GOT No attachement contentTye : " + p.getContentType());
                    else
                    {
                        if (idx == 0)
                        {
                            return p;
                        }
                        idx--;
                    }
                }
                else if (disposition.equalsIgnoreCase(Part.INLINE))
                {
                    if (idx == 0)
                    {
                        return p;
                    }

                    idx--;
                }
                else if (disposition.equalsIgnoreCase(Part.ATTACHMENT))
                {
                    if (idx == 0)
                    {
                        return p;
                    }

                    idx--;
                }
                else
                {
                    System.err.println("we got unknown disposition :" + disposition); //shouldn't happen !
                }
            }
        }
        return null;
    }

    public int get_attachment_cnt()
    {
        try
        {
            Object content = msg.getContent();
            if (content instanceof Multipart)
            {
                return mp_count((Multipart) content, 0);
            }
        }
        catch (IOException iOException)
        {
        }
        catch (MessagingException messagingException)
        {
        }
        return 0;
    }

    public Part get_attachment( int idx )
    {
        try
        {
            Object content = msg.getContent();
            if (content instanceof Multipart)
            {
                return get_attachment((Multipart) content, idx);
            }
        }
        catch (IOException iOException)
        {
        }
        catch (MessagingException messagingException)
        {
        }
        return null;
    }

    byte[] get_byte_content( Part p ) throws IOException, MessagingException
    {
        try
        {
            ByteArrayOutputStream byos = new ByteArrayOutputStream();
            p.writeTo(byos);

            return byos.toByteArray();
        }
        catch (Exception exception)
        {
        }
        return p.getContent().toString().getBytes();
    }
    String get_txt_content( Part p ) throws IOException, MessagingException
    {
        try
        {
            //String cstxt =  new String( p.getContent().toString().getBytes(cs) );
            if ( p.getContent() instanceof String)
                return p.getContent().toString();

           

            String cs = get_charset( p );
            byte[] data = get_byte_content( p );
            String txt_msg = new String( data, cs );
            return txt_msg;
        }
        catch (Exception exception)
        {
        }
        return p.getContent().toString();
    }
    public String get_text_content()
    {
        String txt_msg = null;
        try
        {


            Object content = msg.getContent();
            if (content instanceof Multipart)
            {
                check_mp_content((Multipart) content);
            }
            else if (content instanceof Part)
            {
                Part p = (Part) content;
                check_part_content(p);
            }
            else if (content instanceof String)
            {
                txt_msg = (String) content;
            }

            Part p = text_part;
            if (p == null)
            {
                p = html_part;
            }
            if (p != null)
            {
                txt_msg = get_txt_content( p );
            }
        }
        catch (IOException iOException)
        {
        }
        catch (MessagingException messagingException)
        {
        }

        // NOTHING FOUND? THEN USE COMPLETE MSG
        if (txt_msg == null)
        {
            try
            {
                txt_msg = get_txt_content( msg );
            }
            catch (Exception ex)
            {
            }
        }

        return txt_msg;
    }
    public Part get_text_part()
    {
        Part p = null;
        try
        {
            Object content = msg.getContent();
            if (content instanceof Multipart)
            {
                check_mp_content((Multipart) content);
            }
            else if (content instanceof Part)
            {
                Part _p = (Part) content;
                check_part_content(_p);
            }

            p = text_part;
            if (p == null)
            {
                p = html_part;
            }
        }
        catch (IOException iOException)
        {
        }
        catch (MessagingException messagingException)
        {
        }

        return p;
    }

    public Part get_html_part()
    {
        return html_part;
    }


    public String get_html_content()
    {
        String txt_msg = null;
        try
        {
            Object content = msg.getContent();
            if (content instanceof Multipart)
            {
                check_mp_content((Multipart) content);
            }
            else if (content instanceof Part)
            {
                Part p = (Part) content;
                check_part_content(p);
            }


            Part p = html_part;
            if (p == null && msg.isMimeType("text/html"))
            {
                txt_msg = get_txt_content( msg );
            }
            
            if (p != null)
            {
                txt_msg = get_txt_content( p );
            }
        }
        catch (IOException iOException)
        {
            System.out.println(iOException.getMessage());
        }
        catch (MessagingException messagingException)
        {
            System.out.println(messagingException.getMessage());
        }

        return txt_msg;
    }

    static ArrayList<RFCMailAddress> getRFCMailAddressList( String txt, RFCMailAddress.ADR_TYPE typ)
    {
        ArrayList<RFCMailAddress> list = new ArrayList<RFCMailAddress>();
        String[] args = txt.split(",|;| |<|>|\"|\'|\r|\n|\t");

        for (int i = 0; i < args.length; i++)
        {
            String string = args[i];
            if (string.length() == 0)
                continue;
            
            if (string.indexOf('@') > 0)
            {
                list.add(new RFCMailAddress(string.toLowerCase(), typ));
            }
        }
        return list;
    }
    static void sort_email_list(ArrayList<RFCMailAddress> list )
    {
        Comparator<RFCMailAddress> comp = new Comparator<RFCMailAddress>()
        {

            public int compare( RFCMailAddress o1, RFCMailAddress o2 )
            {
                int t1 = o1.getAdr_type().ordinal();
                int t2 = o2.getAdr_type().ordinal();
                if (t1 != t2)
                    return t1 - t2;

                int dr = o1.get_domain().compareToIgnoreCase(o2.get_domain());
                if (dr != 0)
                    return dr;

                return o1.get_mail().compareToIgnoreCase(o2.get_mail());
            }
        };

        // SORT
        Collections.sort(list, comp);

        // REMOVE DOUBLES        
        for (int i = 0; i < list.size(); i++)
        {
            RFCMailAddress rFCMailAddress = list.get(i);

            for (int j = 0; j < i; j++)
            {
                RFCMailAddress last_rFCMailAddress = list.get(j);
                if (last_rFCMailAddress.get_mail().equals(rFCMailAddress.get_mail()) )
                {
                    list.remove(i);
                    i--;
                    break;
                }
            }
        }
    }

    // PARSE EMAIL_LIST FROM MAIL HEADERS
    public static ArrayList<RFCMailAddress> parse_email_list( MimeMessage msg )
    {        
        ArrayList<RFCMailAddress> list = new ArrayList<RFCMailAddress>();
        try
        {
            Enumeration en = msg.getAllHeaders();
            while (en.hasMoreElements())
            {
                Object h = en.nextElement();
                if (h instanceof Header)
                {
                    Header ih = (Header) h;
                    String name = ih.getName();
                    String value = ih.getValue();
                    ArrayList<RFCMailAddress> local_list = null;
                    
                    if (name.compareToIgnoreCase(CS_Constants.FLD_FROM ) == 0)
                    {
                        local_list = getRFCMailAddressList(value, RFCMailAddress.ADR_TYPE.FROM);
                    }
                    else if (name.compareToIgnoreCase(CS_Constants.FLD_TO ) == 0)
                    {
                        local_list = getRFCMailAddressList(value, RFCMailAddress.ADR_TYPE.TO);
                    }
                    else if (name.compareToIgnoreCase(CS_Constants.FLD_ENVELOPE_TO ) == 0)
                    {
                        local_list = getRFCMailAddressList(value, RFCMailAddress.ADR_TYPE.BCC);
                    }
                    else if (name.compareToIgnoreCase("X-" + CS_Constants.FLD_ENVELOPE_TO ) == 0)
                    {
                        local_list = getRFCMailAddressList(value, RFCMailAddress.ADR_TYPE.BCC);
                    }
                    else if (name.compareToIgnoreCase(CS_Constants.FLD_CC ) == 0)
                    {
                        local_list = getRFCMailAddressList(value, RFCMailAddress.ADR_TYPE.CC);
                    }
                    else if (name.compareToIgnoreCase(CS_Constants.FLD_BCC ) == 0)
                    {
                        local_list = getRFCMailAddressList(value, RFCMailAddress.ADR_TYPE.BCC);
                    }
                    if (local_list != null)
                    {
                        list.addAll(local_list);
                    }
                }
            }
            sort_email_list(list);
        }
        catch (MessagingException messagingException)
        {
        }
        return list;
    }

    /**
     * @return the session
     */
    public final Session getSession()
    {
        return session;
    }

    public boolean contains_email( String string )
    {
        for (int i = 0; i < email_list.size(); i++)
        {
            RFCMailAddress rFCMailAddress = email_list.get(i);
            if (rFCMailAddress.get_mail().equalsIgnoreCase(string))
                return true;
        }
        return false;
    }

    static public String get_charset( Part p )
    {
        if (p == null)
            return null;

        String mt;
        try
        {
            mt = p.getContentType();
        }
        catch (MessagingException ex)
        {
            return null;
        }
        int atr_idx = mt.indexOf(';');
        if (atr_idx == -1)
            atr_idx = mt.indexOf('\n');
        if (atr_idx == -1)
            return "";


        String attr = mt.substring(atr_idx + 1);

        String delim = "[/;\"=\n\r\t ]";
        StringTokenizer st = new StringTokenizer(attr, delim);
        String name = st.nextToken();
        try
        {
            if (name.compareToIgnoreCase("charset") == 0)
            {
                String eq = st.nextToken("\"\n\r; ");
                String val = eq;
                if (val.length() > 1 && val.charAt(0) == '=')
                    return javax.mail.internet.MimeUtility.javaCharset(val.substring(1));

                if (st.hasMoreTokens())
                    val = st.nextToken("\"\n\r");

                return javax.mail.internet.MimeUtility.javaCharset(val);
            }
        }
        catch (Exception e)
        {
            System.out.println("Invalid Charset: " + mt);
        }
        return "UTF-8";
    }

}
