package be.bt.rest;

import be.bt.domain.Drawing;
import be.bt.service.IDrawingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
public class DrawingResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(DrawingResource.class);


    @Autowired
    private EntityLinks links;

//    private ClientsRepository clientsRepository;
    private IDrawingService iDrawingService;

    public DrawingResource(IDrawingService iDrawingService) {
        this.iDrawingService = iDrawingService;
    }

    @PostMapping(value = "/gamblers/{gamblerId}/drawings")
//    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Drawing> save(@PathVariable Long gamblerId, @RequestBody Drawing drawing)
            throws Exception {
//        try {
        Drawing drawingResult = iDrawingService.save(gamblerId, drawing);
        if (drawingResult != null) {
            return new ResponseEntity<Drawing>(drawingResult, HttpStatus.CREATED);
        }
        else {
            return new ResponseEntity<Drawing>(drawingResult, HttpStatus.PAYMENT_REQUIRED);
        }

            //return iDrawingService.save(gamblerId, drawing);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return dr;
    }


    @GetMapping(value = "/gamblers/{gamblerId}/drawings")
//    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Set<Drawing>> allDrawingsByGamblerId(@PathVariable Long gamblerId)
            throws Exception {
        Set<Drawing> drawingResult = iDrawingService.allDrawingsByGamblerId(gamblerId);
        if (drawingResult != null) {
            return new ResponseEntity<>(drawingResult, HttpStatus.CREATED);
        }
        else {
            return new ResponseEntity<>(drawingResult, HttpStatus.NOT_FOUND);
        }
     }

    @GetMapping(value = "/gamblers/{gamblerId}/drawings/count")
//    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Integer> countDrawingsByGamblerId(@PathVariable Long gamblerId)
            throws Exception {
        //
        Integer drawingCount = iDrawingService.countDrawingsByGamblerId(gamblerId);
        if (drawingCount != 0) {
            return new ResponseEntity<>(drawingCount, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(0, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/gamblers/drawings/top10")
//    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<List<Drawing>> topTenGamblersHighestProfit()
            throws Exception {
        //
        List<Drawing> drawings = iDrawingService.topTenGamblersHighestProfit();
        if (drawings != null) {
            return new ResponseEntity<>(drawings, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(drawings, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/gamblers/{gamblerId}/drawings/totalprofit")
//    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<BigDecimal> totalProfitByGambler(@PathVariable Long gamblerId)
            throws Exception {
        //

        BigDecimal totalProfit = iDrawingService.totalProfitByGambler(gamblerId);

        LOGGER.info("totalProfit :::: " + totalProfit);
        if (totalProfit != null) {
            return new ResponseEntity<>(totalProfit, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new BigDecimal("0"), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/v1/drawings", params = { "range", "" })
    public ResponseEntity<String> getDrawingsByRange(@RequestParam("range") String range) {

        LOGGER.info("Range : " + range);

        return new ResponseEntity<>(range, HttpStatus.OK);

    }

    @GetMapping(value = "/v1/gamblers/drawings/top10_2")
//    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Page<Drawing>> topTenGamblersHighestProfit2()
            throws Exception {
        //
        Page<Drawing> drawings = iDrawingService.topTenGamblersHighestProfit2();
        if (drawings != null) {
            return new ResponseEntity<>(drawings, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(drawings, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/drawings",produces =MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResources<Drawing>> allDrawings(Pageable pageable, PagedResourcesAssembler assembler){
        Page<Drawing> drawings = iDrawingService.findAllDrawings(pageable);
        drawings.getContent().stream().forEach(drawing -> LOGGER.info("Drawing : " + drawing.toString()));




        PagedResources<Drawing> pr  = assembler
                .toResource(drawings, linkTo(DrawingResource.class)
                        .slash("/drawings").withSelfRel());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Link", createLinkHeader(pr));
        return new ResponseEntity<>(assembler
                .toResource(drawings, linkTo(DrawingResource.class)
                        .slash("/drawings").withSelfRel()), responseHeaders, HttpStatus.OK);
    }

    private String createLinkHeader(PagedResources<Drawing> pr){
        final StringBuilder linkHeader = new StringBuilder();
        if (pr.hasLink("first")) {
            linkHeader.append(buildLinkHeader(pr.getLinks("first").get(0).getHref(), "first"));
            linkHeader.append(", ");
        }
        else if (pr.hasLink("next")) {
            linkHeader.append(buildLinkHeader( pr.getLinks("next").get(0).getHref(),"next"));
            linkHeader.append(", ");
        }
        else if (pr.hasLink("last")) {
            linkHeader.append(buildLinkHeader( pr.getLinks("last").get(0).getHref(),"last"));
            linkHeader.append(", ");
        }
        else if (pr.hasLink("previous")) {
            linkHeader.append(buildLinkHeader( pr.getLinks("previous").get(0).getHref(),"previous"));
        }
        return linkHeader.toString();
    }

    public static String buildLinkHeader(final String uri, final String rel) {
        return "<" + uri + ">; rel=\"" + rel + "\"";
    }

}
