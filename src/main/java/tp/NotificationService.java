package tp;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Orchestre la gestion des abonnements et l'envoi des notifications.
 * Il ne dépend que des abstractions (INotifier) et non des implémentations (SRP & OCP).
 */
public class NotificationService {
    private final Set<Employee> abonnes = new HashSet<>();
    private final List<INotifier> notifiers;

    public NotificationService(List<INotifier> notifiers) {
        this.notifiers = notifiers;
    }

    public void abonner(Employee employe) {
        abonnes.add(employe);
    }

    public void desabonner(Employee employe) {
        abonnes.remove(employe);
    }

    public boolean estAbonne(Employee employe) {
        return abonnes.contains(employe);
    }

    /**
     * NOUVEAU : Retourne une vue non modifiable de la liste des  abonnés.
     * Permet à l'extérieur de consulter la liste sans pouvoir la modifier.
     * @return Un ensemble des employés abonnés.
     */
    public Set<Employee> getAbonnes() {
        return Collections.unmodifiableSet(abonnes);
    }

    public void envoyerMessage(Employee expediteur, String message) {
        System.out.println("\n>>> " + expediteur.getNom() + " envoie un message...");
        if (abonnes.isEmpty()) {
            System.out.println("Aucun employé n'est abonné pour le moment.");
            return;
        }

        abonnes.stream()
                .filter(abonne -> !abonne.equals(expediteur))
                .forEach(abonne -> {
                    notifiers.forEach(notifier -> notifier.send(expediteur, abonne, message));
                });
        System.out.println(">>> Message envoyé à tous les abonnés concernés.");
    }
}