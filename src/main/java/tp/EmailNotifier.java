package tp;

/**
 * Simulation d'un envoi de notification par email.
 */
public class EmailNotifier implements INotifier {
    @Override
    public void send(Employee expediteur, Employee destinataire, String message) {
        String notification = "[EMAIL] 📧 Envoyé à " + destinataire.getEmail() + " : \"" + message + "\" (Envoyé par " + expediteur.getNom() + ")";
        System.out.println(notification);
        // On stocke la notification formatée pour l'historique de l'employé
        destinataire.ajouterNotification(notification);
    }
}
