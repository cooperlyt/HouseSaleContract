package com.dgsoft.house.sale.model;

import com.dgsoft.common.system.PersonEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * Created by cooper on 9/15/15.
 */

@Entity
@Table(name = "BUSINESS_POOL", catalog = "CONTRACT")
public class BusinessPool implements PersonEntity, java.io.Serializable {

    private String id;
    private String personName;
    private PersonEntity.CredentialsType credentialsType;
    private String credentialsNumber;
    private String relation;
    private BigDecimal poolArea;
    private String perc;
    private String phone;

    private String legalPerson;

    private HouseContract houseContract;
    private ContractOwner.LegalType legalType;

    public BusinessPool() {
    }

    public BusinessPool(HouseContract houseContract) {
        this.houseContract = houseContract;
    }

    @Id
    @Column(name = "ID", unique = true, nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "NAME", nullable = false, length = 50)
    @NotNull
    @Size(max = 50)
    @Override
    public String getPersonName() {
        return this.personName;
    }

    @Override
    public void setPersonName(String name) {
        this.personName = name;
    }


    @Override
    @Enumerated(EnumType.STRING)
    @Column(name = "ID_TYPE", nullable = false, length = 32)
    @NotNull
    public PersonEntity.CredentialsType getCredentialsType() {
        return credentialsType;
    }

    @Override
    public void setCredentialsType(PersonEntity.CredentialsType credentialsType) {
        this.credentialsType = credentialsType;
    }

    @Override
    @Column(name = "ID_NO", nullable = false, length = 100)
    @NotNull
    @Size(max = 100)
    public String getCredentialsNumber() {
        return credentialsNumber;
    }

    @Override
    public void setCredentialsNumber(String cerdentialsNumber) {
        this.credentialsNumber = cerdentialsNumber;
    }

    @Column(name = "RELATION", length = 32)
    @Size(max = 32)
    public String getRelation() {
        return this.relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    @Column(name = "POOL_AREA", scale = 4)
    public BigDecimal getPoolArea() {
        return this.poolArea;
    }

    public void setPoolArea(BigDecimal poolArea) {
        this.poolArea = poolArea;
    }

    @Column(name = "PERC", length = 10)
    @Size(max = 10)
    public String getPerc() {
        return this.perc;
    }

    public void setPerc(String perc) {
        this.perc = perc;
    }

    @Column(name = "PHONE", nullable = true, length = 15)
    @Size(max = 15)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }



    @Column(name = "LEGAL_PERSON", nullable = true, length = 50)
    @Size(max = 50)
    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CONTRACT", nullable = false)
    @NotNull
    public HouseContract getHouseContract() {
        return houseContract;
    }

    public void setHouseContract(HouseContract houseContract) {
        this.houseContract = houseContract;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "LEGAL_TYPE",length = 20)
    public ContractOwner.LegalType getLegalType() {
        return legalType;
    }

    public void setLegalType(ContractOwner.LegalType legalType) {
        this.legalType = legalType;
    }
}
