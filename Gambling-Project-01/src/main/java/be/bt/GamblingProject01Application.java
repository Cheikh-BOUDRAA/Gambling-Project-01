package be.bt;

import be.bt.domain.Gambler;
import be.bt.domain.Role;
import be.bt.repository.GamblerRepository;
import be.bt.rest.GamblerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.config.EnableEntityLinks;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.HashSet;

@SpringBootApplication
//@EnableSpringDataWebSupport  //
@EnableEntityLinks
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class GamblingProject01Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(GamblerResource.class);

    public static void main(String[] args) {
        SpringApplication.run(GamblingProject01Application.class, args);
    }

    @Bean
    BCryptPasswordEncoder encoder() {
        return 	new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner runIt(GamblerRepository clientsRepo) {
        return args -> {
            try {


//    public Gambler(String username, String firstName, String lastName, LocalDate birthDate, String password,
//                        BigDecimal solde, boolean enabled, Set<Role> roles) {

                Gambler gambler1 = new Gambler("gambler1@gmail.com",
                        "Gambler1",
                        "GAMBLER1",
                        LocalDate.of(1975, Month.SEPTEMBER, 30),
                        encoder().encode("1234"),
                        new BigDecimal("100"), true, null);

////                    Gambler gambler1 = new Gambler("gambler1@gmail.com",
////                            encoder().encode("1234"),
////                            new BigDecimal("100"), true, null);
//
                Role role1 = new Role("USER", new HashSet<>(Arrays.asList(gambler1)));
                gambler1.setRoles(new HashSet<>(Arrays.asList(role1)));
//
////                Gambler gambler2 = new Gambler("user2", encoder().encode("password"), true, null,new BigDecimal("1000"));
////                    Gambler gambler2 = new Gambler("gambler2@hotmail.com",
////                            encoder().encode("1234"),
////                            new BigDecimal("100"), true, null);
//
                Gambler gambler2 = new Gambler("gambler2@hotmail.com",
                        "Gambler2",
                        "GAMBLER2",
                        LocalDate.of(2018, Month.JANUARY, 15),
                        encoder().encode("1234"),
                        new BigDecimal("100"), true, null);

                Role role2 = new Role("ADMIN", new HashSet<>(Arrays.asList(gambler2)));
                gambler2.setRoles(new HashSet<>(Arrays.asList(role2)));

                clientsRepo.saveAll(Arrays.asList(gambler1, gambler2));

                System.out.println("Fin de l'initialisation par CommandLineRunner ...");
            } catch (Exception ex) {
                LOGGER.error("Exception rencontrée lors de l'initialisation par CommandLineRunner : " + ex);
            }
        };
    }

}
