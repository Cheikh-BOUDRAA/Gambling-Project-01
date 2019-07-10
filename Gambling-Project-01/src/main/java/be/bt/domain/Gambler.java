package be.bt.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Gamblers")
public class Gambler {

    //@JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String username;


    //Prénom
    private String firstName;

    //Nom
    private String lastName;


    // Date de naissance
    LocalDate birthDate;


//    @JsonIgnore
    private String password;

    private BigDecimal solde;

    private boolean enabled;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(joinColumns = @JoinColumn(name = "gambler_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @JsonIgnore
    @OneToMany(mappedBy = "gambler", fetch = FetchType.LAZY)
    private Set<Drawing> drawings = new HashSet<>();

    public Gambler() {
    }

    public Gambler(String username, String firstName, String lastName, LocalDate birthDate, String password,
                   BigDecimal solde, boolean enabled, Set<Role> roles) {
        super();
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.password = password;
        this.solde = solde;
        this.enabled = enabled;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BigDecimal getSolde() {
        return solde;
    }

    public void setSolde(BigDecimal solde) {
        this.solde = solde;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "Gambler{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate +
                ", password='" + password + '\'' +
                ", solde=" + solde +
                ", enabled=" + enabled +
//                ", roles=" + roles +
                '}';
    }
}