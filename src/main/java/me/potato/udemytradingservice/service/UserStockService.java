package me.potato.udemytradingservice.service;

import lombok.RequiredArgsConstructor;
import me.potato.udemytradingservice.dto.StockTradeRequest;
import me.potato.udemytradingservice.dto.UserStockDto;
import me.potato.udemytradingservice.entity.UserStock;
import me.potato.udemytradingservice.repository.UserStockRepository;
import me.potato.udemytradingservice.util.EntityDtoUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class UserStockService {

    private final UserStockRepository stockRepository;

    public Flux<UserStockDto> getUserStocks(String userId) {
        return stockRepository.findAllByUserId(userId)
                .map(EntityDtoUtil::toUserStockDto);
    }


    // buy stock
    public Mono<UserStock> buyStock(StockTradeRequest request) {
        return stockRepository.findByUserIdAndStockSymbol(request.getUserId(), request.getStockSymbol())
                .defaultIfEmpty(EntityDtoUtil.toUserStock(request))
                .doOnNext(us -> us.setQuantity(us.getQuantity() + request.getQuantity()))
                .flatMap(stockRepository::save);
    }


    // sell stock
    public Mono<UserStock> sellStock(StockTradeRequest request) {
        return stockRepository.findByUserIdAndStockSymbol(request.getUserId(), request.getStockSymbol())
                .filter(us -> us.getQuantity() >= request.getQuantity())
                .doOnNext(us -> us.setQuantity(us.getQuantity() - request.getQuantity()))
                .flatMap(stockRepository::save);
    }
}
