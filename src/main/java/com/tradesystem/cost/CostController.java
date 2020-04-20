package com.tradesystem.cost;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/cost")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CostController {

    private CostMapper costMapper;
    private CostService costService;


    public CostController(CostMapper costMapper, CostService costService) {
        this.costMapper = costMapper;
        this.costService = costService;
    }

/*
    @PostMapping("/create")
    public List<CostDto> create(@RequestBody List<CostDto> costsDtoList) {
        final List<Cost> costs = costService.createCosts(costsDtoList);

        return costMapper.toDto(costs);
    }
*/

    @PostMapping("/create")
    public CostDto create(@RequestBody CostDto costDto) {
        final Cost cost = costService.createCost(costDto);

        return costMapper.toDto(cost);
    }

    @GetMapping("/getMonthCosts")
    public List<CostDto> getMonthCosts(@RequestParam(value = "localDate", required = false)
                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String localDate) {
        int year = Integer.valueOf(localDate.substring(0, 4));
        int month = Integer.valueOf(localDate.substring(5, 7));

        List<Cost> costs = costService.getMonthCosts(month, year);

        return costMapper.toDto(costs);
    }
}
