package me.potato.udemytradingservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketConnectorConfigurer;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

import java.time.Duration;

@Configuration
public class RSocketClientConfig {
    @Bean
    public RSocketConnectorConfigurer connectorConfigurer() {
        return c -> c.reconnect(retryStrategy());
    }

    private static RetryBackoffSpec retryStrategy() {
        return Retry.fixedDelay(5, Duration.ofSeconds(2))
                .doBeforeRetry(retrySignal -> System.out.println("Retrying..." + retrySignal.totalRetries()));
    }
}
