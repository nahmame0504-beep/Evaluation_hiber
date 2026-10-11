package ma.projet.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import ma.projet.classes.Categorie;
import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommandeProduit;
import ma.projet.classes.Produit;
import ma.projet.service.CategorieService;
import ma.projet.service.CommandeService;
import ma.projet.service.LigneCommandeService;
import ma.projet.service.ProduitService;
import ma.projet.util.HibernateUtil;

public class Test {

    public static void main(String[] args) {
        CategorieService cs = new CategorieService();
        ProduitService ps = new ProduitService();
        CommandeService comService = new CommandeService();
        LigneCommandeService lcs = new LigneCommandeService();

        try {
            System.out.println("============================================================");
            System.out.println("  APPLICATION DE GESTION DE STOCK - MAGASIN INFORMATIQUE   ");
            System.out.println("============================================================\n");

            // Initialisation des données de test si nécessaire
            Commande commande4 = comService.findById(4);
            if (commande4 == null) {
                System.out.println("--- Insertion des données de test initiales ---");
                Categorie c1 = new Categorie("INF", "Informatique");
                Categorie c2 = new Categorie("PER", "Périphériques");
                cs.create(c1);
                cs.create(c2);

                Produit p1 = new Produit("ES12", 120.0f, c1);
                Produit p2 = new Produit("ZR85", 100.0f, c1);
                Produit p3 = new Produit("EE85", 200.0f, c1);
                Produit p4 = new Produit("CL01", 80.0f, c2);
                ps.create(p1);
                ps.create(p2);
                ps.create(p3);
                ps.create(p4);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date4 = sdf.parse("2013-03-14");
                commande4 = new Commande(date4);
                comService.create(commande4);

                lcs.create(new LigneCommandeProduit(commande4, p1, 7));
                lcs.create(new LigneCommandeProduit(commande4, p2, 14));
                lcs.create(new LigneCommandeProduit(commande4, p3, 5));
                System.out.println("Données de test créées avec succès.\n");
            }

            // 1. Test : Affichage des produits par catégorie
            System.out.println(">>> 1. Affichage des produits par catégorie :");
            for (Categorie cat : cs.findAll()) {
                ps.afficherProduitsParCategorie(cat);
                System.out.println();
            }

            // 2. Test : Affichage des produits commandés entre deux dates
            System.out.println(">>> 2. Affichage des produits commandés entre deux dates :");
            SimpleDateFormat sdfDates = new SimpleDateFormat("yyyy-MM-dd");
            Date dateDebut = sdfDates.parse("2013-01-01");
            Date dateFin = sdfDates.parse("2013-12-31");
            ps.afficherProduitsEntreDates(dateDebut, dateFin);
            System.out.println();

            // 3. Test : Affichage des produits commandés dans une commande donnée
            System.out.println(">>> 3. Affichage des produits commandés dans la commande 4 (Exemple attendu) :");
            ps.afficherProduitsParCommande(4);
            System.out.println();

            // 4. Test : Affichage des produits dont le prix est supérieur à 100 DH (Requête nommée)
            System.out.println(">>> 4. Affichage des produits avec un prix > 100 DH (Requête nommée) :");
            ps.afficherProduitsPrixSuperieurs100();
            System.out.println();

            System.out.println("============================================================");
            System.out.println("                   TESTS TERMINÉS AVEC SUCCÈS               ");
            System.out.println("============================================================");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
