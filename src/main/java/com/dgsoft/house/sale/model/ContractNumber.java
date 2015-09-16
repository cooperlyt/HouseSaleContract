package com.dgsoft.house.sale.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by cooper on 9/15/15.
 */
@Entity
@Table(name = "CONTRACT_NUMBER", catalog = "CONTRACT")
public class ContractNumber implements java.io.Serializable  {

    private String contractNumber;
    private HouseContract houseContract;

    @Id
    @Column(name = "CONTRACT_NUMBER", unique = true, nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTRACT_ID", nullable = false)
    @NotNull
    public HouseContract getHouseContract() {
        return houseContract;
    }

    public void setHouseContract(HouseContract houseContract) {
        this.houseContract = houseContract;
    }
}
