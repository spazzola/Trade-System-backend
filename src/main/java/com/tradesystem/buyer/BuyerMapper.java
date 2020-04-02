package com.tradesystem.buyer;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BuyerMapper {

    public BuyerDto toDto(Buyer buyer) {
        return BuyerDto.builder()
                .id(buyer.getId())
                .name(buyer.getName())
                .currentBalance(buyer.getCurrentBalance())
                .build();
    }

    public List<BuyerDto> toDto(List<Buyer> buyers) {
        return buyers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
