package ma.projet.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.service.FemmeService;
import ma.projet.service.HommeService;
import ma.projet.service.MariageService;
import ma.projet.util.HibernateUtil;

public class Test {

    private static void resetDatabase() {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.createNativeQuery("DELETE FROM mariage").executeUpdate();
            session.createNativeQuery("DELETE FROM personne").executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        HommeService hommeService = new HommeService();
        FemmeService femmeService = new FemmeService();
        MariageService mariageService = new MariageService();

        System.out.println("==========================================================================");
        System.out.println("  APPLICATION DE GESTION DE L'ETAT CIVIL - TEST ");
        System.out.println("==========================================================================\n");

        try {
            // Nettoyage préalable pour garantir des tests idempotents et propres
            resetDatabase();

            // --------------------------------------------------------------------
            // 1. Créer 10 femmes et 5 hommes
            // --------------------------------------------------------------------
            System.out.println(">>> 1. Création de 10 femmes et 5 hommes...");

            Femme f1 = new Femme("RAMI", "SALIMA", "0612345601", "Casablanca", sdf.parse("15/04/1970"));
            Femme f2 = new Femme("ALI", "AMAL", "0612345602", "Rabat", sdf.parse("20/08/1974"));
            Femme f3 = new Femme("ALAOUI", "WAFA", "0612345603", "Fès", sdf.parse("12/03/1978"));
            Femme f4 = new Femme("ALAMI", "KARIMA", "0612345604", "Marrakech", sdf.parse("05/11/1962")); // Plus âgée
            Femme f5 = new Femme("SADIK", "LOUBNA", "0612345605", "Tanger", sdf.parse("18/02/1980"));
            Femme f6 = new Femme("BAHA", "SANA", "0612345606", "Agadir", sdf.parse("25/09/1982"));
            Femme f7 = new Femme("KAMAL", "NOURA", "0612345607", "Meknès", sdf.parse("30/06/1985"));
            Femme f8 = new Femme("RADI", "HIND", "0612345608", "Oujda", sdf.parse("14/01/1988"));
            Femme f9 = new Femme("FASSI", "IMANE", "0612345609", "Kenitra", sdf.parse("09/10/1990"));
            Femme f10 = new Femme("TAHIRI", "NADIA", "0612345610", "Tétouan", sdf.parse("22/12/1992"));

            femmeService.create(f1);
            femmeService.create(f2);
            femmeService.create(f3);
            femmeService.create(f4);
            femmeService.create(f5);
            femmeService.create(f6);
            femmeService.create(f7);
            femmeService.create(f8);
            femmeService.create(f9);
            femmeService.create(f10);

            Homme h1 = new Homme("SAFI", "SAID", "0661112233", "Casablanca", sdf.parse("10/01/1965"));
            Homme h2 = new Homme("JALAL", "OMAR", "0661112234", "Rabat", sdf.parse("15/05/1970"));
            Homme h3 = new Homme("MORAD", "KAMAL", "0661112235", "Fès", sdf.parse("20/07/1972"));
            Homme h4 = new Homme("NABIL", "YOUSSEF", "0661112236", "Marrakech", sdf.parse("11/11/1975"));
            Homme h5 = new Homme("RACHID", "AMINE", "0661112237", "Tanger", sdf.parse("03/03/1980"));

            hommeService.create(h1);
            hommeService.create(h2);
            hommeService.create(h3);
            hommeService.create(h4);
            hommeService.create(h5);

            System.out.println("10 femmes et 5 hommes créés avec succès.\n");

            // Création des mariages pour le jeu de tests
            // Mariages de SAFI SAID (h1) :
            // - Karima Alami (échoué) : du 03/09/1989 au 03/09/1990, 0 enfants
            // - Salima Rami (en cours) : depuis 03/09/1990, 4 enfants
            // - Amal Ali (en cours) : depuis 03/09/1995, 2 enfants
            // - Wafa Alaoui (en cours) : depuis 04/11/2000, 3 enfants
            Mariage m1 = new Mariage(h1, f4, sdf.parse("03/09/1989"), sdf.parse("03/09/1990"), 0);
            Mariage m2 = new Mariage(h1, f1, sdf.parse("03/09/1990"), null, 4);
            Mariage m3 = new Mariage(h1, f2, sdf.parse("03/09/1995"), null, 2);
            Mariage m4 = new Mariage(h1, f3, sdf.parse("04/11/2000"), null, 3);

            // Mariages supplémentaires :
            // - Karima Alami (f4) se remarie avec Omar Jalal (h2) -> mariée 2 fois
            Mariage m5 = new Mariage(h2, f4, sdf.parse("15/06/1992"), null, 1);
            // - Loubna Sadik (f5) mariée avec Omar Jalal (h2) puis remariée avec Kamal Morad (h3) -> mariée 2 fois
            Mariage m6 = new Mariage(h2, f5, sdf.parse("01/01/2001"), sdf.parse("01/01/2005"), 1);
            Mariage m7 = new Mariage(h3, f5, sdf.parse("10/05/2006"), null, 2);

            mariageService.create(m1);
            mariageService.create(m2);
            mariageService.create(m3);
            mariageService.create(m4);
            mariageService.create(m5);
            mariageService.create(m6);
            mariageService.create(m7);

            // --------------------------------------------------------------------
            // 2. Afficher la liste des femmes
            // --------------------------------------------------------------------
            System.out.println(">>> 2. Liste des femmes enregistrées :");
            List<Femme> femmes = femmeService.findAll();
            for (Femme f : femmes) {
                System.out.printf("- ID: %d | Nom: %-10s | Prénom: %-10s | Tél: %-12s | Née le: %s%n",
                        f.getId(), f.getNom(), f.getPrenom(), f.getTelephone(),
                        (f.getDateNaissance() != null ? sdf.format(f.getDateNaissance()) : "N/A"));
            }
            System.out.println();

            // --------------------------------------------------------------------
            // 3. Afficher la femme la plus âgée
            // --------------------------------------------------------------------
            System.out.println(">>> 3. Femme la plus âgée :");
            femmeService.afficherFemmePlusAgee();
            System.out.println();

            // --------------------------------------------------------------------
            // 4. Afficher les épouses d’un homme donné entre deux dates
            // --------------------------------------------------------------------
            System.out.println(">>> 4. Affichage des épouses d'un homme entre deux dates :");
            Date dDebut = sdf.parse("01/01/1988");
            Date dFin = sdf.parse("31/12/2005");
            hommeService.afficherEpousesEntreDates(h1, dDebut, dFin);
            System.out.println();

            // --------------------------------------------------------------------
            // 5. Afficher le nombre d’enfants d’une femme entre deux dates (requête native nommée)
            // --------------------------------------------------------------------
            System.out.println(">>> 5. Nombre d'enfants d'une femme entre deux dates (Requête native nommée) :");
            femmeService.afficherNombreEnfantsEntreDates(f1, sdf.parse("01/01/1990"), sdf.parse("31/12/2005"));
            femmeService.afficherNombreEnfantsEntreDates(f4, sdf.parse("01/01/1988"), sdf.parse("31/12/2000"));
            System.out.println();

            // --------------------------------------------------------------------
            // 6. Afficher les femmes mariées deux fois ou plus (requête nommée)
            // --------------------------------------------------------------------
            System.out.println(">>> 6. Femmes mariées au moins deux fois (Requête nommée) :");
            femmeService.afficherFemmesMarieesAuMoinsDeuxFois();
            System.out.println();

            // --------------------------------------------------------------------
            // 7. Afficher les hommes mariés à quatre femmes entre deux dates (API Criteria)
            // --------------------------------------------------------------------
            System.out.println(">>> 7. Nombre d'hommes mariés à quatre femmes entre deux dates (API Criteria) :");
            hommeService.afficherHommesMariesAQuatreFemmes(dDebut, dFin);
            System.out.println();

            // --------------------------------------------------------------------
            // 8. Afficher les mariages d’un homme avec tous les détails
            // --------------------------------------------------------------------
            System.out.println(">>> 8. Détails des mariages d'un homme donné (SAFI SAID) :");
            hommeService.afficherMariages(h1);
            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }

        System.out.println("==========================================================================");
        System.out.println("  FIN DU TEST AVEC SUCCES ");
        System.out.println("==========================================================================");
    }
}
