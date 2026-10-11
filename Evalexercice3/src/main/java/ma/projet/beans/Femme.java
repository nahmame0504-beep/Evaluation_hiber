package ma.projet.beans;

import java.util.Date;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@DiscriminatorValue("Femme")
@NamedQueries({
    @NamedQuery(
        name = "Femme.findFemmesMarieesAuMoinsDeuxFois",
        query = "SELECT m.femme FROM Mariage m GROUP BY m.femme HAVING COUNT(m) >= 2"
    ),
    @NamedQuery(
        name = "findFemmesMarieesAuMoinsDeuxFois",
        query = "SELECT m.femme FROM Mariage m GROUP BY m.femme HAVING COUNT(m) >= 2"
    )
})
@NamedNativeQueries({
    @NamedNativeQuery(
        name = "Femme.countEnfantsEntreDates",
        query = "SELECT COALESCE(SUM(nbrEnfant), 0) FROM mariage WHERE femme_id = :femmeId AND dateDebut BETWEEN :dateDebut AND :dateFin"
    ),
    @NamedNativeQuery(
        name = "countEnfantsEntreDates",
        query = "SELECT COALESCE(SUM(nbrEnfant), 0) FROM mariage WHERE femme_id = :femmeId AND dateDebut BETWEEN :dateDebut AND :dateFin"
    )
})
public class Femme extends Personne {

    public Femme() {
        super();
    }

    public Femme(String nom, String prenom, String telephone, String adresse, Date dateNaissance) {
        super(nom, prenom, telephone, adresse, dateNaissance);
    }
}
