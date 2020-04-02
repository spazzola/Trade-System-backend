package com.tradesystem.cost;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cost")
public class CostController {

    private CostMapper costMapper;
    private CostService costService;


    public CostController(CostMapper costMapper, CostService costService) {
        this.costMapper = costMapper;
        this.costService = costService;
    }


    @PostMapping("/create")
    public List<CostDto> create(@RequestBody List<CostDto> costsDtoList) {
        final List<Cost> costs = costService.createCosts(costsDtoList);

        return costMapper.toDto(costs);
    }
}
