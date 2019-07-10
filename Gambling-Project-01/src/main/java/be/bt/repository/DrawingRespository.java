package be.bt.repository;

import be.bt.domain.Drawing;
import javafx.beans.binding.NumberExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface DrawingRespository extends JpaRepository<Drawing, Long> {

//    JPAQ
//    NumberExpression totalPrice = sales.total.multiply(sales.price);


//        @Query("FROM Drawing g where g.gambler.id = :gamblerId")
//        Set<Drawing> Drawing find (@Param("gamblerId") Long gamblerId);

    Set<Drawing> findAllByGamblerId(Long gamblerId); // Return All the Drawings by GamblerId


    int countAllByGamblerId(Long gamblerId);  // return total drawings by Gambler

//
//    @Query("SELECT d FROM Drawing d WHERE " +
//            "LOWER(t.title) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR " +
//            "LOWER(t.description) LIKE LOWER(CONCAT('%',:searchTerm, '%'))")
//    List<Drawing> findBySearchTerm(@Param("searchTerm") String searchTerm, Sort sort);

    // ========================== Use native SQL ============================
    @Query(value = "SELECT * FROM drawings d ORDER BY d.profit_amount DESC LIMIT 10",
            nativeQuery=true
    )
    List<Drawing> findTop10DrawingsByProfitAmountDescByNativeSql();
    // =======================================================================

    //Page<Drawing> page = findAll(PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "seatNumber")));
    //Set<Drawing> findTop10OrderByProfitAmountByDesc();

//    Page<Drawing> findAllByOrderByProfitAmountDesc();

//    Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "ProfitAmountDesc"));
//    @Override
//    List<Drawing> findAll( (new Sort(Sort.Direction.ASC, "title") );


//       findAll(new Sort(Sort.Direction.DESC, "id"));

    //Set<Drawing> findTop10OrderByProfitAmountDesc();


//    Set<Drawing> findAllBy

//    List<Drawing> findDistinctFirstTenOrderByProfitAmount();
//    Set<Drawing>

//    @Query(value = "SELECT sum(c.customer_id) FROM CustomerMaster c ")
//    public String getMaxId();

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //               SQL Agregation SUM, AVG etc ...
    // ************************************* Indexed Query Parameters ***********************************
    // Native SQL Way
    // @Query(value = "SELECT SUM(profit_amount) FROM Drawings WHERE gambler_id = ?1", nativeQuery = true)
    // JPQL Way
    @Query("SELECT SUM(d.profitAmount) FROM Drawing d WHERE d.gambler.id = ?1")
    BigDecimal totalProfitAmountByGamblerIdIndexQP(Long GamblerId);
    // ********************************* Named Query Parameters *****************************************
    // Native SQL Way
    // @Query(value = "SELECT SUM(profitAmount) FROM Drawings WHERE gambler_id = ?1", nativeQuery = true)
    // JPQL Way
    @Query("SELECT SUM(d.profitAmount) FROM Drawing d WHERE d.gambler.id = :gamblerId")
    BigDecimal totalProfitAmountByGamblerIdNamedQP(@Param("gamblerId") Long gamblerId);
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

}
