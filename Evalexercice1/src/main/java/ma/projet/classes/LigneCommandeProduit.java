package ma.projet.classes;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name = "ligne_commande_produit")
public class LigneCommandeProduit implements Serializable {

    @EmbeddedId
    private LigneCommandeProduitPK pk;

    private int quantite;

    @ManyToOne
    @MapsId("commande")
    @JoinColumn(name = "commande_id")
    private Commande commande;

    @ManyToOne
    @MapsId("produit")
    @JoinColumn(name = "produit_id")
    private Produit produit;

    public LigneCommandeProduit() {
        this.pk = new LigneCommandeProduitPK();
    }

    public LigneCommandeProduit(Commande commande, Produit produit, int quantite) {
        this.commande = commande;
        this.produit = produit;
        this.quantite = quantite;
        this.pk = new LigneCommandeProduitPK(
                commande != null ? commande.getId() : 0,
                produit != null ? produit.getId() : 0
        );
    }

    public LigneCommandeProduit(Produit produit, Commande commande, int quantite) {
        this(commande, produit, quantite);
    }

    public LigneCommandeProduit(LigneCommandeProduitPK pk, int quantite) {
        this.pk = pk;
        this.quantite = quantite;
    }

    public LigneCommandeProduitPK getPk() {
        return pk;
    }

    public void setPk(LigneCommandeProduitPK pk) {
        this.pk = pk;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
        if (this.pk == null) {
            this.pk = new LigneCommandeProduitPK();
        }
        if (commande != null) {
            this.pk.setCommande(commande.getId());
        }
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
        if (this.pk == null) {
            this.pk = new LigneCommandeProduitPK();
        }
        if (produit != null) {
            this.pk.setProduit(produit.getId());
        }
    }

    @Override
    public String toString() {
        return "LigneCommandeProduit{" +
                "quantite=" + quantite +
                ", commande=" + (commande != null ? commande.getId() : null) +
                ", produit=" + (produit != null ? produit.getReference() : null) +
                '}';
    }
}
