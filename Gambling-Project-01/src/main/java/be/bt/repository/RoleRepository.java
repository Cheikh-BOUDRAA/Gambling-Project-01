package be.bt.repository;

import be.bt.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {

    Role findByRole(String role);

}
