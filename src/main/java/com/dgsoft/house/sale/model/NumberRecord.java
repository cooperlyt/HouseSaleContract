package com.dgsoft.house.sale.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by cooper on 9/15/15.
 */
@Entity
@Table(name = "NUMBER_POOL", catalog = "CONTRACT", uniqueConstraints = @UniqueConstraint(columnNames = {
        "TYPE", "PROJECT_CODE"}))
public class NumberRecord implements java.io.Serializable{

    private String id;
    private long number;
    private String type;
    private ProjectNumber projectNumber;
    private long version;

    public NumberRecord() {
    }

    public NumberRecord(ProjectNumber projectNumber, String type) {
        this.projectNumber = projectNumber;
        this.type = type;
        number = 1;
    }

    @Id
    @Column(name = "ID", unique = true, nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "NUMBER", nullable = false)
    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    @Column(name = "TYPE", length = 50, nullable = false)
    @NotNull
    @Size(max = 50)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_CODE",nullable = false)
    @NotNull
    public ProjectNumber getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(ProjectNumber projectNumber) {
        this.projectNumber = projectNumber;
    }

    @Version
    @Column(name = "VERSION",nullable = false)
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
