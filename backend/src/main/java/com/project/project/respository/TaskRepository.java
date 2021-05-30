package com.project.project.respository;

import com.project.project.entity.HoursBag;
import com.project.project.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {


}
