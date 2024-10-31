package ProjetoTCC.TCC2.service;

import ProjetoTCC.TCC2.entity.Tarefa;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

//    public void enviarEmailBoasVindas(String to, String nomeUsuario) {
//        SimpleMailMessage mensagem = new SimpleMailMessage();
//        mensagem.setFrom("nosimplereply@gmail.com");
//        mensagem.setTo(to);
//        mensagem.setSubject("Bem-vindo ao Simple");
//        mensagem.setText("Olá, " + nomeUsuario + "! Bem-vindo ao Simple, estamos felizes em ter você conosco!");
//        mailSender.send(mensagem);
//    }

    public void enviarNotificacao(Tarefa tarefa, String para, String tarefaNome, String tarefaDescricao, LocalDate tarefaDataFinal) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        String tarefaUrl = "http://localhost:8080/tarefas" + tarefa.getId();

        helper.setFrom("nosimplereply@gmail.com");
        helper.setTo(para);
        helper.setSubject("Nova Tarefa Criada");

        String htmlMsg = "<div style='background-color: white; padding: 20px; font-family: Arial, sans-serif;'>"
                + "<p>Tarefa: <strong>" + tarefaNome + "</strong></p>"
                + "<p>Descrição: " + tarefaDescricao + "</p>"
                + "<p>Data Final: " + tarefaDataFinal + "</p>"
                + "<hr>"
                + "<div style='display: flex; align-items: center; justify-content: space-between;'>"
                + "<img src='cid:logo' alt='Logo' style='width: 130px; height: auto;'/>"
                + "<a href='" + tarefaUrl + "' style='"
                + "display: inline-block; "
                + "padding: 10px 20px; "
                + "font-size: 16px; "
                + "color: white; " // Corrigido para color
                + "text-decoration: none; "
                + "border: 1px solid black; "
                + "background-color: #849bff; " // Cor de fundo do botão
                + "border-radius: 20px; "
                + "margin-left: 20px;'>"
                + "Acessar Tarefa</a>"
                + "</div>"
                + "</div>";


        helper.setText(htmlMsg, true);
        helper.addInline("logo", new FileSystemResource("C:/Programacao/Projeto-TCC/TCC-Identidade/LOGO/LOGO-SIMPLE (2).png"));

        mailSender.send(message);
    }
}
//"C:\Programacao\Projeto-TCC\TCC-Identidade\LOGO\LOGO-SIMPLE (2).png"

