package me.potato.udemytradingservice.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserDto {
    private String id;
    private String name;
    private int    balance;
}
