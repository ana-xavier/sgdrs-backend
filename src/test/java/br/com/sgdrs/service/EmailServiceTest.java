package br.com.sgdrs.service;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import br.com.sgdrs.service.util.EmailService;

public class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailService emailService;

    public EmailServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testEnviarSenhaAleatoria() {

        String email = "teste@exemplo.com";
        String senha = "123456";

        String EMAIL_CRIACAO_CONTA = "Seu usuário foi criado no sistema SDGRS! Para acesso, utilizar:\nLogin:" + email
                + "\nSenha:" + senha + "\nEsta senha deve ser alterada no primeiro acesso!";

        emailService.enviarSenhaAleatoria(email, "Criação de Conta - SGDRS", senha);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@sgdrs.com");
        message.setTo(email);
        message.setSubject("Criação de Conta - SGDRS");
        message.setText(EMAIL_CRIACAO_CONTA);

        verify(javaMailSender).send(message);
    }
}
