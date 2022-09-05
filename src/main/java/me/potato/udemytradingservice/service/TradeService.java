package me.potato.udemytradingservice.service;

import lombok.RequiredArgsConstructor;
import me.potato.udemytradingservice.client.StockClient;
import me.potato.udemytradingservice.client.UserClient;
import me.potato.udemytradingservice.dto.*;
import me.potato.udemytradingservice.util.EntityDtoUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class TradeService {
    private final UserStockService stockService;
    private final UserClient       userClient;
    private final StockClient      stockClient;


    public Mono<StockTradeResponse> trade(StockTradeRequest tradeRequest) {
        var request = EntityDtoUtil.toTransactionRequest(tradeRequest, estimatePrice(tradeRequest));
        var userStockMono = TradeType.BUY.equals(tradeRequest.getTradeType()) ?
                buyStock(tradeRequest, request) :
                sellStock(tradeRequest, request);

        return userStockMono
                .defaultIfEmpty(EntityDtoUtil.toTradeResponse(tradeRequest, TradeStatus.FAILED, 0));
    }

    private Mono<StockTradeResponse> buyStock(StockTradeRequest tradeRequest, TransactionRequest transactionRequest) {
        return userClient.doTransaction(transactionRequest)
                .filter(tr -> TransactionStatus.COMPLETED.equals(tr.getStatus()))
                .flatMap(ts -> stockService.buyStock(tradeRequest))
                .map(us -> EntityDtoUtil.toTradeResponse(tradeRequest, TradeStatus.COMPLETED, transactionRequest.getAmount()));

    }

    private Mono<StockTradeResponse> sellStock(StockTradeRequest tradeRequest, TransactionRequest transactionRequest) {
        return stockService.sellStock(tradeRequest)
                .flatMap(us -> userClient.doTransaction(transactionRequest))
                .map(tr -> EntityDtoUtil.toTradeResponse(tradeRequest, TradeStatus.COMPLETED, transactionRequest.getAmount()));

    }

    public int estimatePrice(StockTradeRequest tradeRequest) {
        return tradeRequest.getQuantity() * stockClient.getCurrentStockPrice(tradeRequest.getStockSymbol());
    }

}
