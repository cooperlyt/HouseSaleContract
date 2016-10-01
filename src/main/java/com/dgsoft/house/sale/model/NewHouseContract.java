package com.dgsoft.house.sale.model;

import com.dgsoft.house.SalePayType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by cooper on 9/27/16.
 */
@Entity
@Table(name = "NEW_HOUSE_CONTRACT", catalog = "CONTRACT")
public class NewHouseContract implements java.io.Serializable{

    private String id;

    private SalePayType salePayType;
    private String projectCode;


    private String projectCerNumber;

    private HouseContract houseContract;

    public NewHouseContract() {
    }

    public NewHouseContract(HouseContract houseContract) {
        this.houseContract = houseContract;
    }

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

    @Column(name = "PROJECT_ID",nullable = true, length = 32)
    @Size(max = 32)
    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }


    @Column(name = "PROJECT_CER_NUMBER", nullable = true, length = 100)
    @Size(max = 100)
    public String getProjectCerNumber() {
        return projectCerNumber;
    }

    public void setProjectCerNumber(String projectCerNumber) {
        this.projectCerNumber = projectCerNumber;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "SALE_PAY_TYPE", nullable = false, length = 32)
    @NotNull
    public SalePayType getSalePayType() {
        return salePayType;
    }

    public void setSalePayType(SalePayType salePayType) {
        this.salePayType = salePayType;
    }




}
