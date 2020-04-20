package com.tradesystem.cost;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CostService {

    private CostDao costDao;


    public CostService(CostDao costDao) {
        this.costDao = costDao;
    }
/*
    @Transactional
    public List<Cost> createCosts(List<CostDto> costsDtoList) {
        List<Cost> costsList = new ArrayList<>();

        for (CostDto costDto : costsDtoList) {
            Cost cost = Cost.builder()
                    .name(costDto.getName())
                    .value(costDto.getValue())
                    .date(costDto.getDate())
                    .build();

            costsList.add(cost);
        }

        return costDao.saveAll(costsList);
    }
*/
    @Transactional
    public Cost createCost(CostDto costDto) {
        if (validateCost(costDto)) {
            Cost cost = Cost.builder()
                    .name(costDto.getName())
                    .value(costDto.getValue())
                    .date(costDto.getDate())
                    .build();

            return costDao.save(cost);
        }
        throw new RuntimeException("Can't create cost");
    }

    @Transactional
    public List<Cost> getMonthCosts(int month, int year) {
        return costDao.getMonthCosts(month, year);
    }
    private boolean validateCost(CostDto cost) {
        if (cost.getName().equals("") || cost.getName() == null) {
            return false;
        }
        if (cost.getDate() == null) {
            return false;
        }
        if (cost.getValue().doubleValue() <= 0) {
            return false;
        }
        return true;
    }
}
