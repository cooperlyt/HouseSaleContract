package com.dgsoft.house.sale.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by cooper on 9/15/15.
 */
@Entity
@Table(name = "PROJECT_NUMBER_POOL", catalog = "CONTRACT")
public class ProjectNumber implements java.io.Serializable{

    private String projectCode;

    private long number;

    public ProjectNumber() {
    }

    public ProjectNumber(String projectCode, long number) {
        this.projectCode = projectCode;
        this.number = number;
    }

    @Id
    @Column(name = "PROJECT_CODE", unique = true, nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    @Column(nullable = false, name = "NUMBER")
    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

}
