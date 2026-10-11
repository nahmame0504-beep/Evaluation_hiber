package ma.projet.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ma.projet.beans.Femme;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;

public class FemmeService implements IDao<Femme> {

    @Override
    public boolean create(Femme o) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.save(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public boolean update(Femme o) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.update(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public boolean delete(Femme o) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.delete(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Femme findById(int id) {
        Session session = null;
        Femme femme = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            femme = session.get(Femme.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return femme;
    }

    @Override
    public List<Femme> findAll() {
        Session session = null;
        List<Femme> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            list = session.createQuery("from Femme", Femme.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return list;
    }

    /**
     * Exécute une requête native nommée retournant le nombre d’enfants d’une femme entre deux dates.
     */
    public int countEnfantsEntreDates(int femmeId, Date dateDebut, Date dateFin) {
        Session session = null;
        int count = 0;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            @SuppressWarnings("rawtypes")
            Query query = session.getNamedNativeQuery("Femme.countEnfantsEntreDates");
            query.setParameter("femmeId", femmeId);
            query.setParameter("dateDebut", dateDebut);
            query.setParameter("dateFin", dateFin);
            Object result = query.getSingleResult();
            if (result != null) {
                count = ((Number) result).intValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return count;
    }

    public int countEnfantsEntreDates(Femme femme, Date dateDebut, Date dateFin) {
        if (femme == null) {
            return 0;
        }
        return countEnfantsEntreDates(femme.getId(), dateDebut, dateFin);
    }

    public void afficherNombreEnfantsEntreDates(Femme femme, Date dateDebut, Date dateFin) {
        if (femme == null) {
            System.out.println("Femme invalide.");
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        int count = countEnfantsEntreDates(femme, dateDebut, dateFin);
        System.out.println("Nombre d'enfants de " + femme.getPrenom() + " " + femme.getNom() +
                " entre " + sdf.format(dateDebut) + " et " + sdf.format(dateFin) + " : " + count);
    }

    /**
     * Exécute une requête nommée retournant les femmes mariées au moins deux fois.
     */
    public List<Femme> findFemmesMarieesAuMoinsDeuxFois() {
        Session session = null;
        List<Femme> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Femme> query = session.createNamedQuery("Femme.findFemmesMarieesAuMoinsDeuxFois", Femme.class);
            list = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return list;
    }

    public void afficherFemmesMarieesAuMoinsDeuxFois() {
        System.out.println("Femmes mariées au moins deux fois :");
        List<Femme> list = findFemmesMarieesAuMoinsDeuxFois();
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Femme f = list.get(i);
                System.out.println((i + 1) + ". " + f.getPrenom() + " " + f.getNom());
            }
        } else {
            System.out.println("Aucune femme mariée deux fois ou plus.");
        }
    }

    /**
     * Retourne et affiche la femme la plus âgée.
     */
    public Femme getFemmePlusAgee() {
        Session session = null;
        Femme femme = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Femme> query = session.createQuery(
                    "from Femme f where f.dateNaissance is not null order by f.dateNaissance asc",
                    Femme.class
            );
            query.setMaxResults(1);
            List<Femme> list = query.list();
            if (list != null && !list.isEmpty()) {
                femme = list.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return femme;
    }

    public void afficherFemmePlusAgee() {
        Femme f = getFemmePlusAgee();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if (f != null) {
            System.out.println("La femme la plus âgée est : " + f.getPrenom() + " " + f.getNom() +
                    " (née le " + (f.getDateNaissance() != null ? sdf.format(f.getDateNaissance()) : "inconnue") + ")");
        } else {
            System.out.println("Aucune femme trouvée.");
        }
    }
}
