package me.potato.udemytradingservice.controller;

import lombok.RequiredArgsConstructor;
import me.potato.udemytradingservice.client.UserClient;
import me.potato.udemytradingservice.dto.UserDto;
import me.potato.udemytradingservice.dto.UserStockDto;
import me.potato.udemytradingservice.service.UserStockService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@RestController
@RequestMapping("user")
public class UserController {
    private final UserClient userClient;

    private final UserStockService stockService;

    @GetMapping("all")
    public Flux<UserDto> allUsers() {
        return userClient.getAllUsers();
    }

    @GetMapping("{userId}")
    public Flux<UserStockDto> getUserStocks(@PathVariable String userId) {
        return stockService.getUserStocks(userId);
    }
}
