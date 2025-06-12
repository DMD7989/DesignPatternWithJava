package tp;

/**
 * Implémentation pour envoyer une notification sur la console.
 */
public class ConsoleNotifier implements INotifier {
    @Override
    public void send(Employee expediteur, Employee destinataire, String message) {
        String notification = "[CONSOLE] 💻 Pour " + destinataire.getNom() + " >> " + message + " (de la part de " + expediteur.getNom() + ")";
        System.out.println(notification);
        // On stocke la notification formatée pour l'historique de l'employé
        destinataire.ajouterNotification(notification);
    }
}
