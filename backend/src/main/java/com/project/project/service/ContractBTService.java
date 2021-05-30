package com.project.project.service;

import com.project.project.dto.ContractBTDTO;
import com.project.project.dto.ContractDTO;
import com.project.project.entity.Contract;
import com.project.project.respository.ContractRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContractBTService {

    private final ContractRepository contractRepository;

    public ContractBTService(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    public List<ContractBTDTO> getAllContracts() {

        return contractRepository.findAll().stream().map(ContractBTMapper::toDto) //contrato -> toDto(contrato)
                .collect(Collectors.toList());
    }

    public ContractBTDTO getContractById(Long id) {

        return ContractBTMapper.toDto(contractRepository.findById(id).isPresent() ?
                contractRepository.findById(id).get() : new Contract());
    }
}








