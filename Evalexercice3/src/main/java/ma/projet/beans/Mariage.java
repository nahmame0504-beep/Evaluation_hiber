package ma.projet.beans;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "mariage")
@NamedNativeQueries({
    @NamedNativeQuery(
        name = "Mariage.countEnfantsFemmeNative",
        query = "SELECT COALESCE(SUM(m.nbrEnfant), 0) FROM mariage m WHERE m.femme_id = :femmeId AND m.dateDebut BETWEEN :dateDebut AND :dateFin"
    )
})
public class Mariage implements Serializable {

    @EmbeddedId
    private MariagePK pk;

    @ManyToOne
    @MapsId("homme")
    @JoinColumn(name = "homme_id")
    private Homme homme;

    @ManyToOne
    @MapsId("femme")
    @JoinColumn(name = "femme_id")
    private Femme femme;

    @Temporal(TemporalType.DATE)
    @Column(name = "dateFin")
    private Date dateFin;

    @Column(name = "nbrEnfant")
    private int nbrEnfant;

    public Mariage() {
        this.pk = new MariagePK();
    }

    public Mariage(Homme homme, Femme femme, Date dateDebut, Date dateFin, int nbrEnfant) {
        this.homme = homme;
        this.femme = femme;
        this.dateFin = dateFin;
        this.nbrEnfant = nbrEnfant;
        this.pk = new MariagePK(
                homme != null ? homme.getId() : 0,
                femme != null ? femme.getId() : 0,
                dateDebut
        );
    }

    public Mariage(MariagePK pk, Date dateFin, int nbrEnfant) {
        this.pk = pk;
        this.dateFin = dateFin;
        this.nbrEnfant = nbrEnfant;
    }

    public MariagePK getPk() {
        return pk;
    }

    public void setPk(MariagePK pk) {
        this.pk = pk;
    }

    public Homme getHomme() {
        return homme;
    }

    public void setHomme(Homme homme) {
        this.homme = homme;
        if (this.pk == null) {
            this.pk = new MariagePK();
        }
        if (homme != null) {
            this.pk.setHomme(homme.getId());
        }
    }

    public Femme getFemme() {
        return femme;
    }

    public void setFemme(Femme femme) {
        this.femme = femme;
        if (this.pk == null) {
            this.pk = new MariagePK();
        }
        if (femme != null) {
            this.pk.setFemme(femme.getId());
        }
    }

    public Date getDateDebut() {
        return pk != null ? pk.getDateDebut() : null;
    }

    public void setDateDebut(Date dateDebut) {
        if (this.pk == null) {
            this.pk = new MariagePK();
        }
        this.pk.setDateDebut(dateDebut);
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public int getNbrEnfant() {
        return nbrEnfant;
    }

    public void setNbrEnfant(int nbrEnfant) {
        this.nbrEnfant = nbrEnfant;
    }

    @Override
    public String toString() {
        return "Mariage{" +
                "homme=" + (homme != null ? (homme.getNom() + " " + homme.getPrenom()) : null) +
                ", femme=" + (femme != null ? (femme.getNom() + " " + femme.getPrenom()) : null) +
                ", dateDebut=" + getDateDebut() +
                ", dateFin=" + dateFin +
                ", nbrEnfant=" + nbrEnfant +
                '}';
    }
}
