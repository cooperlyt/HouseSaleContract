package com.dgsoft.house.sale.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by cooper on 9/27/16.
 */
@Entity
@Table(name = "OLD_HOUSE_CONTRACT", catalog = "CONTRACT")
public class OldHouseContract implements java.io.Serializable {

    private String id;
    private boolean control;

    private HouseContract houseContract;

    @Id
    @Column(name = "ID", unique = true, nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    @GenericGenerator(name = "pkGenerator",
            strategy = "foreign",
            parameters = { @org.hibernate.annotations.Parameter(name = "property", value = "houseContract") })
    @GeneratedValue(generator = "pkGenerator")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    public HouseContract getHouseContract() {
        return houseContract;
    }

    public void setHouseContract(HouseContract houseContract) {
        this.houseContract = houseContract;
    }

    @Column(name = "CONTROL",nullable = false)
    public boolean isControl() {
        return control;
    }

    public void setControl(boolean control) {
        this.control = control;
    }
}
