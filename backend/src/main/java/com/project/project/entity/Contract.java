package com.project.project.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "contract")
@SequenceGenerator(name = "contract_seq", sequenceName = "contract_seq", allocationSize = 1)
public class Contract implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contract_seq")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "beneficiary", nullable = false, length = 100)
    private String beneficiary;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @OneToMany(mappedBy = "contract")
    private List<HoursBag> hoursBagList;

    @OneToMany(mappedBy = "contract")
    private List<Task> taskList;

    public Contract() {
    }

    public Contract(Long id, String name, String beneficiary, LocalDate createdAt) {
        this.id = id;
        this.name = name;
        this.beneficiary = beneficiary;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public List<HoursBag> getHoursBagList() {
        return hoursBagList;
    }

    public void setHoursBagList(List<HoursBag> hoursBagList) {
        this.hoursBagList = hoursBagList;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    @Override
    public String toString() {
        return "Contract{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", beneficiary='" + beneficiary + '\'' +
                ", createdAt=" + createdAt +
                ", hoursBagList=" + hoursBagList +
                ", taskList=" + taskList +
                '}';
    }
}
