package ma.projet.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import ma.projet.classes.Employe;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.service.EmployeService;
import ma.projet.service.EmployeTacheService;
import ma.projet.service.ProjetService;
import ma.projet.service.TacheService;
import ma.projet.util.HibernateUtil;

public class Test {

    public static void main(String[] args) {
        EmployeService employeService = new EmployeService();
        ProjetService projetService = new ProjetService();
        TacheService tacheService = new TacheService();
        EmployeTacheService employeTacheService = new EmployeTacheService();

        try {
            System.out.println("============================================================");
            System.out.println("        APPLICATION DE GESTION DE PROJETS - TESTS          ");
            System.out.println("============================================================\n");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            // Initialisation des données si le projet 4 n'existe pas encore
            Projet p4 = projetService.findById(4);
            Employe e1 = null;
            Employe e2 = null;

            if (p4 == null) {
                System.out.println("--- Insertion des données de test initiales ---");

                // Création des employés
                e1 = new Employe("ALAMI", "Ahmed", "0611223344");
                e2 = new Employe("BENANI", "Karim", "0655667788");
                employeService.create(e1);
                employeService.create(e2);

                // Alignement des IDs pour Projet (Projet 4)
                for (int i = 1; i <= 3; i++) {
                    Projet dummy = new Projet("Projet Init " + i, sdf.parse("2012-01-01"), sdf.parse("2012-12-31"), e1);
                    projetService.create(dummy);
                }

                // Projet 4 : Gestion de stock
                p4 = new Projet("Gestion de stock", sdf.parse("2013-01-14"), sdf.parse("2013-12-31"), e1);
                projetService.create(p4);

                // Alignement des IDs pour Tache (Taches 12, 13, 14)
                for (int i = 1; i <= 11; i++) {
                    Tache dummyTache = new Tache("Tâche Init " + i, sdf.parse("2012-01-01"), sdf.parse("2012-01-15"), 500.0, p4);
                    tacheService.create(dummyTache);
                }

                // Tâches 12, 13, 14
                Tache t12 = new Tache("Analyse", sdf.parse("2013-02-01"), sdf.parse("2013-02-28"), 1500.0, p4);
                Tache t13 = new Tache("Conception", sdf.parse("2013-03-01"), sdf.parse("2013-03-31"), 2500.0, p4);
                Tache t14 = new Tache("Développement", sdf.parse("2013-04-01"), sdf.parse("2013-04-30"), 800.0, p4);
                tacheService.create(t12);
                tacheService.create(t13);
                tacheService.create(t14);

                // Affectation des tâches réalisées avec les dates réelles
                EmployeTache et1 = new EmployeTache(e2, t12, sdf.parse("2013-02-10"), sdf.parse("2013-02-20"));
                EmployeTache et2 = new EmployeTache(e2, t13, sdf.parse("2013-03-10"), sdf.parse("2013-03-15"));
                EmployeTache et3 = new EmployeTache(e2, t14, sdf.parse("2013-04-10"), sdf.parse("2013-04-25"));
                employeTacheService.create(et1);
                employeTacheService.create(et2);
                employeTacheService.create(et3);

                System.out.println("Données initialisées avec succès.\n");
            } else {
                e1 = p4.getChefProjet();
                if (e1 == null && !employeService.findAll().isEmpty()) {
                    e1 = employeService.findAll().get(0);
                }
                e2 = employeService.findById(2);
                if (e2 == null && employeService.findAll().size() > 1) {
                    e2 = employeService.findAll().get(1);
                }
            }

            // 1. EmployeService : Afficher la liste des tâches réalisées par un employé
            System.out.println(">>> 1. Liste des tâches réalisées par un employé :");
            if (e2 != null) {
                employeService.afficherTachesRealisees(e2);
            }
            System.out.println();

            // 2. EmployeService : Afficher la liste des projets gérés par un employé
            System.out.println(">>> 2. Liste des projets gérés par un employé :");
            if (e1 != null) {
                employeService.afficherProjetsGeres(e1);
            }
            System.out.println();

            // 3. ProjetService : Afficher la liste des tâches planifiées pour un projet
            System.out.println(">>> 3. Liste des tâches planifiées pour le projet 4 :");
            projetService.afficherTachesPlanifiees(4);
            System.out.println();

            // 4. ProjetService : Afficher la liste des tâches réalisées avec les dates réelles (Exemple attendu)
            System.out.println(">>> 4. Affichage attendu pour le projet 4 (Dates réelles) :");
            projetService.afficherTachesRealisees(4);
            System.out.println();

            // 5. TacheService : Afficher les tâches dont le prix est supérieur à 1000 DH (Requête nommée)
            System.out.println(">>> 5. Tâches dont le prix est supérieur à 1000 DH (Requête nommée) :");
            tacheService.afficherTachesPrixSuperieur1000();
            System.out.println();

            // 6. TacheService : Afficher les tâches réalisées entre deux dates
            System.out.println(">>> 6. Tâches réalisées entre le 01/02/2013 et le 31/03/2013 :");
            Date dDebut = sdf.parse("2013-02-01");
            Date dFin = sdf.parse("2013-03-31");
            tacheService.afficherTachesRealiseesEntreDeuxDates(dDebut, dFin);
            System.out.println();

            System.out.println("============================================================");
            System.out.println("                   TESTS TERMINÉS AVEC SUCCÈS               ");
            System.out.println("============================================================\n");

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
