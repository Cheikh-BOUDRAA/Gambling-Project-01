package be.bt.domain;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Banks")
public class Bank {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Nom de la banque
    private String bankName;

    // Montant disponible dans la la banque
    private BigDecimal availableAmount;

    public Bank() {
    }

    public Bank(String bankName, BigDecimal availableAmount) {
        this.bankName = bankName;
        this.availableAmount = availableAmount;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public BigDecimal getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(BigDecimal availableAmount) {
        this.availableAmount = availableAmount;
    }

    @Override
    public String toString() {
        return "Bank{" +
                "id=" + id +
                ", bankName='" + bankName + '\'' +
                ", availableAmount=" + availableAmount +
                '}';
    }
}
