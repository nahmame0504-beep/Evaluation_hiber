package ma.projet.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class MariagePK implements Serializable {

    @Column(name = "homme_id")
    private int homme;

    @Column(name = "femme_id")
    private int femme;

    @Temporal(TemporalType.DATE)
    @Column(name = "dateDebut")
    private Date dateDebut;

    public MariagePK() {
    }

    public MariagePK(int homme, int femme, Date dateDebut) {
        this.homme = homme;
        this.femme = femme;
        this.dateDebut = dateDebut;
    }

    public int getHomme() {
        return homme;
    }

    public void setHomme(int homme) {
        this.homme = homme;
    }

    public int getFemme() {
        return femme;
    }

    public void setFemme(int femme) {
        this.femme = femme;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MariagePK that = (MariagePK) o;
        return homme == that.homme &&
                femme == that.femme &&
                Objects.equals(dateDebut, that.dateDebut);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homme, femme, dateDebut);
    }

    @Override
    public String toString() {
        return "MariagePK{" +
                "homme=" + homme +
                ", femme=" + femme +
                ", dateDebut=" + dateDebut +
                '}';
    }
}
