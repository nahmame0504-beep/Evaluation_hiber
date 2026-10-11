package ma.projet.service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;

public class ProjetService implements IDao<Projet> {

    @Override
    public boolean create(Projet o) {
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
    public boolean update(Projet o) {
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
    public boolean delete(Projet o) {
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
    public Projet findById(int id) {
        Session session = null;
        Projet p = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            p = session.get(Projet.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return p;
    }

    @Override
    public List<Projet> findAll() {
        Session session = null;
        List<Projet> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            list = session.createQuery("from Projet", Projet.class).list();
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
     * Récupérer la liste des tâches planifiées pour un projet
     */
    public List<Tache> getTachesPlanifiees(Projet p) {
        if (p == null) return null;
        return getTachesPlanifiees(p.getId());
    }

    public List<Tache> getTachesPlanifiees(int projetId) {
        Session session = null;
        List<Tache> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Tache> query = session.createQuery(
                    "from Tache t where t.projet.id = :pId order by t.id",
                    Tache.class
            );
            query.setParameter("pId", projetId);
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

    /**
     * Afficher la liste des tâches planifiées pour un projet
     */
    public void afficherTachesPlanifiees(Projet p) {
        if (p == null) {
            System.out.println("Projet introuvable !");
            return;
        }
        afficherTachesPlanifiees(p.getId());
    }

    public void afficherTachesPlanifiees(int projetId) {
        Projet projet = findById(projetId);
        if (projet == null) {
            System.out.println("Projet " + projetId + " introuvable !");
            return;
        }

        System.out.printf("Projet : %-4d Nom : %-20s%n", projet.getId(), projet.getNom());
        System.out.println("Liste des tâches planifiées :");

        List<Tache> list = getTachesPlanifiees(projetId);
        if (list == null || list.isEmpty()) {
            System.out.println("Aucune tâche planifiée pour ce projet.");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        System.out.printf("%-4s %-15s %-15s %-15s %-10s%n", "Num", "Nom", "Date Début", "Date Fin", "Prix");
        for (Tache t : list) {
            String debut = t.getDateDebut() != null ? sdf.format(t.getDateDebut()) : "";
            String fin = t.getDateFin() != null ? sdf.format(t.getDateFin()) : "";
            String prixStr = (t.getPrix() == (int) t.getPrix())
                    ? ((int) t.getPrix() + " DH")
                    : (t.getPrix() + " DH");
            System.out.printf("%-4d %-15s %-15s %-15s %-10s%n",
                    t.getId(),
                    t.getNom(),
                    debut,
                    fin,
                    prixStr
            );
        }
    }

    /**
     * Récupérer la liste des tâches réalisées avec les dates réelles pour un projet
     */
    public List<EmployeTache> getTachesRealisees(Projet p) {
        if (p == null) return null;
        return getTachesRealisees(p.getId());
    }

    public List<EmployeTache> getTachesRealisees(int projetId) {
        Session session = null;
        List<EmployeTache> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<EmployeTache> query = session.createQuery(
                    "select et from EmployeTache et join fetch et.tache t where t.projet.id = :pId order by t.id",
                    EmployeTache.class
            );
            query.setParameter("pId", projetId);
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

    /**
     * Afficher la liste des tâches réalisées avec les dates réelles
     * Format attendu dans l'exercice :
     * Projet : 4      Nom : Gestion de stock     Date début : 14 Janvier 2013
     * Liste des tâches:
     * Num Nom            Date Début Réelle   Date Fin Réelle
     * 12  Analyse        10/02/2013          20/02/2013
     * 13  Conception     10/03/2013          15/03/2013
     * 14  Développement  10/04/2013          25/04/2013
     */
    public void afficherTachesRealisees(Projet p) {
        if (p == null) {
            System.out.println("Projet introuvable !");
            return;
        }
        afficherTachesRealisees(p.getId());
    }

    public void afficherTachesRealisees(int projetId) {
        Projet projet = findById(projetId);
        if (projet == null) {
            System.out.println("Projet " + projetId + " introuvable !");
            return;
        }

        SimpleDateFormat sdfHeader = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH);
        String dateDebutFormatted = "";
        if (projet.getDateDebut() != null) {
            String rawDate = sdfHeader.format(projet.getDateDebut());
            String[] parts = rawDate.split(" ");
            if (parts.length == 3) {
                dateDebutFormatted = parts[0] + " " + Character.toUpperCase(parts[1].charAt(0)) + parts[1].substring(1) + " " + parts[2];
            } else {
                dateDebutFormatted = rawDate;
            }
        }

        System.out.printf("Projet : %-6d Nom : %-20s Date début : %s%n",
                projet.getId(),
                projet.getNom(),
                dateDebutFormatted
        );
        System.out.println("Liste des tâches:");

        List<EmployeTache> list = getTachesRealisees(projetId);
        if (list == null || list.isEmpty()) {
            System.out.println("Aucune tâche réalisée pour ce projet.");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        System.out.printf("%-3s %-14s %-19s %-15s%n", "Num", "Nom", "Date Début Réelle", "Date Fin Réelle");
        for (EmployeTache et : list) {
            Tache t = et.getTache();
            String debut = et.getDateDebutReelle() != null ? sdf.format(et.getDateDebutReelle()) : "";
            String fin = et.getDateFinReelle() != null ? sdf.format(et.getDateFinReelle()) : "";
            System.out.printf("%-3d %-14s %-19s %-15s%n",
                    t.getId(),
                    t.getNom(),
                    debut,
                    fin
            );
        }
    }
}
