package pe.gob.fonafe.sistemagestionriesgoapi.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.fonafe.sistemagestionriesgoapi.dao.ICorreoPlanDao;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOCorreoPlan;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOGenerico;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOMailConfig;
import pe.gob.fonafe.sistemagestionriesgoapi.dto.DTOParametro;
import pe.gob.fonafe.sistemagestionriesgoapi.models.CorreoPlanBean;
import pe.gob.fonafe.sistemagestionriesgoapi.service.ICorreoPlanService;
import pe.gob.fonafe.sistemagestionriesgoapi.utils.SNConstantes;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import java.util.*;

@Service
@Transactional
public class CorreoPlanServiceImpl implements ICorreoPlanService {

    /*@Autowired
    JavaMailSender mailSender;*/

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    final ICorreoPlanDao iCorreoPlanDao;

    @Value("${user_email_send}")
    private String user_email_send;

    @Value("${key_email_send}")
    private String key_email_send;

    public CorreoPlanServiceImpl(ICorreoPlanDao iCorreoPlanDao) {
        this.iCorreoPlanDao = iCorreoPlanDao;
    }

    @Override
    public DTOGenerico enviarCorreoPlan(String mailHtml, String destinatarios, String destinatarioJefeInmediato, List<DTOMailConfig> listaMailConfig){

        String correcto = "OK";
        String incorrecto = "NOOK";
        
        logger.info("Inicio de CorreoPlanServiceImpl - enviarCorreo");
       
        DTOGenerico statusProcess = new DTOGenerico();

        Properties prop = new Properties();

        for (DTOMailConfig mailConfig : listaMailConfig){
            prop.put(mailConfig.getDeParametro(),mailConfig.getDeValor1());
        }

        final   String username = user_email_send;

        try {

//           destinatarios="sdc@solutionsdataconsultores.com";
            /*Authenticator auth = new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(user_email_send,key_email_send);
                }
            };

            Session session = Session.getInstance(prop, auth);*/

            Session session = Session.getInstance(prop,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(user_email_send, key_email_send);
                        }
                    });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(destinatarios)
            );

            if (destinatarioJefeInmediato != null) {

                message.setRecipients(
                        Message.RecipientType.CC,
                        InternetAddress.parse(destinatarioJefeInmediato)
                );

            }


            Multipart mp = new MimeMultipart();
            BodyPart htmlPart = new MimeBodyPart();

            htmlPart.setContent(mailHtml,"text/html; charset=utf-8");
            mp.addBodyPart(htmlPart);
            message.setSubject("Notificación de plan de acción");

            message.setText(mailHtml);
            message.saveChanges();
            message.setContent(mp);
            Transport.send(message);

            statusProcess.setOderesult(correcto);
        } catch (MessagingException ex) {
            statusProcess.setOderesult(incorrecto);
            logger.error(ex.getMessage());
        }

        logger.info("Fin de CorreoPlanServiceImpl - enviarCorreo");
        return statusProcess;
    }

    private void showConfirmationForm2(String cadena) {
    	

		
	}

	@Override
    public DTOCorreoPlan obtenerCorreoPlan(){
        logger.info("Inicio de CorreoPlanServiceImpl - obtenerCorreoPlan");
        DTOCorreoPlan dtoCorreoPlan;
        try {
            dtoCorreoPlan = iCorreoPlanDao.obtenerCorreoPlan();
        }catch (Exception ex){
            dtoCorreoPlan = new DTOCorreoPlan();
            logger.error(ex.getMessage());
        }
        logger.info("Fin de CorreoPlanServiceImpl - obtenerCorreoPlan");
        return dtoCorreoPlan;
    }

    @Override
    public DTOCorreoPlan actualizarNotificacionPlanAccion(CorreoPlanBean correoPlanBean) {
        logger.info("Inicio de CorreoPlanServiceImpl - actualizarNotificacionPlanAccion");
        DTOCorreoPlan dtoCorreoPlan = null;

        try {
            dtoCorreoPlan = new DTOCorreoPlan();

            dtoCorreoPlan = iCorreoPlanDao.actualizarNotificacionPlanAccion(correoPlanBean);
        } catch (Exception ex) {
            dtoCorreoPlan = new DTOCorreoPlan();
            dtoCorreoPlan.setIdConfigCorreo(0L);
            dtoCorreoPlan.setCodigoResultado(SNConstantes.CODIGO_ERROR);
            logger.error(ex.getMessage());
        }

        logger.info("Fin de CorreoPlanServiceImpl - actualizarNotificacionPlanAccion");
        return dtoCorreoPlan;
    }

    @Override
    public DTOGenerico listarFechaVencimiento() {
        logger.info("Inicio de CorreoPlanServiceImpl - listarFechaVencimiento");
        DTOGenerico dtoGenerico;

        try {
            dtoGenerico = iCorreoPlanDao.listarFechaVencimiento();
        } catch (Exception ex) {
            dtoGenerico = new DTOGenerico();
            logger.error(ex.getMessage());
        }

        logger.info("Fin de CorreoPlanServiceImpl - listarFechaVencimiento");
        return dtoGenerico;
    }

    private DTOGenerico sendMailTest(String cuerpoCorreo) throws DataAccessException {

        String correcto = "OK";
        String incorrecto = "NOOK";
        String destinatarios = "";
        //log.info("Inicio de MailServiceImpl - sendMailProceso");
        DTOGenerico statusProcess = new DTOGenerico();
        // Properties prop = new Properties();

        final String username = "pruebacanvia@gmail.com";
        final String password = "P@ssw0rd123#";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("pruebacanvia@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse("rpumapillo@canvia.com,richardpumac@gmail.com,pruebacanvia@gmail.com,jtitodc@gmail.com")
            );
            MimeBodyPart htmlPart = new MimeBodyPart();
            Multipart mp = new MimeMultipart();
            htmlPart.setContent(cuerpoCorreo,"text/html; charset=utf-8");
            mp.addBodyPart(htmlPart);

            message.setSubject("Testing Gmail");
            message.setContent(mp);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
        }


        //log.info("Fin de MailServiceImpl - sendMailProceso");

        return statusProcess;

    }

	@Override
	public DTOGenerico enviarCorreo(String mailHtml, String destinatarios, String destinatarioJefeInmediato,
			 String subject) {
		// TODO Auto-generated method stub
		String correcto = "OK";
        String incorrecto = "NOOK";
        //String destinatarios = "";
        //log.info("Inicio de MailServiceImpl - sendMailProceso");
        logger.info("Inicio de Correo Service Impl - enviarCorreo");
        DTOGenerico statusProcess = new DTOGenerico();
        // Properties prop = new Properties();

        final String username = "jtitodc@gmail.com";
        final String password = "fflm looj jlog ndzb";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("jtitodc@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(destinatarios)
            );
            MimeBodyPart htmlPart = new MimeBodyPart();
            Multipart mp = new MimeMultipart();
            htmlPart.setContent(mailHtml,"text/html; charset=utf-8");
            mp.addBodyPart(htmlPart);

            message.setSubject(subject);
            message.setContent(mp);

            Transport.send(message);

            System.out.println("Done");
            statusProcess.setOderesult(correcto);
            //dtoCorreoPlan.setCodigoResultado(SNConstantes.CODIGO_ERROR);
            statusProcess.setCodigoResultado(SNConstantes.CODIGO_EXITO);
        } catch (MessagingException e) {
            //e.printStackTrace();
            statusProcess.setOderesult(incorrecto);
            statusProcess.setCodigoResultado(SNConstantes.CODIGO_ERROR);
            logger.error(e.getMessage());
        }
        logger.info("Fin de Correo - enviarCorreo");
        return statusProcess;
	}


}
