/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package home.shared.mail;

import home.shared.CS_Constants;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
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
        msg = new MimeMessage(session);
        email_list = new ArrayList<RFCMailAddress>();
    }

    public RFCMimeMail( java.util.Properties props )
    {
        session = Session.getDefaultInstance(props, null);
        msg = new MimeMessage(session);
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

        msg = new MimeMessage(session, bis);
        email_list = parse_email_list(msg);

        bis.close();
    }

    public void parse( InputStream bis ) throws FileNotFoundException, MessagingException, IOException
    {
        msg = new MimeMessage(session, bis);
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
        if (p.isMimeType("multipart/alternative"))
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
            if (p instanceof Multipart && p.isMimeType("multipart/alternative"))
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
                    System.err.println("GOT No attachement contentTye : " + p.getContentType());
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
                    System.err.println("GOT No attachement contentTye : " + p.getContentType());
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

            Part p = text_part;
            if (p == null)
            {
                p = html_part;
            }
            txt_msg = null;
            if (p != null)
            {
                txt_msg = p.getContent().toString();
            }
        }
        catch (IOException iOException)
        {
        }
        catch (MessagingException messagingException)
        {
        }

        if (txt_msg == null)
        {
            try
            {
                txt_msg = msg.getContent().toString();
            }
            catch (Exception ex)
            {
            }
        }

        return txt_msg;
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

            txt_msg = null;
            if (p != null)
            {
                txt_msg = p.getContent().toString();
            }
        }
        catch (IOException iOException)
        {
        }
        catch (MessagingException messagingException)
        {
        }

        return txt_msg;
    }

    // PARSE EMAIL_LIST FROM MAIL HEADERS
    private ArrayList<RFCMailAddress> parse_email_list( MimeMessage msg )
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
                    
                    if (name.compareToIgnoreCase(CS_Constants.FLD_FROM ) == 0)
                    {
                        list.add(new RFCMailAddress(value, RFCMailAddress.ADR_TYPE.FROM));
                    }
                    else if (name.compareToIgnoreCase(CS_Constants.FLD_TO ) == 0)
                    {
                        list.add(new RFCMailAddress(value, RFCMailAddress.ADR_TYPE.TO));
                    }
                    else if (name.compareToIgnoreCase(CS_Constants.FLD_ENVELOPE_TO ) == 0)
                    {
                        list.add(new RFCMailAddress(value, RFCMailAddress.ADR_TYPE.TO));
                    }
                    else if (name.compareToIgnoreCase(CS_Constants.FLD_CC ) == 0)
                    {
                        list.add(new RFCMailAddress(value, RFCMailAddress.ADR_TYPE.CC));
                    }
                    else if (name.compareToIgnoreCase(CS_Constants.FLD_BCC ) == 0)
                    {
                        list.add(new RFCMailAddress(value, RFCMailAddress.ADR_TYPE.BCC));
                    }
                }
            }
        }
        catch (MessagingException messagingException)
        {
        }
        return list;
    }
}
