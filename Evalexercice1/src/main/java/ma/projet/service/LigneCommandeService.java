package ma.projet.service;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ma.projet.classes.LigneCommandeProduit;
import ma.projet.classes.LigneCommandeProduitPK;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;

public class LigneCommandeService implements IDao<LigneCommandeProduit> {

    @Override
    public boolean create(LigneCommandeProduit o) {
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
    public boolean update(LigneCommandeProduit o) {
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
    public boolean delete(LigneCommandeProduit o) {
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
    public LigneCommandeProduit findById(int id) {
        // La clé primaire étant composite, cette méthode retourne null par défaut
        return null;
    }

    public LigneCommandeProduit findById(LigneCommandeProduitPK pk) {
        Session session = null;
        LigneCommandeProduit lcp = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            lcp = session.get(LigneCommandeProduit.class, pk);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return lcp;
    }

    public LigneCommandeProduit findById(int commandeId, int produitId) {
        return findById(new LigneCommandeProduitPK(commandeId, produitId));
    }

    @Override
    public List<LigneCommandeProduit> findAll() {
        Session session = null;
        List<LigneCommandeProduit> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            list = session.createQuery("from LigneCommandeProduit", LigneCommandeProduit.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return list;
    }

    public List<LigneCommandeProduit> findByCommande(int commandeId) {
        Session session = null;
        List<LigneCommandeProduit> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<LigneCommandeProduit> query = session.createQuery(
                    "from LigneCommandeProduit lcp join fetch lcp.produit where lcp.commande.id = :cId",
                    LigneCommandeProduit.class
            );
            query.setParameter("cId", commandeId);
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
