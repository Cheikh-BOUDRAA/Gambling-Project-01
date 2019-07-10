package be.bt.repository;

import be.bt.domain.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<Bank, Long> {

    Bank findBankByBankName(String bankName);
    Bank findBankById(Long Id);

}