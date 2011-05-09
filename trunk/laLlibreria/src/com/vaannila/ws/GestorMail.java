package com.vaannila.ws;

import java.util.Date;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class GestorMail {
  private static GestorMail instancia = null;

  private Properties p;

  public static GestorMail getInstancia() {
      if(instancia == null) instancia = new GestorMail();
      return instancia;
  }

  private GestorMail() {
      p = new Properties();
      String s = getServerURL();
      p.put("mail.smtp.host", s);
      p.put("mail.smtp.auth", "true");
      String port = getServerPort();
      p.put("mail.smtp.port", port);
      p.put("mail.smtp.starttls.enable", "true");
      p.put("mail.smtp.socketFactory.port", port);
      p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
      p.put("mail.smtp.socketFactory.fallback", "false");
   }

  public boolean enviarMail(String to, String subject, String doc, String firma, String codi) {
      Session session = Session.getInstance(p);
      String username = getUserName();
      String password = getPassword();
      MimeMessage message = new MimeMessage(session);
      try {
          message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
          message.setFrom(new InternetAddress(username));
          
          BodyPart comanda = new MimeBodyPart();
          comanda.setContent(doc,"text/xml");
          comanda.setFileName("COMANDA_DATA="+new Date()+".xml");
          
          BodyPart signatura = new MimeBodyPart();
          signatura.setContent(firma,"text/xml");
          signatura.setFileName("SIGANTURA_DATA="+new Date()+".xml");
          
          BodyPart text = new MimeBodyPart();
          text.setText("El codi de la comanda és "+codi);
          
          
          MimeMultipart correo = new MimeMultipart();
          correo.addBodyPart(comanda);
          correo.addBodyPart(signatura);
          correo.addBodyPart(text);
          
          message.setContent(correo);

          message.setSubject(subject);
          Transport t = session.getTransport("smtp");
          t.connect(username,password);
          t.sendMessage(message, message.getAllRecipients());
          t.close();
          return true;
      } catch(Exception ex) {
          return false;
      }
  }

  public String getServerURL() {
      return "smtp.gmail.com";
  }

  public String getServerPort() {
      return "465";
  }

  public String getUserName() {
      return "lallibreria.cat2011@gmail.com";
  }

  public String getPassword() {
      return "P4$$W0rD";
  }
}
