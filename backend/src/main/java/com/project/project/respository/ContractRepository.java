package com.project.project.respository;

import com.project.project.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Long> {

    public List<Contract> findByName(String name);

}
