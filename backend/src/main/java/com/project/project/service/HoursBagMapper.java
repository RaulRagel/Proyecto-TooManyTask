package com.project.project.service;

import com.project.project.dto.HoursBagDTO;
import com.project.project.entity.Contract;
import com.project.project.entity.HoursBag;

public class HoursBagMapper {

    public static HoursBagDTO toDto(HoursBag hoursBag) {
        HoursBagDTO hoursBagDTO = new HoursBagDTO();

        hoursBagDTO.setId(hoursBag.getId());
        hoursBagDTO.setContractId(hoursBag.getContract().getId());

        hoursBagDTO.setContractBN(
         "["+hoursBag.getContract().getBeneficiary()+"] "+hoursBag.getContract().getName()
        );

        hoursBagDTO.setInitDate(hoursBag.getInitDate());
        hoursBagDTO.setEndDate(hoursBag.getEndDate());
        hoursBagDTO.setHours(hoursBag.getHours());
        hoursBagDTO.setCreatedAt(hoursBag.getCreatedAt());

        return hoursBagDTO;
    }

    public static HoursBag toEntity(HoursBagDTO hoursBagDTO) {
        HoursBag hoursBag = new HoursBag();

        hoursBag.setId(hoursBagDTO.getId());
        hoursBag.setInitDate(hoursBagDTO.getInitDate());
        hoursBag.setEndDate(hoursBagDTO.getEndDate());
        hoursBag.setHours(hoursBagDTO.getHours());
        hoursBag.setCreatedAt(hoursBagDTO.getCreatedAt());

        Contract contract = new Contract();
        contract.setId(hoursBagDTO.getContractId());

        hoursBag.setContract(contract);

        return hoursBag;
    }
}
