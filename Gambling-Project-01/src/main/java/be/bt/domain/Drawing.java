package be.bt.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "Drawings")
public class Drawing  /*implements Comparable<Drawing>*/  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Résultat du tirage
    private String drawResult;

    // La data du tirage
    private LocalDateTime drawDate;

    // Montant de la mise
    private BigDecimal betAmount;

    // Montant du gain
    private BigDecimal profitAmount;

    public Gambler getGambler() {
        return gambler;
    }

    public void setGambler(Gambler gambler) {
        this.gambler = gambler;
    }

    @ManyToOne
    private Gambler gambler;

    public Drawing() {
    }

    public Drawing(String drawResult, LocalDateTime drawDate,
                   BigDecimal betAmount, BigDecimal profitAmount, Gambler gambler) {
        this.drawResult = drawResult;
        this.drawDate = drawDate;
        this.betAmount = betAmount;
        this.profitAmount = profitAmount;
        this.gambler = gambler;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDrawResult() {
        return drawResult;
    }

    public void setDrawResult(String drawResult) {
        this.drawResult = drawResult;
    }

    public LocalDateTime getDrawDate() {
        return drawDate;
    }

    public void setDrawDate(LocalDateTime drawDate) {
        this.drawDate = drawDate;
    }

    public BigDecimal getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(BigDecimal betAmount) {
        this.betAmount = betAmount;
    }

    public BigDecimal getProfitAmount() {
        return profitAmount;
    }

    public void setProfitAmount(BigDecimal profitAmount) {
        this.profitAmount = profitAmount;
    }

    @Override
    public String toString() {
        return "Drawing{" +
                "id=" + id +
                ", drawResult='" + drawResult + '\'' +
                ", drawDate=" + drawDate +
                ", betAmount=" + betAmount +
                ", profitAmount=" + profitAmount +
                '}';
    }

    /*@Override
    public int compareTo(Drawing drawing) {

        // Ascending order
        // return this.profitAmount.compareTo(drawing.getProfitAmount());

        // Descending order
        return drawing.getProfitAmount().compareTo(this.profitAmount);

    }*/
}
