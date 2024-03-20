package ca.mcgill.ecse321.mailer;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import jodd.mail.Email;
import jodd.mail.EmailAddress;
import jodd.mail.EmailMessage;
import jodd.net.MimeTypes;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import ca.mcgill.ecse321.SportsSchedulePlus.beans.MailConfigBean;
import ca.mcgill.ecse321.SportsSchedulePlus.service.mailerservice.Mailer;

/**
 * Test class for the Mailer class. 
 * Tests whether the sendEmail method handles messages, html text and recipients properly.
 *
 * @author Dania Bouhmidi
 */

public class MailerTests {

    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(MailerTests.class);

    private static MailConfigBean sendConfigBean;
    private static Mailer mailer;
   
    @BeforeAll
    /**
     * Initializes the sendConfigBean object. 
     */
    public static void init() {
        sendConfigBean = new MailConfigBean("imap.gmail.com", "smtp.gmail.com", "sports.schedule.plus@gmail.com", "aqlq ldup ymfh eejb");
        mailer = new Mailer(sendConfigBean);
    }

    @Test
    /**
     * Test for sendEmail with null values for all parameters except the toField
     * List.
     *
     * @throws IOException
     */
    public void sendEmailNullValuesExceptToFieldTest() throws IOException  {
        // Arrange
        String toField = "haku.xyz@gmail.com";
        // Act
        Email sentEmail = mailer.sendEmail(null, null, null, toField);

        pause();

        //Assert
        assertNull(sentEmail);
    }

    @Test
    /**
     * Test for sendEmail with a null send MailConfigBean.
     *
     * @throws IOException
     */
    public void sendEmailNullSendBeanTest() throws IOException {
        // Arrange
        mailer = new Mailer(null);
     
        String toField = "haku.xyz@gmail.com";
        // Act
        Email sentEmail = mailer.sendEmail(null, "Hi !", null, toField);

        pause();

        // Assert
        assertNull(sentEmail);
    }

    @Test
    /**
     * Test for an email's subject text. Verifies that the subject's text that
     * was sent as a parameter to the sendEmail method is the same as the one
     * retrieved from the sent Email object.
     *
     * @throws IOException
     */
    public void sendEmailSubjectTextTest() throws IOException {
        // Arrange
        String toField = "haku.xyz@gmail.com";

        // Act
        Email sentEmail = mailer.sendEmail(
                "Test Message : To Recipients",null, "Hello this is a test message", toField);

        pause();

        // Assert
        assertEquals("Test Message : To Recipients", sentEmail.subject());
    }

    @Test
    /**
     * Test for an email sent with plain text. Verifies that the text sent as a
     * parameter is the same as the text message retrieved from the email.
     *
     * @throws IOException
     */
    public void sendEmailMessageTextTest() throws IOException {
        // Arrange

        String toField = "haku.xyz@gmail.com";

        // Act
        Email sentEmail = mailer.sendEmail(
                "Test Message", "Hello this is a test message", null, toField);

        pause();

        // Assert
        EmailMessage textMessage = null;
        for (EmailMessage message : sentEmail.messages()) {
            if (message.getMimeType().equals(MimeTypes.MIME_TEXT_PLAIN)) {
                textMessage = message;
                LOG.info(message.getContent());
            }
        }
        assertEquals("Hello this is a test message", textMessage.getContent());
    }

    @Test
    /**
     * Test for sendEmail with the to (recipients) field. Verifies that the to
     * address was properly set.
     *
     * @throws IOException
     */
    public void sendEmailToRecipientsTest() throws IOException {
        // Arrange
        String toField = "haku.xyz@gmail.com";

        // Act
        Email sentEmail = mailer.sendEmail("Test Message : To Recipients", "Hello this is a test message", null, toField);

        pause();
        String actualAddress = convertEmailAddressesToStrings(sentEmail.to())[0];
        String expectedAddress = toField;
    
        // Assert
        assertEquals(actualAddress, expectedAddress);

    }
    

    /**
     * Test for sendEmail without recipients.
     *  Verifies that an IllegalArgumentException is thrown.
     *
     * @throws IOException
     */
    public void sendEmailNoRecipientsTest() throws IOException {
        // Assert
        assertThrows(IllegalArgumentException.class, () -> {
        //Act
        mailer.sendEmail("Test Message : No Recipients", "Hello, this is a test message", null, null);
        });
        fail("Did not throw IllegalArgumentException");
    }

    @Test
    /**
     * Test for sendEmail with htmlText.
     *
     * @throws IOException
     */
    public void sendEmailWithHtmlTextTest() throws IOException {
        // Arrange
        String htmlTextToSet = "<html><META http-equiv=Content-Type "
                + "content=\"text/html; charset=utf-8\">"
                + "<body><h1>Testing sendEmail. "
                + "</h1>"
                + "<h2>Html test text.</h2></body></html>";

        String textMessage = "This is a test for the setContent method."
                + "(testing html text.)";
        
        String toField = "haku.xyz@gmail.com";

        // Act
        Email sentEmail = mailer.sendEmail(
                "Test Message : Html text.", textMessage, htmlTextToSet, toField);
        pause();
        String actualHtmlMessage = "";
        for (EmailMessage message : sentEmail.messages()) {
            if (message.getMimeType().equals(MimeTypes.MIME_TEXT_HTML)) {
                actualHtmlMessage = message.getContent();
                LOG.info(message.getContent());
            }
        }

        // Assert
        assertEquals(htmlTextToSet, actualHtmlMessage);
    }



    /**
     * Test for sendEmail with an invalid sending email address.Verifies that an
     * InvalidEmailAddressException is thrown.
     *
     * @throws IOException
     */
    public void sendEmailFromInvalidSendEmailAdressTest() throws IOException {

        // Arrange
        MailConfigBean invalidSendConfigBean
                = new MailConfigBean("imap.gmail.com", "smtp.gmail.com", "   xyzy12345.this.email.does.not.exist.either.@gmail.com", "908123454yhfgvbfd");

        String toField = "haku.xyz@gmail.com";
        mailer = new Mailer(invalidSendConfigBean);
        
        // Assert

        assertThrows(IllegalArgumentException.class, () -> {
        // Act
           mailer.sendEmail("Test for the sendEmail method",
           "Email addresses do not exist.", null, toField);
        });
        fail("Did not throw InvalidEmailException");

    }

    /**
     * Test for sendEmail with a sending email address with an invalid
     * password.
     * Verifies that a MailException is thrown.
     *
     * @throws IOException
     */
    public void sendEmailWithInvalidFromEmailPasswordTest() throws IOException {
        // Arrange
        MailConfigBean invalidSendConfigBean
                = new MailConfigBean("imap.gmail.com", "smtp.gmail.com", "moonchild.zyx@gmail.com", "908123454yhfgvbfdinvalid");

        String toField = "haku.xyz@gmail.com";
        mailer = new Mailer(invalidSendConfigBean);
        
        // Assert
        assertThrows(IllegalArgumentException.class, () -> {
        //Act
        mailer.sendEmail("Test for the sendEmail method",
        "Email password is invalid.", null, toField);
        });
    }

    /**
     * Test for sendEmail with one invalid to field email address.
     * Verifies that an exception is thrown.
     *
     * @throws IOException
     */
    @Test
    public void sendEmailWithInvalidToFieldEmailAdressTest() throws IOException {
        // Arrange
        String toField = "this.email.does.not.exist";
        // Assert
        assertThrows(IllegalArgumentException.class, () -> {
        // Act
        mailer.sendEmail("Test for the sendEmail method",
                    "toField email address does not exist.", null, toField);
        });
    }

    /**
     * Helper method to add a (five) second pause to allow the Gmail server to
     * receive what has been sent.
     */
    private void pause() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            LOG.error("Threaded sleep failed", e);
        }
    }

    /**
     * Helper method to convert an array of EmailAddress objects to a String
     * array for comparison purposes.
     *
     * @param emails
     * @return String [] of emails
     */
    private String[] convertEmailAddressesToStrings(EmailAddress[] emails) {
        String[] stringEmails = new String[emails.length];
        for (int i = 0; i < emails.length; i++) {
            stringEmails[i] = emails[i].getEmail();
        }
        return stringEmails;
    }

}
