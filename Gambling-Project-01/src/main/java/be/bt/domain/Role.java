package be.bt.domain;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String role;

    // @JsonIgnore
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<Gambler> gamblers;

    public Role() {
    }

    public Role(String role, Set<Gambler> gamblers) {
        this.role = role;
        this.gamblers = gamblers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<Gambler> getGamblers() {
        return gamblers;
    }

    public void setGamblers(Set<Gambler> gamblers) {
        this.gamblers = gamblers;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", role='" + role + '\'' +
                //", gamblers=" + gamblers.toString() +
                '}';
    }
}
