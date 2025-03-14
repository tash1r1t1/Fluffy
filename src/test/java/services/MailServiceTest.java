package services;

import com.fluffy.universe.models.User;
import com.fluffy.universe.services.MailService;
import com.fluffy.universe.utils.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    private static MockedStatic<Configuration> configurationMock;

    @BeforeEach
    void setUp() {
        configurationMock = Mockito.mockStatic(Configuration.class);
        configurationMock.when(() -> Configuration.get("mail.from")).thenReturn("noreply@example.com");
        configurationMock.when(() -> Configuration.get("mail.host")).thenReturn("smtp.example.com");
        configurationMock.when(() -> Configuration.get("mail.port")).thenReturn("587");
        configurationMock.when(() -> Configuration.get("mail.ssl")).thenReturn("true");
        configurationMock.when(() -> Configuration.get("mail.auth")).thenReturn("true");
        configurationMock.when(() -> Configuration.get("mail.user")).thenReturn("user@example.com");
        configurationMock.when(() -> Configuration.get("mail.password")).thenReturn("password");
        configurationMock.when(() -> Configuration.get("application.url")).thenReturn("https://example.com");
    }

    @AfterEach
    void tearDown() {
        configurationMock.close();
    }

    @Test
    void testSendResetLink() throws MessagingException {
        User user = new User();
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setResetPasswordToken("token123");

        try (MockedStatic<Transport> transportMock = mockStatic(Transport.class)) {
            MailService.sendResetLink(user);
            transportMock.verify(() -> Transport.send(any(Message.class)), times(1));
        }
    }

    @Test
    void testGetResource() {
        String result = MailService.getResource("mails/password-reset.txt");
        assertNotNull(result);
    }

    @Test
    void testRender() throws MessagingException {
        Map<String, String> values = Map.of(
                "firstName", "John",
                "url", "https://example.com",
                "token", "token123"
        );
        assertNotNull(MailService.render("password-reset", values));
    }


}