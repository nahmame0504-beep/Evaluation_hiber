package ma.projet.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;

public class HommeService implements IDao<Homme> {

    @Override
    public boolean create(Homme o) {
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
    public boolean update(Homme o) {
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
    public boolean delete(Homme o) {
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
    public Homme findById(int id) {
        Session session = null;
        Homme homme = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            homme = session.get(Homme.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return homme;
    }

    @Override
    public List<Homme> findAll() {
        Session session = null;
        List<Homme> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            list = session.createQuery("from Homme", Homme.class).list();
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
     * Méthode pour afficher et retourner les épouses d’un homme entre deux dates.
     */
    public List<Femme> getEpousesEntreDates(int hommeId, Date dateDebut, Date dateFin) {
        Session session = null;
        List<Femme> epouses = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Femme> query = session.createQuery(
                    "select distinct m.femme from Mariage m " +
                    "where m.homme.id = :hommeId and m.pk.dateDebut between :d1 and :d2",
                    Femme.class
            );
            query.setParameter("hommeId", hommeId);
            query.setParameter("d1", dateDebut);
            query.setParameter("d2", dateFin);
            epouses = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return epouses;
    }

    public List<Femme> getEpousesEntreDates(Homme homme, Date dateDebut, Date dateFin) {
        if (homme == null) {
            return new ArrayList<>();
        }
        return getEpousesEntreDates(homme.getId(), dateDebut, dateFin);
    }

    public void afficherEpousesEntreDates(Homme homme, Date dateDebut, Date dateFin) {
        if (homme == null) {
            System.out.println("Homme invalide.");
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("Epouses de " + homme.getNom() + " " + homme.getPrenom() +
                " entre " + sdf.format(dateDebut) + " et " + sdf.format(dateFin) + " :");
        List<Femme> list = getEpousesEntreDates(homme, dateDebut, dateFin);
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Femme f = list.get(i);
                System.out.println((i + 1) + ". " + f.getPrenom() + " " + f.getNom() +
                        (f.getDateNaissance() != null ? " (Date de naissance : " + sdf.format(f.getDateNaissance()) + ")" : ""));
            }
        } else {
            System.out.println("Aucune épouse trouvée pour cette période.");
        }
    }

    /**
     * Méthode utilisant l’API Criteria pour retourner le nombre d’hommes mariés à quatre femmes entre deux dates.
     */
    public long countHommesMariesAQuatreFemmes(Date dateDebut, Date dateFin) {
        Session session = null;
        long count = 0;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Homme> cq = cb.createQuery(Homme.class);
            Root<Mariage> root = cq.from(Mariage.class);

            Predicate datePredicate = cb.between(root.get("pk").get("dateDebut"), dateDebut, dateFin);

            cq.select(root.get("homme"))
              .where(datePredicate)
              .groupBy(root.get("homme"))
              .having(cb.greaterThanOrEqualTo(cb.countDistinct(root.get("femme")), 4L));

            List<Homme> hommes = session.createQuery(cq).getResultList();
            count = (hommes != null) ? hommes.size() : 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return count;
    }

    public void afficherHommesMariesAQuatreFemmes(Date dateDebut, Date dateFin) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        long count = countHommesMariesAQuatreFemmes(dateDebut, dateFin);
        System.out.println("Nombre d'hommes mariés à quatre femmes entre " +
                sdf.format(dateDebut) + " et " + sdf.format(dateFin) + " : " + count);
    }

    /**
     * Méthode pour afficher les mariages d’un homme donné avec les détails
     * (femme, dates, nombre d’enfants).
     */
    public void afficherMariages(Homme homme) {
        if (homme == null) {
            System.out.println("Homme invalide.");
            return;
        }
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Mariage> query = session.createQuery(
                    "from Mariage m join fetch m.femme where m.homme.id = :hId order by m.pk.dateDebut asc",
                    Mariage.class
            );
            query.setParameter("hId", homme.getId());
            List<Mariage> mariages = query.list();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            System.out.println("Nom : " + homme.getNom() + " " + homme.getPrenom());

            List<Mariage> enCours = new ArrayList<>();
            List<Mariage> echoues = new ArrayList<>();

            for (Mariage m : mariages) {
                if (m.getDateFin() == null) {
                    enCours.add(m);
                } else {
                    echoues.add(m);
                }
            }

            System.out.println("Mariages En Cours :");
            if (enCours.isEmpty()) {
                System.out.println("Aucun mariage en cours.");
            } else {
                for (int i = 0; i < enCours.size(); i++) {
                    Mariage m = enCours.get(i);
                    Femme f = m.getFemme();
                    String nomComplet = (f != null ? (f.getPrenom() + " " + f.getNom()) : "Inconnue");
                    System.out.printf("%d. Femme : %-15s Date Début : %-10s    Nbr Enfants : %d%n",
                            (i + 1),
                            nomComplet,
                            (m.getDateDebut() != null ? sdf.format(m.getDateDebut()) : "N/A"),
                            m.getNbrEnfant());
                }
            }

            System.out.println();
            System.out.println("Mariages échoués :");
            if (echoues.isEmpty()) {
                System.out.println("Aucun mariage échoué.");
            } else {
                for (int i = 0; i < echoues.size(); i++) {
                    Mariage m = echoues.get(i);
                    Femme f = m.getFemme();
                    String nomComplet = (f != null ? (f.getPrenom() + " " + f.getNom()) : "Inconnue");
                    System.out.printf("%d. Femme : %-15s Date Début : %-10s%n",
                            (i + 1),
                            nomComplet,
                            (m.getDateDebut() != null ? sdf.format(m.getDateDebut()) : "N/A"));
                    System.out.printf("   Date Fin : %-10s    Nbr Enfants : %d%n",
                            (m.getDateFin() != null ? sdf.format(m.getDateFin()) : "N/A"),
                            m.getNbrEnfant());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void afficherMariages(int hommeId) {
        Homme h = findById(hommeId);
        afficherMariages(h);
    }
}
