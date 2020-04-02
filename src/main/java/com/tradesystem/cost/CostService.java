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

}
