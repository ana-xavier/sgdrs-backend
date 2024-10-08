package br.com.sgdrs.service.util;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  private final JavaMailSender emailSender;

  public EmailService(JavaMailSender emailSender) {
    this.emailSender = emailSender;
  }

  public void enviarSenhaAleatoria(String email, String subject, String senha) {

    String EMAIL_CRIACAO_CONTA = "Seu usuário foi criado no sistema SDGRS! Para acesso, utilizar:\nLogin:" + email
        + "\nSenha:" + senha + "\nEsta senha deve ser alterada no primeiro acesso!";

    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("noreply@sgdrs.com");
    message.setSubject(subject);
    message.setTo(email);
    message.setText(EMAIL_CRIACAO_CONTA);
    emailSender.send(message);
  }
}