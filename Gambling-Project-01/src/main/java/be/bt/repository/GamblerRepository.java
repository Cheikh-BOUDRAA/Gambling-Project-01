package be.bt.repository;

import be.bt.domain.Gambler;
//import be.bt.domain.Offre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;
import java.util.stream.Stream;

public interface GamblerRepository extends JpaRepository<Gambler,Long> {

    Gambler findByUsername(String username);

    //Set<Offre> findAllByUsername(String username);

    @Query(value = "From Gambler c where c.solde = (select max(c.solde) from Gambler c)")
    Set<Gambler> clientAyantLeMeilleurSolde() ;

}
