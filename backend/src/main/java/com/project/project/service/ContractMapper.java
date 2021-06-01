package com.project.project.service;

import com.project.project.dto.ContractDTO;
import com.project.project.entity.Contract;

public class ContractMapper {

    public static ContractDTO toDto(Contract contract) {
        ContractDTO contractDTO = new ContractDTO();

        contractDTO.setId(contract.getId());
        contractDTO.setName(contract.getName());
        contractDTO.setBeneficiary(contract.getBeneficiary());
        contractDTO.setCreatedAt(contract.getCreatedAt());

        if(contract.getHoursBagList() != null) contractDTO.setHourBags(contract.getHoursBagList().size());
        else contractDTO.setHourBags(0);

        if(contract.getTaskList() != null) contractDTO.setTasks(contract.getTaskList().size());
        else contractDTO.setTasks(0);

        contractDTO.setWarnings(0);

        return contractDTO;
    }

    public static Contract toEntity(ContractDTO contractDTO) {
        Contract contract = new Contract();

        contract.setId(contractDTO.getId());
        contract.setName(contractDTO.getName());
        contract.setBeneficiary(contractDTO.getBeneficiary());
        contract.setCreatedAt(contractDTO.getCreatedAt());

        return contract;
    }
}
