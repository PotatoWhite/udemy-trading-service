package me.potato.udemytradingservice.controller;

import lombok.RequiredArgsConstructor;
import me.potato.udemytradingservice.client.StockClient;
import me.potato.udemytradingservice.dto.StockTickDto;
import me.potato.udemytradingservice.dto.StockTradeRequest;
import me.potato.udemytradingservice.dto.StockTradeResponse;
import me.potato.udemytradingservice.service.TradeService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("stock")
public class TradeController {
    private final TradeService tradeService;
    private final StockClient  stockClient;

    @GetMapping(value = "tick/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<StockTickDto> stockTicks() {
        return stockClient.getStockStream();
    }

    @PostMapping("trade")
    public Mono<ResponseEntity<StockTradeResponse>> trade(@RequestBody Mono<StockTradeRequest> requestMono) {
        return requestMono
                .filter(tr -> tr.getQuantity() > 0)
                .flatMap(tradeService::trade)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
}
