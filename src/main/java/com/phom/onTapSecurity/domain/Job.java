package com.phom.onTapSecurity.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.phom.onTapSecurity.util.SecurityUtil;
import com.phom.onTapSecurity.util.constant.LevelEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;


@Entity
@Table(name = "jobs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "location")
    private String location;

    @Column(name = "salary")
    private double salary;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "level")
    @Enumerated(EnumType.STRING)
    private LevelEnum level;

    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "is_active")
    private boolean active;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "update_by")
    private String updatedBy;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToMany
    @JsonIgnoreProperties(value = {"jobs"})
    @JoinTable(name = "job_skill", joinColumns = @JoinColumn(name = "job_id"))
    private List<Skill> skills;

    @PrePersist
    public void handleCreate() {
        this.createdAt = Instant.now();
        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
    }

    @PreUpdate
    public void handleUpdate() {
        this.updatedAt = Instant.now();
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
    }
}
