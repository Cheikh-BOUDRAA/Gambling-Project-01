package be.bt.rest;

import be.bt.domain.Gambler;
import be.bt.domain.Role;
import be.bt.repository.BankRepository;
import be.bt.repository.DrawingRespository;
import be.bt.repository.GamblerRepository;
import be.bt.repository.RoleRepository;
import be.bt.service.IAuthenticationFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;


@RestController
public class GamblerResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(GamblerResource.class);

    private GamblerRepository gamblerRepository;
    private DrawingRespository drawingRespository;
    private BankRepository bankRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder encoder;
    //@Autowired
    private IAuthenticationFacade authenticationFacade;

    public GamblerResource(GamblerRepository gamblerRepository,
                           DrawingRespository drawingRespository,
                           BankRepository bankRepository,
                           RoleRepository roleRepository,
                           BCryptPasswordEncoder encoder,
                           IAuthenticationFacade authenticationFacade) {
        this.gamblerRepository = gamblerRepository;
        this.drawingRespository = drawingRespository;
        this.bankRepository = bankRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.authenticationFacade = authenticationFacade;
    }

    @PostMapping("/register")
    public ResponseEntity<Gambler> register(@RequestBody Gambler gambler) {
        LOGGER.info(">>>>> RECU The username " + gambler.toString());
        try {

            Gambler resultGambler = gamblerRepository.findByUsername(gambler.getUsername());
            // Tester si le nom d'utilisatur est déjà réservé
            if (resultGambler != null) {
                LOGGER.info("The username " + gambler.getUsername() + " is already taken !");
                return new ResponseEntity<Gambler>(gambler, HttpStatus.CONFLICT);

            } else {
                // Add gambler to db
                // Prepare drawings instead of roles
                // Role role1 = new Role("USER", new HashSet<>(Arrays.asList(user)));
                Role role1 = roleRepository.findByRole("USER");   //new Role("USER", new HashSet<>(Arrays.asList(user)));
                LOGGER.info(">>>>>>>>   >>>> Role : " + role1.getRole());
                LOGGER.info(">>>>>>>>   >>>> IS Role NULL ??? " + (role1 == null));

                gambler.setRoles(new HashSet<>(Arrays.asList(role1)));

//                // Crypt gambler text clear password before db persisting
                gambler.setPassword(encoder.encode(gambler.getPassword()));
                //LOGGER.info(">>>>> Gambler Password " + gambler.getPassword());
//                // Set gambler solde to default value od 100 euro
                gambler.setSolde(new BigDecimal("100"));
                // Set gambler enabled status to true
                gambler.setEnabled(true);
                LOGGER.info(">>>>> RECU The username " + gambler.toString());
                gamblerRepository.save(gambler);
                LOGGER.info("The username " + gambler.getUsername() + " has been added to the database !");

                

                return new ResponseEntity<Gambler>(gambler, HttpStatus.CREATED);

            }
        } catch (Exception ex) {
            LOGGER.error("Exception lors de l'enregistrement de l'utlisateur:" + ex);
            return new ResponseEntity<Gambler>(gambler, HttpStatus.CONFLICT);
            // throw new Exception("Exception lors de l'enregistrement de l'utlisateur : "+ex.getMessage());
        }
    }


    //@RequestMapping(value = "/username", method = RequestMethod.GET)
    //@ResponseBody
    @PostMapping("/login")
    public ResponseEntity<Gambler> currentUserNameSimple() {

        Authentication authentication = authenticationFacade.getAuthentication();
//        Gson gson = new Gson();
        //return authentication.getName();
        Gambler resultGambler = gamblerRepository.findByUsername(authentication.getName());
        //return new ResponseEntity<String>("Gambler : " + resultGambler.toString(), HttpStatus.OK);
        ResponseEntity<Gambler> re = (resultGambler!=null)? new ResponseEntity<Gambler>(resultGambler, HttpStatus.OK):
                                                            new ResponseEntity<Gambler>(new Gambler("",
                                                                    "",
                                                                    "",
                                                                    null,
                                                                    "",
                                                                    new BigDecimal("0"),
                                                                    false, null), HttpStatus.NOT_FOUND);
        LOGGER.info("The Gambler " + re.getBody().toString());
        return re;
    }

    @GetMapping("/checklogin")
    public ResponseEntity<?> login(Principal gambler) {

        if (gambler != null) {
            LOGGER.info(">>>>>>  "+ gambler.getName() + ": Authenticated successfully ");
            return new ResponseEntity<String>(gambler.getName() + ": Authenticated successfully", HttpStatus.OK);

        }
        else
            return new ResponseEntity<String>("Please add your basic token in the Authorization Header",
                    HttpStatus.UNAUTHORIZED);
    }


//    @GetMapping(value = "/gamblers")
//    HttpEntity<PagedResources<Gambler>> persons(Pageable pageable,
//                                               PagedResourcesAssembler assembler) {
//
//        Page<Gambler> persons = gamblerRepository.findAll(pageable);
//        return new ResponseEntity<PagedResources<Gambler>>(assembler.toResources(persons), HttpStatus.OK);
//    }

}