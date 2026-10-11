package ma.projet.service;

import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ma.projet.beans.Mariage;
import ma.projet.beans.MariagePK;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;

public class MariageService implements IDao<Mariage> {

    @Override
    public boolean create(Mariage o) {
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
    public boolean update(Mariage o) {
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
    public boolean delete(Mariage o) {
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
    public Mariage findById(int id) {
        // La clé primaire étant composite, cette méthode retourne null par défaut
        return null;
    }

    public Mariage findById(MariagePK pk) {
        Session session = null;
        Mariage m = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            m = session.get(Mariage.class, pk);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return m;
    }

    public Mariage findById(int hommeId, int femmeId, Date dateDebut) {
        return findById(new MariagePK(hommeId, femmeId, dateDebut));
    }

    @Override
    public List<Mariage> findAll() {
        Session session = null;
        List<Mariage> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            list = session.createQuery("from Mariage m join fetch m.homme join fetch m.femme", Mariage.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return list;
    }

    public List<Mariage> findByHomme(int hommeId) {
        Session session = null;
        List<Mariage> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Mariage> query = session.createQuery(
                    "from Mariage m join fetch m.femme where m.homme.id = :hId order by m.pk.dateDebut asc",
                    Mariage.class
            );
            query.setParameter("hId", hommeId);
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

    public List<Mariage> findByFemme(int femmeId) {
        Session session = null;
        List<Mariage> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Mariage> query = session.createQuery(
                    "from Mariage m join fetch m.homme where m.femme.id = :fId order by m.pk.dateDebut asc",
                    Mariage.class
            );
            query.setParameter("fId", femmeId);
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
