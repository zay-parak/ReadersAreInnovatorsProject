package connection;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;

import jakarta.mail.internet.MimeMessage;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Properties;





public class sendEmail {
    private static final int VERIFICATION_CODE_LENGTH = 4;

        private String receiverEmail;
        private Integer type;
        private String subject;
        private String text;

        public sendEmail(String receiverEmail, Integer type, String subject, String text) {
            this.receiverEmail = receiverEmail;
            this.type = type;
            this.subject = subject;
            this.text = text;
        }
    


    public String emailSender(){
            String sender = "official.jarz.co@gmail.com";
            String password ="owxktkzpdxdhxmvy"; //"kvkhiglhtmpnjnjl";

            String verificationCode = generateVerificationCode(); // Generate a verification code


            if (type == 1){
                subject = "Email Verification";
                text = "Please click the link below to verify your email:\n\n" +
                        "http://192.168.20.220:8080/RIP_FrontEnd/" +
                        "\n Your verification code is: "+verificationCode;
            }



            Properties props = new Properties();
             props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2"); // Set the SSL/TLS protocol explicitly
            props.setProperty("mail.smtp.starttls.enable","true"); // Set the cipher suite explicitly
            
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.trust","smtp.gmail.com" );
            
         
            Session session = Session.getInstance(props, new Authenticator() {
    
    @Override
    protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
        return new jakarta.mail.PasswordAuthentication(sender, password);
    }
});

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("localhost"));
               
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverEmail));
                message.setSubject(subject);
                message.setText(text);
                Transport.send(message);
                return verificationCode;

            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return"Email not sent";
        }

        private static String generateVerificationCode() {
            SecureRandom secureRandom = new SecureRandom();
            byte[] randomBytes = new byte[VERIFICATION_CODE_LENGTH];
            secureRandom.nextBytes(randomBytes);
            String verificationCode = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
            return verificationCode;
        }
}

