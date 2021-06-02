package com.project.project.respository;

import com.project.project.entity.Contract;
import com.project.project.entity.HoursBag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HoursBagRepository extends JpaRepository<HoursBag, Long> {


}
