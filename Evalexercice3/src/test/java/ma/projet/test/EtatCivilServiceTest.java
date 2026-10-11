package ma.projet.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.service.FemmeService;
import ma.projet.service.HommeService;
import ma.projet.service.MariageService;
import ma.projet.util.HibernateUtil;

public class EtatCivilServiceTest {

    private static HommeService hommeService;
    private static FemmeService femmeService;
    private static MariageService mariageService;
    private static SimpleDateFormat sdf;

    @BeforeClass
    public static void setUp() {
        hommeService = new HommeService();
        femmeService = new FemmeService();
        mariageService = new MariageService();
        sdf = new SimpleDateFormat("dd/MM/yyyy");
    }

    @AfterClass
    public static void tearDown() {
        HibernateUtil.shutdown();
    }

    @Test
    public void testSessionFactoryCreation() {
        assertNotNull("SessionFactory must not be null", HibernateUtil.getSessionFactory());
    }

    @Test
    public void testCreateFindPersonnesEtMariages() throws Exception {
        Femme f = new Femme("BENANI", "SAMIRA", "0600000001", "Rabat", sdf.parse("10/05/1985"));
        assertTrue(femmeService.create(f));
        assertTrue(f.getId() > 0);

        Homme h = new Homme("DRISSI", "KARIM", "0600000002", "Casablanca", sdf.parse("15/09/1980"));
        assertTrue(hommeService.create(h));
        assertTrue(h.getId() > 0);

        Mariage m = new Mariage(h, f, sdf.parse("01/01/2010"), null, 2);
        assertTrue(mariageService.create(m));

        Mariage found = mariageService.findById(h.getId(), f.getId(), sdf.parse("01/01/2010"));
        assertNotNull("Mariage must be retrieved by composite PK", found);
        assertEquals(2, found.getNbrEnfant());

        int count = femmeService.countEnfantsEntreDates(f.getId(), sdf.parse("01/01/2009"), sdf.parse("31/12/2011"));
        assertTrue("Children count should be at least 2", count >= 2);

        List<Femme> epouses = hommeService.getEpousesEntreDates(h, sdf.parse("01/01/2009"), sdf.parse("31/12/2011"));
        assertNotNull(epouses);
        assertTrue(!epouses.isEmpty());
    }
}
