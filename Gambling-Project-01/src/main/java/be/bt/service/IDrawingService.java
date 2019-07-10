package be.bt.service;

import be.bt.domain.Drawing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface IDrawingService {

    Drawing save(Long gamblerId, Drawing drawing) throws Exception;

    Set<Drawing> allDrawingsByGamblerId(Long gamblerId);

    Integer countDrawingsByGamblerId(Long gamblerId);
    List<Drawing> topTenGamblersHighestProfit();
    Page<Drawing> topTenGamblersHighestProfit2();
    BigDecimal totalProfitByGambler(Long gamblerId);
    Page<Drawing> findAllDrawings(Pageable pageable);
}
