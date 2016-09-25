package com.dgsoft.house.sale.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by cooper on 9/25/16.
 */
@Entity
@Table(name = "SCHOOL_AREA", catalog = "CONTRACT")
public class SchoolArea implements java.io.Serializable{

    private String id;
    private String name;
    private String district;

    public SchoolArea() {
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

    @Column(name = "NAME",nullable = false,length = 64)
    @NotNull
    @Size(max = 64)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "DISTRICT", nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
