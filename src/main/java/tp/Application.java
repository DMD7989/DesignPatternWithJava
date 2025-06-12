package tp;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

public class Application {

    private static final List<Employee> employes = new ArrayList<>();
    private static final NotificationService service;
    // NOUVEAU : Un compteur pour générer des ID uniques pour les nouveaux employés.
    private static int nextEmployeeId = 1;

    // Initialisation du service avec les notifiers disponibles
    static {
        // On initialise notre liste de notifieurs.
        // On peut garder les deux : la notification console et le vrai email !
        List<INotifier> notifiers = List.of(
                new ConsoleNotifier(),
                new RealEmailNotifier() // On remplace la simulation par le vrai notifieur
        );
        service = new NotificationService(notifiers);
    }

    public static void main(String[] args) {
        // Création des employés de l'entreprise
        initialiserEmployes();

        // Boucle du menu interactif
        Scanner scanner = new Scanner(System.in);
        int choix = -1;
        while (choix != 0) {
            afficherMenu();
            try {
                choix = Integer.parseInt(scanner.nextLine());
                gererChoixMenu(choix, scanner);
            } catch (NumberFormatException e) {
                System.out.println("Erreur : Veuillez entrer un nombre valide.");
            }
        }
        System.out.println("Au revoir !");
        scanner.close();
    }

    private static void initialiserEmployes() {
        // On utilise la méthode d'ajout pour s'assurer que les ID sont bien gérés
        creerNouvelEmploye("Djimmoh", "djimmoh20@gmail.com");
        creerNouvelEmploye("DmD", "dembeledjime83@gmail.com");
    }

    // MÉTHODE MODIFIÉE pour centraliser la création et la gestion des ID
    private static void creerNouvelEmploye(String nom, String email) {
        Employee nouvelEmploye = new Employee(nextEmployeeId, nom, email);
        employes.add(nouvelEmploye);
        nextEmployeeId++; // On incrémente l'ID pour le prochain
    }

    private static void afficherMenu() {
        System.out.println("\n--- MENU DE NOTIFICATION ---");
        System.out.println("1. Lister tous les employés");
        System.out.println("2. Lister les employés abonnés"); // NOUVELLE OPTION
        System.out.println("3. Ajouter un nouvel employé");    // NOUVELLE OPTION
        System.out.println("4. Abonner un employé");
        System.out.println("5. Désabonner un employé");
        System.out.println("6. Envoyer un message à tous les abonnés");
        System.out.println("7. Afficher les notifications d'un employé");
        System.out.println("8. Vérifier si un employé est abonné");
        System.out.println("0. Quitter");
        System.out.print("Votre choix : ");
    }

    // MÉTHODE MISE À JOUR avec les nouvelles options
    private static void gererChoixMenu(int choix, Scanner scanner) {
        switch (choix) {
            case 1 -> listerTousLesEmployes();
            case 2 -> listerEmployesAbonnes(); // NOUVEAU
            case 3 -> ajouterEmploye(scanner); // NOUVEAU
            case 4 -> gererAbonnement(scanner, true);
            case 5 -> gererAbonnement(scanner, false);
            case 6 -> envoyerMessage(scanner);
            case 7 -> afficherNotificationsEmploye(scanner);
            case 8 -> verifierAbonnement(scanner);
            case 0 -> {}
            default -> System.out.println("Choix invalide.");
        }
    }

    // RENOMMÉE pour plus de clarté
    private static void listerTousLesEmployes() {
        System.out.println("\n--- Liste de tous les employés ---");
        employes.forEach(System.out::println);
    }

    // NOUVELLE MÉTHODE
    private static void listerEmployesAbonnes() {
        System.out.println("\n--- Liste des employés abonnés ---");
        Set<Employee> abonnes = service.getAbonnes();
        if (abonnes.isEmpty()) {
            System.out.println("Aucun employé n'est actuellement abonné.");
        } else {
            abonnes.forEach(System.out::println);
        }
    }

    // NOUVELLE MÉTHODE
    private static void ajouterEmploye(Scanner scanner) {
        System.out.println("\n--- Ajout d'un nouvel employé ---");
        System.out.print("Entrez le nom de l'employé : ");
        String nom = scanner.nextLine();
        System.out.print("Entrez l'email de l'employé : ");
        String email = scanner.nextLine();

        creerNouvelEmploye(nom, email);

        System.out.println("L'employé '" + nom + "' a été ajouté avec l'ID " + (nextEmployeeId - 1) + ".");
    }

    // Le reste des méthodes (gererAbonnement, envoyerMessage, etc.) reste identique
    // mais doit être adapté pour utiliser la nouvelle numérotation si nécessaire.
    // Le code ci-dessous est inchangé.
    private static void gererAbonnement(Scanner scanner, boolean abonner) {
        String action = abonner ? "abonner" : "désabonner";
        System.out.print("Entrez l'ID de l'employé à " + action + " : ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Optional<Employee> employeOpt = findEmployeeById(id);
            if (employeOpt.isPresent()) {
                if (abonner) {
                    service.abonner(employeOpt.get());
                    System.out.println(employeOpt.get().getNom() + " a été abonné.");
                } else {
                    service.desabonner(employeOpt.get());
                    System.out.println(employeOpt.get().getNom() + " a été désabonné.");
                }
            } else {
                System.out.println("Aucun employé trouvé avec cet ID.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID invalide.");
        }
    }

    private static void envoyerMessage(Scanner scanner) {
        System.out.print("Entrez l'ID de l'expéditeur : ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Optional<Employee> expediteurOpt = findEmployeeById(id);
            if (expediteurOpt.isPresent()) {
                System.out.print("Entrez votre message : ");
                String message = scanner.nextLine();
                service.envoyerMessage(expediteurOpt.get(), message);
            } else {
                System.out.println("Aucun employé trouvé avec cet ID.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID invalide.");
        }
    }

    private static void afficherNotificationsEmploye(Scanner scanner) {
        System.out.print("Entrez l'ID de l'employé pour voir ses notifications : ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Optional<Employee> employeOpt = findEmployeeById(id);
            employeOpt.ifPresentOrElse(
                    Employee::afficherNotifications,
                    () -> System.out.println("Aucun employé trouvé avec cet ID.")
            );
        } catch (NumberFormatException e) {
            System.out.println("ID invalide.");
        }
    }

    private static void verifierAbonnement(Scanner scanner) {
        System.out.print("Entrez l'ID de l'employé à vérifier : ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Optional<Employee> employeOpt = findEmployeeById(id);
            if (employeOpt.isPresent()) {
                Employee employe = employeOpt.get();
                if (service.estAbonne(employe)) {
                    System.out.println(employe.getNom() + " est bien abonné.");
                } else {
                    System.out.println(employe.getNom() + " n'est pas abonné.");
                }
            } else {
                System.out.println("Aucun employé trouvé avec cet ID.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID invalide.");
        }
    }

    private static Optional<Employee> findEmployeeById(int id) {
        return employes.stream().filter(e -> e.getId() == id).findFirst();
    }
}