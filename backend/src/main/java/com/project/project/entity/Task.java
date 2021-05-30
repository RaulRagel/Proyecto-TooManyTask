package com.project.project.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "task")
@SequenceGenerator(name = "task_seq", sequenceName = "task_seq", allocationSize = 1)
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_seq")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "finished_at")
    private LocalDate finishedAt;

    @Column(name = "invested_time")
    private int investedTime;

    @Column(name = "priority")
    private String priority;

    @Column(name = "state")
    private String state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    private Contract contract;

    public Task() {
    }

    public Task(Long id, String title, String description, LocalDate createdAt, LocalDate finishedAt, int investedTime, String priority, String state, Contract contract) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.finishedAt = finishedAt;
        this.investedTime = investedTime;
        this.priority = priority;
        this.state = state;
        this.contract = contract;
    }

    public LocalDate getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(LocalDate finishedAt) {
        this.finishedAt = finishedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getInvestedTime() {
        return investedTime;
    }

    public void setInvestedTime(int investedTime) {
        this.investedTime = investedTime;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
