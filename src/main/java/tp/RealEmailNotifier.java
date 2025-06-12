package tp;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

/**
 * Implémentation pour envoyer une VRAIE notification par email
 * en utilisant Jakarta Mail.
 */
public class RealEmailNotifier implements INotifier {

    private final Properties mailProperties = new Properties();
    private final String username;
    private final String password;

    /**
     * Le constructeur lit la configuration depuis le fichier config.properties.
     */
    public RealEmailNotifier() {
        try (FileReader reader = new FileReader("config.properties")) {
            mailProperties.load(reader);
        } catch (IOException e) {
            // Dans une vraie application, on utiliserait un logger
            System.err.println("ERREUR CRITIQUE: Le fichier config.properties est introuvable ou illisible.");
            // On lève une exception pour empêcher l'application de démarrer sans configuration
            throw new RuntimeException("Impossible de charger la configuration mail.", e);
        }

        this.username = mailProperties.getProperty("mail.user");
        this.password = mailProperties.getProperty("mail.password");

        if (this.username == null || this.password == null) {
            throw new RuntimeException("L'utilisateur ou le mot de passe mail n'est pas défini dans config.properties.");
        }
    }

    @Override
    public void send(Employee expediteur, Employee destinataire, String message) {
        // Configuration de la session de messagerie
        Session session = Session.getInstance(mailProperties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Création de l'objet MimeMessage
            Message mimeMessage = new MimeMessage(session);

            // Définir l'expéditeur
            mimeMessage.setFrom(new InternetAddress(this.username));

            // Définir le destinataire
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinataire.getEmail()));

            // Définir le sujet
            mimeMessage.setSubject("Notification de la part de " + expediteur.getNom());

            // Définir le corps du message
            String emailContent = "Bonjour " + destinataire.getNom() + ",\n\n"
                    + "Vous avez reçu le message suivant de la part de " + expediteur.getNom() + " :\n\n"
                    + "\"" + message + "\"\n\n"
                    + "Cordialement,\nVotre système de notification.";
            mimeMessage.setText(emailContent);

            // Envoyer le message
            Transport.send(mimeMessage);

            String confirmation = "[EMAIL] ✅ Email réel envoyé à " + destinataire.getEmail();
            System.out.println(confirmation);
            destinataire.ajouterNotification(confirmation);

        } catch (MessagingException e) {
            String erreur = "[EMAIL] ❌ Échec de l'envoi de l'email à " + destinataire.getEmail() + ". Cause: " + e.getMessage();
            System.err.println(erreur);
            destinataire.ajouterNotification(erreur);
        }
    }
}
