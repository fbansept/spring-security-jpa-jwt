package edu.fbansept.springsecurity.model;

import org.hibernate.annotations.FetchMode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "utilisateur")
@EntityListeners(AuditingEntityListener.class)
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String pseudo;

    private String password;

    private boolean actif;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_utilisateur")
    Set<Role> listeRole;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public Set<Role> getListeRole() {
        return listeRole;
    }

    public void setListeRole(Set<Role> listeRole) {
        this.listeRole = listeRole;
    }
}
