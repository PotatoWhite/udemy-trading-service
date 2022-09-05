package me.potato.udemytradingservice.client;

import io.rsocket.transport.netty.client.TcpClientTransport;
import me.potato.udemytradingservice.dto.StockTickDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.rsocket.RSocketConnectorConfigurer;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class StockClient {
    private final RSocketRequester     requester;
    private       Flux<StockTickDto>   stockTickFlux;
    private final Map<String, Integer> currentPrice;

    private StockClient(RSocketRequester.Builder builder, RSocketConnectorConfigurer connectorConfigurer, @Value("${stock.service.host}") String host, @Value("${stock.service.port}") int port) {
        this.requester = builder.rsocketConnector(connectorConfigurer)
                .transport(TcpClientTransport.create(host, port));

        currentPrice = new HashMap<>();

        initialize();
    }

    public Flux<StockTickDto> getStockStream() {
        return stockTickFlux;
    }

    public int getCurrentStockPrice(String symbol) {
        return currentPrice.getOrDefault(symbol, 0);
    }

    public void initialize() {
        stockTickFlux = requester.route("stock.ticks")
                .retrieveFlux(StockTickDto.class)
                .doOnNext(s -> currentPrice.put(s.getCode(), s.getPrice()))
                .retryWhen(Retry.fixedDelay(Long.MAX_VALUE, Duration.ofSeconds(2)))
                .publish()
                .autoConnect(0);

    }
}
