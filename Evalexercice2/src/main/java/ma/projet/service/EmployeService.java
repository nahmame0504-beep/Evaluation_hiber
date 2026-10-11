package ma.projet.service;

import java.text.SimpleDateFormat;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ma.projet.classes.Employe;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;

public class EmployeService implements IDao<Employe> {

    @Override
    public boolean create(Employe o) {
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
    public boolean update(Employe o) {
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
    public boolean delete(Employe o) {
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
    public Employe findById(int id) {
        Session session = null;
        Employe emp = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            emp = session.get(Employe.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return emp;
    }

    @Override
    public List<Employe> findAll() {
        Session session = null;
        List<Employe> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            list = session.createQuery("from Employe", Employe.class).list();
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
     * Récupérer la liste des tâches réalisées par un employé
     */
    public List<EmployeTache> getTachesRealisees(Employe e) {
        if (e == null) return null;
        return getTachesRealisees(e.getId());
    }

    public List<EmployeTache> getTachesRealisees(int employeId) {
        Session session = null;
        List<EmployeTache> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<EmployeTache> query = session.createQuery(
                    "from EmployeTache et join fetch et.tache t where et.employe.id = :empId",
                    EmployeTache.class
            );
            query.setParameter("empId", employeId);
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
     * Afficher la liste des tâches réalisées par un employé
     */
    public void afficherTachesRealisees(Employe e) {
        if (e == null) {
            System.out.println("Employé introuvable !");
            return;
        }
        afficherTachesRealisees(e.getId());
    }

    public void afficherTachesRealisees(int employeId) {
        Employe emp = findById(employeId);
        if (emp == null) {
            System.out.println("Employé " + employeId + " introuvable !");
            return;
        }

        System.out.println("Employé : " + emp.getNom() + " " + emp.getPrenom() + " (Tél: " + emp.getTelephone() + ")");
        System.out.println("Liste des tâches réalisées :");

        List<EmployeTache> list = getTachesRealisees(employeId);
        if (list == null || list.isEmpty()) {
            System.out.println("Aucune tâche réalisée par cet employé.");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        System.out.printf("%-4s %-15s %-20s %-20s%n", "Num", "Nom", "Date Début Réelle", "Date Fin Réelle");
        for (EmployeTache et : list) {
            String debut = et.getDateDebutReelle() != null ? sdf.format(et.getDateDebutReelle()) : "";
            String fin = et.getDateFinReelle() != null ? sdf.format(et.getDateFinReelle()) : "";
            System.out.printf("%-4d %-15s %-20s %-20s%n",
                    et.getTache().getId(),
                    et.getTache().getNom(),
                    debut,
                    fin
            );
        }
    }

    /**
     * Récupérer la liste des projets gérés par un employé
     */
    public List<Projet> getProjetsGeres(Employe e) {
        if (e == null) return null;
        return getProjetsGeres(e.getId());
    }

    public List<Projet> getProjetsGeres(int employeId) {
        Session session = null;
        List<Projet> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Projet> query = session.createQuery(
                    "from Projet p where p.chefProjet.id = :empId",
                    Projet.class
            );
            query.setParameter("empId", employeId);
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
     * Afficher la liste des projets gérés par un employé
     */
    public void afficherProjetsGeres(Employe e) {
        if (e == null) {
            System.out.println("Employé introuvable !");
            return;
        }
        afficherProjetsGeres(e.getId());
    }

    public void afficherProjetsGeres(int employeId) {
        Employe emp = findById(employeId);
        if (emp == null) {
            System.out.println("Employé " + employeId + " introuvable !");
            return;
        }

        System.out.println("Employé : " + emp.getNom() + " " + emp.getPrenom());
        System.out.println("Liste des projets gérés :");

        List<Projet> list = getProjetsGeres(employeId);
        if (list == null || list.isEmpty()) {
            System.out.println("Aucun projet géré par cet employé.");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        System.out.printf("%-4s %-25s %-15s %-15s%n", "ID", "Nom", "Date Début", "Date Fin");
        for (Projet p : list) {
            String debut = p.getDateDebut() != null ? sdf.format(p.getDateDebut()) : "";
            String fin = p.getDateFin() != null ? sdf.format(p.getDateFin()) : "";
            System.out.printf("%-4d %-25s %-15s %-15s%n",
                    p.getId(),
                    p.getNom(),
                    debut,
                    fin
            );
        }
    }
}
