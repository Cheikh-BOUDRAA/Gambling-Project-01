package be.bt.service;

import be.bt.domain.Bank;
import be.bt.domain.Drawing;
import be.bt.domain.Gambler;
import be.bt.repository.BankRepository;
import be.bt.repository.DrawingRespository;
import be.bt.repository.GamblerRepository;
import be.bt.utils.ReelNumbersGeneratorFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.springframework.data.domain.Sort.Direction.*;

@Service
public class DrawingServiceImpl implements IDrawingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DrawingServiceImpl.class);

    private enum DrawResult {

        DEFAULT__RESULT("", BigDecimal.valueOf(0)),
        DRAW_RESULT1("111", BigDecimal.valueOf(2)),
        DRAW_RESULT2("777", BigDecimal.valueOf(4)),
        DRAW_RESULT3("421", BigDecimal.valueOf(10));

        private String drawResult;
        private BigDecimal profitCoefficient;

        DrawResult(String drawResult, BigDecimal profitCoefficient) {

            this.drawResult = drawResult;
            this.profitCoefficient = profitCoefficient;

        }

        public String getDrawResult() {
            return this.drawResult;
        }

        public BigDecimal getProfitCoefficient() {
            return this.profitCoefficient;
        }

        public Boolean isWinner(DrawResult drawResult) {
            return drawResult.profitCoefficient.compareTo(DEFAULT__RESULT.profitCoefficient) > 0;
        }

        public static DrawResult getByDrawResult(String drawResult) {

            return Arrays
                    .stream(values())
                    .filter(dr -> dr.drawResult.equals(drawResult))
                    .findFirst().orElse(DEFAULT__RESULT);

        }

    }

    private GamblerRepository gamblerRepository;
    private DrawingRespository drawingRespository;
    private BankRepository bankRepository;
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private ReelNumbersGeneratorFacade reelNumbersGeneratorFacade;


    public DrawingServiceImpl(GamblerRepository gamblerRepository,
                              DrawingRespository drawingRespository,
                              BankRepository bankRepository,
                              AuthenticationFacade authenticationFacade) {
        this.gamblerRepository = gamblerRepository;
        this.authenticationFacade = authenticationFacade;
        this.drawingRespository = drawingRespository;
        this.bankRepository = bankRepository;
    }

    private void processDrawing(String drawResult, DrawResult enumDR,
                                Gambler currentGambler, Drawing drawing) {
        // Get the bank object
        // This part need more work because i'm using a constant id to obtain the bank info
        Bank bank = bankRepository.findBankById(1L);
        LOGGER.info("Banque info : " + bank.toString());

        Boolean isWinner = enumDR.isWinner(enumDR);
        BigDecimal profitAmount = (isWinner) ?
                drawing.getBetAmount().multiply(enumDR.getProfitCoefficient())
                : BigDecimal.valueOf(0);

        String newDrawResult = drawResult;
        // Check if bank has enough money
        boolean isBankCreditSufficient =
                (bank.getAvailableAmount().compareTo(profitAmount) > -1) ? true : false;

        // isWinner can be replaced with
        if ((isWinner) && (isBankCreditSufficient)) {
            // WINNER && banque solde > 0
            // True && banque < 0
//         if (isWinner && isBankCreditSufficient) {
            // Decrease the bank available amount
            bank.setAvailableAmount(bank.getAvailableAmount().subtract(profitAmount));
            // Increase the gmabler solde
            currentGambler.setSolde(currentGambler.getSolde().add(profitAmount));
        } else if ((isWinner) && (!isBankCreditSufficient)) {
            // current gambler lose
            // Increase and set the bank available amount
            bank.setAvailableAmount(bank.getAvailableAmount().add(drawing.getBetAmount()));
            // decrease and set the gmabler solde
            currentGambler.setSolde(currentGambler.getSolde().subtract(drawing.getBetAmount()));
            // Change the drawResult
            // Modify the drawResult
            int newRadom = reelNumbersGeneratorFacade
                    .generateRandom(0, 7, new ArrayList<>(Arrays.asList(1, 2, 4, 7)));
            newDrawResult = reelNumbersGeneratorFacade
                    .changeString(drawResult, Character.forDigit(newRadom, 10));
            LOGGER.info("The new drawResult is : " + newDrawResult);
        } else {

            // current gambler lose
            // Increase and set the bank available amount
            bank.setAvailableAmount(bank.getAvailableAmount().add(drawing.getBetAmount()));
            // decrease and set the gmabler solde
            currentGambler.setSolde(currentGambler.getSolde().subtract(drawing.getBetAmount()));

        }

        // Set the currentGambler
        drawing.setGambler(currentGambler);
        // Setting the drawResult
        drawing.setDrawResult(newDrawResult);
        // Set the drawing profit amount
        // Set the drawing profit amount
        drawing.setProfitAmount(profitAmount);
    }

    @Override
    public Drawing save(Long gamblerId, Drawing drawing) throws Exception {

        LOGGER.info("The bet Amount : " + String.valueOf(drawing.getBetAmount()));

        // Get the current Gambler object using the authentication object obtained from the context
        Gambler currentGambler = gamblerRepository
                .findByUsername(authenticationFacade.getAuthentication().getName());
        //new ResourceNotFoundException("Customer [gamblerId=" + gamblerId + "] can't be found"));

        // Log the gambler data to the console
        LOGGER.info("Gambler Info : " + currentGambler.toString());

        // Check if gambler has a suficient solde to play
        if (currentGambler.getSolde().compareTo(drawing.getBetAmount()) > -1) {

            // Set the drawing current datetime value
            drawing.setDrawDate(LocalDateTime.now());

            // Obtain a new ReelNumbers randomly
            String drawResult = reelNumbersGeneratorFacade.genReelNumbers();
            LOGGER.info("The Drawing Result " + drawResult);
            // Force drawResult to always Win for testing the business logic,
            // don't forget to delete this line when deploying the app
//            drawResult = "777";

            // Get the corresponding enum DrawResult object using the new generated reels numbers
            DrawResult enumDR = DrawResult.getByDrawResult(drawResult);

            switch (enumDR) {
                case DRAW_RESULT1:  // * 2
                    processDrawing(drawResult, enumDR, currentGambler, drawing);
                    break;
                case DRAW_RESULT2:  // * 4
                    processDrawing(drawResult, enumDR, currentGambler, drawing);
                    break;
                case DRAW_RESULT3: // * 10
                    processDrawing(drawResult, enumDR, currentGambler, drawing);
                    break;
                default:
                    processDrawing(drawResult, enumDR, currentGambler, drawing);
                    break;
            }

            return drawingRespository.save(drawing);

        } else { // Check if gambler has a suficient solde to play failed
            // Return the failing reason to the gambler
                        /*{
                          "reason" : "Your credit is insufficient!! Please Load a credit and try again."
                        }*/
            return null;
        }

    }

    @Override
    public Set<Drawing> allDrawingsByGamblerId(Long gamblerId) {

        // Get the Authentication Object
        Authentication authentication = authenticationFacade.getAuthentication();
        // Get the current Gambler object
        Gambler currentGambler = gamblerRepository
                .findByUsername(authentication.getName());

        LOGGER.info("GamblerId : " + currentGambler.getId());

        drawingRespository.findAllByGamblerId(currentGambler.getId())
                .forEach(drawing1 -> LOGGER.info("All by GamblerId : " + drawing1.toString()));
        return drawingRespository.findAllByGamblerId(currentGambler.getId());
    }

    @Override
    public Integer countDrawingsByGamblerId(Long gamblerId) {

        // Get the Authentication Object
        Authentication authentication = authenticationFacade.getAuthentication();
        // Get the current Gambler object
        Gambler currentGambler = gamblerRepository
                .findByUsername(authentication.getName());

        LOGGER.info("GamblerId : " + currentGambler.getId());

        return drawingRespository.countAllByGamblerId(currentGambler.getId());

    }

    @Override
    public List<Drawing> topTenGamblersHighestProfit() {

//        However, we are not done yet. Our example application has one serious flaw:
//
//        It returns all todo entries that are found from the database, and this is a performance problem.

        //---------------------------------------------- Begin Methode 1 ---------------------------------
        // The Favorite One

        Page<Drawing> page =
                drawingRespository
                        .findAll(
                                PageRequest.of(0, 10,
                                        Sort.by(DESC, "profitAmount")));
        return page.getContent();
        //---------------------------------------------- End Methode 1 ---------------------------------

        //---------------------------------------------- Begin Methode 2 ---------------------------------
        // Not good, tight dependency to the database provider (MySQL), moving to any other database will
        // broke the code
        // return drawingRespository.findTo10DrawingsByProfitAmountDescByNativeSql();
        //---------------------------------------------- Begin Methode 2 ---------------------------------

    }

    @Override
    public BigDecimal totalProfitByGambler(Long gamblerId) {

        // Check the nullness of the authentication Object not needed, Gmabler already Authenticated
        // Get the current Gambler from the Authentication Object
        Gambler currentGambler = gamblerRepository
                .findByUsername(
                        authenticationFacade
                                .getAuthentication()
                                .getName()
                );
        // Get the total profit
        BigDecimal totalProfit = drawingRespository
                .totalProfitAmountByGamblerIdNamedQP(currentGambler.getId());

        LOGGER.info("Summmmmmmmmmmmmmmmm : " + totalProfit);

        return totalProfit;
    }

    @Override
    public Page<Drawing> topTenGamblersHighestProfit2() {
        Page<Drawing> page =
                drawingRespository
                        .findAll(
                                PageRequest.of(0, 10,
                                        Sort.by(DESC, "profitAmount")));
        return page;
    }

    @Override
    public Page<Drawing> findAllDrawings(Pageable pageable) {
        return drawingRespository.findAll(pageable);
    }

}