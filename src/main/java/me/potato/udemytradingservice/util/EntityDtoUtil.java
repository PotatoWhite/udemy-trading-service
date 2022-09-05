package me.potato.udemytradingservice.util;

import me.potato.udemytradingservice.dto.*;
import me.potato.udemytradingservice.entity.UserStock;

public class EntityDtoUtil {

    public static TransactionRequest toTransactionRequest(StockTradeRequest stockTradeRequest, int amount) {
        var transactionRequest = new TransactionRequest();
        transactionRequest.setUserId(stockTradeRequest.getUserId());
        transactionRequest.setAmount(stockTradeRequest.getQuantity());

        var type = TradeType.BUY.equals(stockTradeRequest.getTradeType()) ? TransactionType.DEBIT : TransactionType.CREDIT;
        transactionRequest.setType(type);
        transactionRequest.setAmount(amount);

        return transactionRequest;
    }

    public static StockTradeResponse toTradeResponse(StockTradeRequest tradeRequest, TradeStatus status, int price) {
        var stockTradeResponse = new StockTradeResponse();
        stockTradeResponse.setUserId(tradeRequest.getUserId());
        stockTradeResponse.setStockSymbol(tradeRequest.getStockSymbol());
        stockTradeResponse.setQuantity(tradeRequest.getQuantity());
        stockTradeResponse.setTradeType(tradeRequest.getTradeType());

        stockTradeResponse.setTradeStatus(status);
        stockTradeResponse.setPrice(price);

        return stockTradeResponse;
    }

    public static UserStock toUserStock(StockTradeRequest request) {
        var userStock = new UserStock();
        userStock.setUserId(request.getUserId());
        userStock.setStockSymbol(request.getStockSymbol());
        userStock.setQuantity(0);
        return userStock;
    }

    public static UserStockDto toUserStockDto(UserStock userStock) {
        var userStockDto = new UserStockDto();
        userStockDto.setUserId(userStock.getUserId());
        userStockDto.setStockSymbol(userStock.getStockSymbol());
        userStockDto.setQuantity(userStock.getQuantity());
        userStockDto.setId(userStock.getId());
        return userStockDto;
    }
}
