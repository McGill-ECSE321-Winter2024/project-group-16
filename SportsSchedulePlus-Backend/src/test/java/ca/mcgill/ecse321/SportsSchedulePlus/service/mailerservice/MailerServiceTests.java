package ca.mcgill.ecse321.SportsSchedulePlus.service.mailerservice;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.mail.javamail.JavaMailSender;

import ca.mcgill.ecse321.SportsSchedulePlus.beans.MailConfigBean;
import jodd.mail.Email;
import jodd.mail.MailServer;
import jodd.mail.SendMailSession;
import jodd.mail.SmtpServer;

public class MailerServiceTests {
    
    @Mock
    private MailConfigBean mockBean;

    @Mock
    private SmtpServer mockServer;

    @Mock
    private SendMailSession mockSession;

    @InjectMocks
    private Mailer mailer;

    @BeforeEach
    public void setMockOutput() {
        lenient().when(mockBean.getUserEmailAddress()).thenReturn("test@example.com");
        lenient().when(mockBean.getSmtpUrl()).thenReturn("smtp.example.com");
        lenient().when(mockBean.getEmailPassword()).thenReturn("password");

        lenient().when(MailServer.create()
                .ssl(true)
                .host(mockBean.getSmtpUrl())
                .auth(mockBean.getUserEmailAddress(), mockBean.getEmailPassword())
                .buildSmtpMailServer()).thenReturn(mockServer);

        lenient().when(mockServer.createSession()).thenReturn(mockSession);

        mailer = new Mailer(mockBean);
    }

    @Test
    public void testSendEmail() throws IOException {
        String subject = "Test Subject";
        String textMessage = "Test Text Message";
        String htmlText = "<html><body><h1>Test HTML Message</h1></body></html>";
        String toField = "recipient@example.com";

        Email result = mailer.sendEmail(subject, textMessage, htmlText, toField);
        
        verify(mockSession, times(1)).open();
        verify(mockSession, times(1)).sendMail(any(Email.class));
    }

    @Test
    public void testSendEmailWithInvalidAddress() throws IOException {
        String subject = "Test Subject";
        String textMessage = "Test Text Message";
        String htmlText = "<html><body><h1>Test HTML Message</h1></body></html>";
        String toField = "invalid email";

        Email result = mailer.sendEmail(subject, textMessage, htmlText, toField);

        verify(mockSession, times(0)).open();
        verify(mockSession, times(0)).sendMail(any(Email.class));
    }

    @Test
    public void testCheckEmail() {
        String validEmail = "test@example.com";
        String invalidEmail = "invalid email";

        assertTrue(Mailer.checkEmail(validEmail));
        assertFalse(Mailer.checkEmail(invalidEmail));
    }
}

