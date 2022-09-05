package me.potato.udemytradingservice.client;

import io.rsocket.transport.netty.client.TcpClientTransport;
import me.potato.udemytradingservice.dto.TransactionRequest;
import me.potato.udemytradingservice.dto.TransactionResponse;
import me.potato.udemytradingservice.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.rsocket.RSocketConnectorConfigurer;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserClient {
    private final RSocketRequester requester;

    private UserClient(RSocketRequester.Builder builder, RSocketConnectorConfigurer connectorConfigurer, @Value("${user.service.host}") String host, @Value("${user.service.port}") int port) {
        this.requester = builder.rsocketConnector(connectorConfigurer)
                .transport(TcpClientTransport.create(host, port));
    }

    public Mono<TransactionResponse> doTransaction(TransactionRequest request) {
        return requester.route("user.transaction")
                .data(request)
                .retrieveMono(TransactionResponse.class)
                .doOnNext(System.out::println);
    }

    public Flux<UserDto> getAllUsers() {
        return requester.route("user.all")
                .retrieveFlux(UserDto.class);
    }

}
