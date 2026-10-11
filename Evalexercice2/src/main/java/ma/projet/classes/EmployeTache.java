package ma.projet.classes;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "employe_tache")
public class EmployeTache implements Serializable {

    @EmbeddedId
    private EmployeTachePK pk;

    @Temporal(TemporalType.DATE)
    private Date dateDebutReelle;

    @Temporal(TemporalType.DATE)
    private Date dateFinReelle;

    @ManyToOne
    @MapsId("employe")
    @JoinColumn(name = "employe_id")
    private Employe employe;

    @ManyToOne
    @MapsId("tache")
    @JoinColumn(name = "tache_id")
    private Tache tache;

    public EmployeTache() {
        this.pk = new EmployeTachePK();
    }

    public EmployeTache(Employe employe, Tache tache, Date dateDebutReelle, Date dateFinReelle) {
        this.employe = employe;
        this.tache = tache;
        this.dateDebutReelle = dateDebutReelle;
        this.dateFinReelle = dateFinReelle;
        this.pk = new EmployeTachePK(
                employe != null ? employe.getId() : 0,
                tache != null ? tache.getId() : 0
        );
    }

    public EmployeTache(EmployeTachePK pk, Date dateDebutReelle, Date dateFinReelle) {
        this.pk = pk;
        this.dateDebutReelle = dateDebutReelle;
        this.dateFinReelle = dateFinReelle;
    }

    public EmployeTachePK getPk() {
        return pk;
    }

    public void setPk(EmployeTachePK pk) {
        this.pk = pk;
    }

    public Date getDateDebutReelle() {
        return dateDebutReelle;
    }

    public void setDateDebutReelle(Date dateDebutReelle) {
        this.dateDebutReelle = dateDebutReelle;
    }

    public Date getDateFinReelle() {
        return dateFinReelle;
    }

    public void setDateFinReelle(Date dateFinReelle) {
        this.dateFinReelle = dateFinReelle;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
        if (this.pk == null) {
            this.pk = new EmployeTachePK();
        }
        if (employe != null) {
            this.pk.setEmploye(employe.getId());
        }
    }

    public Tache getTache() {
        return tache;
    }

    public void setTache(Tache tache) {
        this.tache = tache;
        if (this.pk == null) {
            this.pk = new EmployeTachePK();
        }
        if (tache != null) {
            this.pk.setTache(tache.getId());
        }
    }

    @Override
    public String toString() {
        return "EmployeTache{" +
                "employe=" + (employe != null ? employe.getNom() : null) +
                ", tache=" + (tache != null ? tache.getNom() : null) +
                ", dateDebutReelle=" + dateDebutReelle +
                ", dateFinReelle=" + dateFinReelle +
                '}';
    }
}
