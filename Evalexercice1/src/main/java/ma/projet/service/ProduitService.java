package ma.projet.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ma.projet.classes.Categorie;
import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommandeProduit;
import ma.projet.classes.Produit;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;

public class ProduitService implements IDao<Produit> {

    @Override
    public boolean create(Produit o) {
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
    public boolean update(Produit o) {
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
    public boolean delete(Produit o) {
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
    public Produit findById(int id) {
        Session session = null;
        Produit p = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            p = session.get(Produit.class, id);
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
    public List<Produit> findAll() {
        Session session = null;
        List<Produit> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            list = session.createQuery("from Produit", Produit.class).list();
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
     * Récupérer les produits par catégorie
     */
    public List<Produit> findByCategorie(Categorie c) {
        Session session = null;
        List<Produit> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Produit> query = session.createQuery(
                    "from Produit p where p.categorie.id = :catId",
                    Produit.class
            );
            query.setParameter("catId", c.getId());
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
     * Afficher la liste des produits par catégorie
     */
    public void afficherProduitsParCategorie(Categorie c) {
        if (c == null) {
            System.out.println("Catégorie introuvable !");
            return;
        }
        List<Produit> list = findByCategorie(c);
        System.out.println("Catégorie : " + c.getLibelle() + " (" + c.getCode() + ")");
        System.out.println("Liste des produits :");
        if (list == null || list.isEmpty()) {
            System.out.println("Aucun produit dans cette catégorie.");
            return;
        }
        System.out.printf("%-12s %-10s%n", "Référence", "Prix");
        for (Produit p : list) {
            String prixStr = (p.getPrix() == (int) p.getPrix())
                    ? ((int) p.getPrix() + " DH")
                    : (p.getPrix() + " DH");
            System.out.printf("%-12s %-10s%n", p.getReference(), prixStr);
        }
    }

    /**
     * Récupérer les produits commandés entre deux dates
     */
    public List<Produit> findProduitsEntreDates(Date dateDebut, Date dateFin) {
        Session session = null;
        List<Produit> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Produit> query = session.createQuery(
                    "select distinct lcp.produit from LigneCommandeProduit lcp " +
                    "where lcp.commande.date between :d1 and :d2",
                    Produit.class
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

    /**
     * Afficher les produits commandés entre deux dates
     */
    public void afficherProduitsEntreDates(Date dateDebut, Date dateFin) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("Produits commandés entre " + sdf.format(dateDebut) + " et " + sdf.format(dateFin) + " :");
        List<Produit> list = findProduitsEntreDates(dateDebut, dateFin);
        if (list == null || list.isEmpty()) {
            System.out.println("Aucun produit commandé dans cette période.");
            return;
        }
        System.out.printf("%-12s %-10s%n", "Référence", "Prix");
        for (Produit p : list) {
            String prixStr = (p.getPrix() == (int) p.getPrix())
                    ? ((int) p.getPrix() + " DH")
                    : (p.getPrix() + " DH");
            System.out.printf("%-12s %-10s%n", p.getReference(), prixStr);
        }
    }

    /**
     * Afficher les produits commandés dans une commande donnée
     */
    public void afficherProduitsParCommande(Commande c) {
        if (c == null) {
            System.out.println("Commande introuvable !");
            return;
        }
        afficherProduitsParCommande(c.getId());
    }

    /**
     * Afficher les produits commandés dans une commande donnée par son ID
     */
    public void afficherProduitsParCommande(int commandeId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Commande commande = session.get(Commande.class, commandeId);
            if (commande == null) {
                System.out.println("Commande : " + commandeId + " introuvable !");
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH);
            String dateFormatted = "";
            if (commande.getDate() != null) {
                String rawDate = sdf.format(commande.getDate());
                String[] parts = rawDate.split(" ");
                if (parts.length == 3) {
                    dateFormatted = parts[0] + " " + Character.toUpperCase(parts[1].charAt(0)) + parts[1].substring(1) + " " + parts[2];
                } else {
                    dateFormatted = rawDate;
                }
            }

            System.out.printf("Commande : %-4d     Date : %s%n", commande.getId(), dateFormatted);
            System.out.println("Liste des produits :");
            System.out.printf("%-12s %-8s %-10s%n", "Référence", "Prix", "Quantité");

            Query<LigneCommandeProduit> query = session.createQuery(
                    "from LigneCommandeProduit lcp join fetch lcp.produit where lcp.commande.id = :cId",
                    LigneCommandeProduit.class
            );
            query.setParameter("cId", commandeId);
            List<LigneCommandeProduit> lignes = query.list();

            if (lignes == null || lignes.isEmpty()) {
                System.out.println("Aucun produit dans cette commande.");
                return;
            }

            for (LigneCommandeProduit lcp : lignes) {
                Produit p = lcp.getProduit();
                String prixStr = (p.getPrix() == (int) p.getPrix())
                        ? ((int) p.getPrix() + " DH")
                        : (p.getPrix() + " DH");
                System.out.printf("%-12s %-8s %-10d%n",
                        p.getReference(),
                        prixStr,
                        lcp.getQuantite()
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Récupérer la liste des produits dont le prix est supérieur à 100 DH via NamedQuery
     */
    public List<Produit> findProduitsPrixSuperieurs100() {
        Session session = null;
        List<Produit> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Produit> query = session.createNamedQuery("findProduitsPrixSuperieur100", Produit.class);
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

    // Alias pour compatibilité
    public List<Produit> findProduitsSuperieur100() {
        return findProduitsPrixSuperieurs100();
    }

    /**
     * Afficher la liste des produits dont le prix est supérieur à 100 DH
     */
    public void afficherProduitsPrixSuperieurs100() {
        System.out.println("Liste des produits dont le prix est supérieur à 100 DH :");
        List<Produit> list = findProduitsPrixSuperieurs100();
        if (list == null || list.isEmpty()) {
            System.out.println("Aucun produit trouvé.");
            return;
        }
        System.out.printf("%-12s %-10s%n", "Référence", "Prix");
        for (Produit p : list) {
            String prixStr = (p.getPrix() == (int) p.getPrix())
                    ? ((int) p.getPrix() + " DH")
                    : (p.getPrix() + " DH");
            System.out.printf("%-12s %-10s%n", p.getReference(), prixStr);
        }
    }
}
