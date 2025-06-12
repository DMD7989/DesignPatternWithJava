package tp;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Représente un employé.
 * Sa seule responsabilité est de gérer les informations de l'employé
 * et la liste de ses notifications reçues (SRP).
 */
public class Employee {
    private final int id;
    private final String nom;
    private final String email;
    private final List<String> notificationsRecues;

    public Employee(int id, String nom, String email) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.notificationsRecues = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }

    public void ajouterNotification(String message) {
        this.notificationsRecues.add(message);
    }

    public void afficherNotifications() {
        System.out.println("--- Notifications pour " + this.nom + " ---");
        if (notificationsRecues.isEmpty()) {
            System.out.println("Aucune notification.");
        } else {
            notificationsRecues.forEach(System.out::println);
        }
        System.out.println("------------------------------------");
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Nom: " + nom + ", Email: " + email;
    }

    // Essentiel pour que la recherche dans les collections (Set, List) fonctionne correctement.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id == employee.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}