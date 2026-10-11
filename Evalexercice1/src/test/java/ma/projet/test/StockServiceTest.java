package ma.projet.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ma.projet.classes.Categorie;
import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommandeProduit;
import ma.projet.classes.Produit;
import ma.projet.service.CategorieService;
import ma.projet.service.CommandeService;
import ma.projet.service.LigneCommandeService;
import ma.projet.service.ProduitService;
import ma.projet.util.HibernateUtil;

public class StockServiceTest {

    private static CategorieService categorieService;
    private static ProduitService produitService;
    private static CommandeService commandeService;
    private static LigneCommandeService ligneCommandeService;

    @BeforeClass
    public static void setUp() {
        categorieService = new CategorieService();
        produitService = new ProduitService();
        commandeService = new CommandeService();
        ligneCommandeService = new LigneCommandeService();
        Assert.assertNotNull(HibernateUtil.getSessionFactory());
    }

    @AfterClass
    public static void tearDown() {
        HibernateUtil.shutdown();
    }

    @Test
    public void testFindAllCategories() {
        List<Categorie> categories = categorieService.findAll();
        Assert.assertNotNull(categories);
    }

    @Test
    public void testFindAllProduits() {
        List<Produit> produits = produitService.findAll();
        Assert.assertNotNull(produits);
    }

    @Test
    public void testNamedQueryPrixSuperieur100() {
        List<Produit> produits = produitService.findProduitsPrixSuperieurs100();
        Assert.assertNotNull(produits);
        for (Produit p : produits) {
            Assert.assertTrue(p.getPrix() > 100);
        }
    }

    @Test
    public void testAfficherProduitsParCommande() {
        // Test sur la commande 4
        Commande c4 = commandeService.findById(4);
        if (c4 != null) {
            produitService.afficherProduitsParCommande(c4);
        }
    }

    @Test
    public void testFindProduitsEntreDates() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = sdf.parse("2013-01-01");
        Date d2 = sdf.parse("2013-12-31");
        List<Produit> list = produitService.findProduitsEntreDates(d1, d2);
        Assert.assertNotNull(list);
    }
}
