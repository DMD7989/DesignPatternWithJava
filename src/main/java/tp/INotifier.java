package tp;

/**
 * Interface pour tout système de notification.
 * Garantit que toute nouvelle méthode de notification (Email, SMS, etc.)
 * aura une méthode send().
 * C'est la clé pour le principe Ouvert/Fermé (OCP).
 */
public interface INotifier {
    void send(Employee expediteur, Employee destinataire, String message);
}
