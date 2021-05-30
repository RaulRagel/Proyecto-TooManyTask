package com.project.project.controller;

import com.project.project.dto.ContractBTDTO;
import com.project.project.service.ContractBTService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contractBT")
@CrossOrigin
public class ContractBTController {

    private final ContractBTService contractBTService;

    public ContractBTController(ContractBTService contractService) {
        this.contractBTService = contractService;
    }

    @GetMapping
    public ResponseEntity<List<ContractBTDTO>> getAll() {
        return ResponseEntity.ok(contractBTService.getAllContracts());
    }

    @GetMapping("/{contractId}")
    public ResponseEntity<ContractBTDTO> getById(@PathVariable String contractId) {
        Long id = Long.parseLong(contractId);
        return ResponseEntity.ok(contractBTService.getContractById(id));
    }


}











