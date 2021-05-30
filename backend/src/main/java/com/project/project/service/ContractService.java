package com.project.project.service;

import com.project.project.dto.ContractDTO;
import com.project.project.entity.Contract;
import com.project.project.respository.ContractRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContractService {

    private final ContractRepository contractRepository;

    public ContractService(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    public List<ContractDTO> getAllContracts() {

        return contractRepository.findAll().stream().map(ContractMapper::toDto) //contrato -> toDto(contrato)
                .collect(Collectors.toList());
    }

    public ContractDTO getContractById(Long idContract) {
        return ContractMapper.toDto(contractRepository.findById(idContract).isPresent() ?
                contractRepository.findById(idContract).get() : new Contract());
    }

    /*public List<ContractDTO> getContractByName(String name) {
        return contractRepository.findByName(name).stream().map(ContractMapper::toDto)
                .collect(Collectors.toList());
    }*/

    public ContractDTO create(ContractDTO contractDTO) {
        contractDTO.setCreatedAt(LocalDate.now());
        return ContractMapper.toDto(contractRepository.save(ContractMapper.toEntity(contractDTO)));
    }

    public ContractDTO update(ContractDTO contractDTO) {
        return ContractMapper.toDto(contractRepository.save(ContractMapper.toEntity(contractDTO)));
    }


    public void deleteContractById(Long idContract) {

        ContractDTO contract = getContractById(idContract);
        contractRepository.delete(ContractMapper.toEntity(contract));
    }


}








