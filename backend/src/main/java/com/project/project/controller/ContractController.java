package com.project.project.controller;

import com.project.project.dto.ContractDTO;
import com.project.project.service.ContractService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contract")
@CrossOrigin
public class ContractController {

    //objeto para acceder a los métodos de Service
    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @GetMapping
    public ResponseEntity<List<ContractDTO>> getAll() {
        return ResponseEntity.ok(contractService.getAllContracts());
    }

    @GetMapping("/{contractId}")
    public ResponseEntity<ContractDTO> getById(@PathVariable String contractId) {
        Long id = Long.parseLong(contractId);
        return ResponseEntity.ok(contractService.getContractById(id));
    }

    @GetMapping("/name/{contractId}")
    public ResponseEntity<String> getNameById(@PathVariable String contractId) {
        Long id = Long.parseLong(contractId);
        return ResponseEntity.ok(contractService.getContractById(id).getName());
    }

    /*@GetMapping("/name/{contractId}")
    public ResponseEntity<List<ContractDTO>> getByName(@PathVariable String contractId) {

        return ResponseEntity.ok(contractService.getContractByName(contractId));
    }*/

    @PostMapping
    public ResponseEntity<ContractDTO> create(@RequestBody ContractDTO data) {
        return ResponseEntity.ok(contractService.create(data));
    }

    @PutMapping
    public ResponseEntity<ContractDTO> update(@RequestBody ContractDTO data) {
        if (data.getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(contractService.update(data));
    }

    @DeleteMapping("/{contractId}")
    public ResponseEntity<Void> deleteById(@PathVariable String contractId) {

        Long id = Long.parseLong(contractId);

        contractService.deleteContractById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}













