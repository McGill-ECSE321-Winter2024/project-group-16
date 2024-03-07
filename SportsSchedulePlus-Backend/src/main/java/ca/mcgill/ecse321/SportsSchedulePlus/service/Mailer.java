package ca.mcgill.ecse321.SportsSchedulePlus.service;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jodd.mail.Email;
import jodd.mail.MailServer;
import jodd.mail.RFC2822AddressParser;
import jodd.mail.SendMailSession;
import jodd.mail.SmtpServer;

/**
 * Mailer is a class to send emails. It has an Email object that is
 * set with the sendEmail method. It requires a MailConfigBean object to hold
 * the credentials of the sending email.
 *
 * @author Dania Bouhmidi
 */
public class Mailer {

    private final static Logger LOG = LoggerFactory.getLogger(Mailer.class);
    private Email email;
    private final MailConfigBean sendBean;

    /**
     * Constructor : sets the MailConfigBean required to hold the sending
     * account's credentials.
     *
     * @param sendBean
     */
    public Mailer(final MailConfigBean sendBean) {
        this.sendBean = sendBean;

    }

    /**
     * Sets the email's content. The email can contain a subject, html text and plain text messages. If any of the
     * parameters of this method are null, the email's content for that specific
     * parameter will not be set.
     *
     * @param subject
     * @param attachments (can be null)
     * @param embeddedAttachments (can be null)
     * @param htmlText (can be null)
     * @param textMessage
     * @throws IOException
     */
    private void setContent(String subject,
            String htmlText, String textMessage) throws IOException {
        if (subject == null) {
            subject = "";
        }
        this.email.subject(subject);
        if (textMessage == null) {
            textMessage = "";
        }
        this.email.textMessage(textMessage);
        if (htmlText != null) {
            this.email.htmlMessage(htmlText);
        }

    }

    /**
     * Sends an email, the email can  have a subject, a textMessage, html text , to recipients.I
     * @param subject
     * @param textMessage
     * @param toField

     */
    public Email sendEmail(
            final String subject, final String textMessage, final String htmlText, String toField) throws IOException {
        if (sendBean != null) {
            if (checkEmail(sendBean.getUserEmailAddress())) {
                LOG.info("send bean valid");
                // Create am SMTP server object
                SmtpServer smtpServer = MailServer.create()
                        .ssl(true)
                        .host(sendBean.getSmtpUrl())
                        .auth(sendBean.getUserEmailAddress(), sendBean.getEmailPassword())
                        .buildSmtpMailServer();

                this.email = Email.create().from(sendBean.getUserEmailAddress());
                if (checkEmail(toField)) {

                    this.email.to(toField);
                }

                this.setContent(subject, htmlText, textMessage);
                try ( // A session is the object responsible for communicating with the server
                        SendMailSession session = smtpServer.createSession()) {
                    // Like a file we open the session, send the message and close the
                    // session
                    session.open();
                    session.sendMail(email);
                    LOG.info("Email sent");
                }
            } else {
                LOG.warn("Unable to send email because the send address : " + sendBean.getUserEmailAddress() + "is invalid");
            }
        }

        return this.email;
    }

    /**
     * Use the RFC2822AddressParser to validate that the email string could be a
     * valid address
     *
     * @param address
     * @return true is OK, false if not
     */
    public static boolean checkEmail(String address) {
        return RFC2822AddressParser.STRICT.parseToEmailAddress(address) != null;
    }

}
