package ma.projet.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ma.projet.classes.Employe;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.service.EmployeService;
import ma.projet.service.EmployeTacheService;
import ma.projet.service.ProjetService;
import ma.projet.service.TacheService;
import ma.projet.util.HibernateUtil;

public class ProjetServiceTest {

    private static EmployeService employeService;
    private static ProjetService projetService;
    private static TacheService tacheService;
    private static EmployeTacheService employeTacheService;

    @BeforeClass
    public static void setUp() {
        employeService = new EmployeService();
        projetService = new ProjetService();
        tacheService = new TacheService();
        employeTacheService = new EmployeTacheService();
        Assert.assertNotNull(HibernateUtil.getSessionFactory());
    }

    @AfterClass
    public static void tearDown() {
        HibernateUtil.shutdown();
    }

    @Test
    public void testFindAllEmployes() {
        List<Employe> list = employeService.findAll();
        Assert.assertNotNull(list);
    }

    @Test
    public void testFindAllProjets() {
        List<Projet> list = projetService.findAll();
        Assert.assertNotNull(list);
    }

    @Test
    public void testFindAllTaches() {
        List<Tache> list = tacheService.findAll();
        Assert.assertNotNull(list);
    }

    @Test
    public void testNamedQueryPrixSuperieur1000() {
        List<Tache> list = tacheService.findTachesPrixSuperieur1000();
        Assert.assertNotNull(list);
        for (Tache t : list) {
            Assert.assertTrue(t.getPrix() > 1000);
        }
    }

    @Test
    public void testAfficherTachesRealiseesProjet() {
        Projet p = projetService.findById(4);
        if (p != null) {
            projetService.afficherTachesRealisees(p);
        }
    }

    @Test
    public void testFindTachesEntreDeuxDates() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = sdf.parse("2013-01-01");
        Date d2 = sdf.parse("2013-12-31");
        List<Tache> list = tacheService.findTachesRealiseesEntreDeuxDates(d1, d2);
        Assert.assertNotNull(list);
    }

    @Test
    public void testGetProjetsGeres() {
        Employe e = employeService.findById(1);
        if (e != null) {
            List<Projet> projets = employeService.getProjetsGeres(e);
            Assert.assertNotNull(projets);
        }
    }
}
