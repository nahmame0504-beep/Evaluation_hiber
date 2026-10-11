package ma.projet.service;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.EmployeTachePK;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;

public class EmployeTacheService implements IDao<EmployeTache> {

    @Override
    public boolean create(EmployeTache o) {
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
    public boolean update(EmployeTache o) {
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
    public boolean delete(EmployeTache o) {
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
    public EmployeTache findById(int id) {
        // La clé primaire étant composite, cette méthode retourne null par défaut
        return null;
    }

    public EmployeTache findById(EmployeTachePK pk) {
        Session session = null;
        EmployeTache et = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            et = session.get(EmployeTache.class, pk);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return et;
    }

    public EmployeTache findById(int employeId, int tacheId) {
        return findById(new EmployeTachePK(employeId, tacheId));
    }

    @Override
    public List<EmployeTache> findAll() {
        Session session = null;
        List<EmployeTache> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            list = session.createQuery("from EmployeTache", EmployeTache.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return list;
    }

    public List<EmployeTache> findByEmploye(int employeId) {
        Session session = null;
        List<EmployeTache> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<EmployeTache> query = session.createQuery(
                    "from EmployeTache et join fetch et.tache where et.employe.id = :empId",
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

    public List<EmployeTache> findByTache(int tacheId) {
        Session session = null;
        List<EmployeTache> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<EmployeTache> query = session.createQuery(
                    "from EmployeTache et join fetch et.employe where et.tache.id = :tId",
                    EmployeTache.class
            );
            query.setParameter("tId", tacheId);
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

    public List<EmployeTache> findByProjet(int projetId) {
        Session session = null;
        List<EmployeTache> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<EmployeTache> query = session.createQuery(
                    "select et from EmployeTache et join fetch et.tache t where t.projet.id = :pId",
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
}
