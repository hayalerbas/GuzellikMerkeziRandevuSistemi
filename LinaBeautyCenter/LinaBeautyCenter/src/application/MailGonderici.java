package application;

import java.util.Properties;

import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class MailGonderici {

    public static void mailGonder(String kime,
                                  String konu,
                                  String mesaj) {

        final String gonderenMail =
        	"linabeautygelisim@gmail.com";

        final String sifre =
                "nzkz wzpu fsnn vshw";

        Properties props = new Properties();

        props.put(
                "mail.smtp.auth",
                "true");

        props.put(
                "mail.smtp.starttls.enable",
                "true");

        props.put(
                "mail.smtp.host",
                "smtp.gmail.com");

        props.put(
                "mail.smtp.port",
                "587");

        Session session =
                Session.getInstance(

                        props,

                        new jakarta.mail.Authenticator() {

                            @Override
                            protected PasswordAuthentication
                            getPasswordAuthentication() {

                                return new PasswordAuthentication(
                                        gonderenMail,
                                        sifre
                                );
                            }
                        });

        try {

            Message message =
                    new MimeMessage(session);

            message.setFrom(
                    new InternetAddress(
                            gonderenMail));

            message.setRecipients(
                    Message.RecipientType.TO,

                    InternetAddress.parse(kime));

            message.setSubject(konu);

            message.setText(mesaj);

            Transport.send(message);

            System.out.println(
                    "Mail gönderildi.");

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // DOĞRULAMA KODU ÜRET

    public static String kodUret() {

        int kod =
                (int) (100000 + Math.random() * 900000);

        return String.valueOf(kod);
    }

    // DOĞRULAMA KODU GÖNDER

    public static boolean kodGonder(String email,
                                    String kod) {

        try {

            mailGonder(

                    email,

                    "Doğrulama Kodunuz",

                    "Doğrulama kodunuz: " + kod
            );

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }
}