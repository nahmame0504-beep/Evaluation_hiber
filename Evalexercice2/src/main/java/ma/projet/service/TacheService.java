package ma.projet.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Tache;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;

public class TacheService implements IDao<Tache> {

    @Override
    public boolean create(Tache o) {
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
    public boolean update(Tache o) {
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
    public boolean delete(Tache o) {
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
    public Tache findById(int id) {
        Session session = null;
        Tache t = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            t = session.get(Tache.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return t;
    }

    @Override
    public List<Tache> findAll() {
        Session session = null;
        List<Tache> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            list = session.createQuery("from Tache", Tache.class).list();
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
     * Récupérer les tâches dont le prix est supérieur à 1000 DH via NamedQuery
     */
    public List<Tache> findTachesPrixSuperieur1000() {
        Session session = null;
        List<Tache> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Tache> query = session.createNamedQuery("Tache.findByPrixSuperieurA1000", Tache.class);
            list = query.list();
        } catch (Exception e) {
            // Tentative avec le second nom si besoin
            try {
                if (session != null && session.isOpen()) {
                    Query<Tache> queryFallback = session.createNamedQuery("findTachesPrixSuperieur1000", Tache.class);
                    list = queryFallback.list();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return list;
    }

    public List<Tache> findTachesPrixSuperieurs1000() {
        return findTachesPrixSuperieur1000();
    }

    /**
     * Afficher les tâches dont le prix est supérieur à 1000 DH
     */
    public void afficherTachesPrixSuperieur1000() {
        System.out.println("Liste des tâches dont le prix est supérieur à 1000 DH (Requête nommée) :");
        List<Tache> list = findTachesPrixSuperieur1000();
        if (list == null || list.isEmpty()) {
            System.out.println("Aucune tâche trouvée avec un prix > 1000 DH.");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        System.out.printf("%-4s %-20s %-12s %-12s %-10s%n", "Num", "Nom", "Date Début", "Date Fin", "Prix");
        for (Tache t : list) {
            String debut = t.getDateDebut() != null ? sdf.format(t.getDateDebut()) : "";
            String fin = t.getDateFin() != null ? sdf.format(t.getDateFin()) : "";
            String prixStr = (t.getPrix() == (int) t.getPrix())
                    ? ((int) t.getPrix() + " DH")
                    : (t.getPrix() + " DH");
            System.out.printf("%-4d %-20s %-12s %-12s %-10s%n",
                    t.getId(),
                    t.getNom(),
                    debut,
                    fin,
                    prixStr
            );
        }
    }

    public void afficherTachesPrixSuperieurs1000() {
        afficherTachesPrixSuperieur1000();
    }

    /**
     * Récupérer les tâches réalisées entre deux dates
     */
    public List<EmployeTache> findEmployeTachesEntreDeuxDates(Date dateDebut, Date dateFin) {
        Session session = null;
        List<EmployeTache> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<EmployeTache> query = session.createQuery(
                    "select et from EmployeTache et join fetch et.tache t " +
                    "where et.dateDebutReelle >= :d1 and et.dateFinReelle <= :d2 " +
                    "order by et.dateDebutReelle",
                    EmployeTache.class
            );
            query.setParameter("d1", dateDebut);
            query.setParameter("d2", dateFin);
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

    public List<Tache> findTachesRealiseesEntreDeuxDates(Date dateDebut, Date dateFin) {
        Session session = null;
        List<Tache> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Tache> query = session.createQuery(
                    "select distinct et.tache from EmployeTache et " +
                    "where et.dateDebutReelle >= :d1 and et.dateFinReelle <= :d2",
                    Tache.class
            );
            query.setParameter("d1", dateDebut);
            query.setParameter("d2", dateFin);
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

    public List<Tache> findTachesEntreDeuxDates(Date dateDebut, Date dateFin) {
        return findTachesRealiseesEntreDeuxDates(dateDebut, dateFin);
    }

    /**
     * Afficher les tâches réalisées entre deux dates
     */
    public void afficherTachesRealiseesEntreDeuxDates(Date dateDebut, Date dateFin) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("Tâches réalisées entre le " + sdf.format(dateDebut) + " et le " + sdf.format(dateFin) + " :");

        List<EmployeTache> list = findEmployeTachesEntreDeuxDates(dateDebut, dateFin);
        if (list == null || list.isEmpty()) {
            System.out.println("Aucune tâche réalisée trouvée dans cette période.");
            return;
        }

        System.out.printf("%-4s %-20s %-15s %-20s %-20s%n", "Num", "Nom de la tâche", "Employé", "Date Début Réelle", "Date Fin Réelle");
        for (EmployeTache et : list) {
            String debut = et.getDateDebutReelle() != null ? sdf.format(et.getDateDebutReelle()) : "";
            String fin = et.getDateFinReelle() != null ? sdf.format(et.getDateFinReelle()) : "";
            String nomEmp = et.getEmploye() != null ? (et.getEmploye().getNom() + " " + et.getEmploye().getPrenom()) : "";
            System.out.printf("%-4d %-20s %-15s %-20s %-20s%n",
                    et.getTache().getId(),
                    et.getTache().getNom(),
                    nomEmp,
                    debut,
                    fin
            );
        }
    }

    public void afficherTachesEntreDeuxDates(Date dateDebut, Date dateFin) {
        afficherTachesRealiseesEntreDeuxDates(dateDebut, dateFin);
    }
}
