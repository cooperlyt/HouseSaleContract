package com.dgsoft.house.sale.model;

import com.dgsoft.house.PoolType;
import com.dgsoft.house.sale.contract.ContractInfo;
import com.dgsoft.house.sale.contract.ContractType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by cooper on 9/14/15.
 */
@Entity
@Table(name = "HOUSE_CONTRACT", catalog = "CONTRACT")
public class HouseContract implements java.io.Serializable, ContractInfo {

    public enum ContractStatus{
        PREPARE, SUBMIT, RECORD
    }

    private String id;
    private String projectCode;
    private String HouseCode;
    private ContractType type;
    private String password;
    private Date createTime;
    private ContractStatus status;
    private String attachEmpId;
    private String attachEmpName;
    private BigDecimal price;
    private String context;
    private int contractVersion;
    private int version;

    private PoolType poolType;

    private ContractOwner contractOwner;
    private Set<BusinessPool> businessPools = new HashSet<BusinessPool>(0);
    private Set<ContractNumber> contractNumbers = new HashSet<ContractNumber>(0);

    public HouseContract() {
    }

    public HouseContract(String id, String projectCode, Date createTime, ContractStatus status, String attachEmpId, String attachEmpName) {
        this.id = id;
        this.projectCode = projectCode;
        this.createTime = createTime;
        this.status = status;
        this.attachEmpId = attachEmpId;
        this.attachEmpName = attachEmpName;
    }

    @Id
    @Column(name = "ID", unique = true, nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    @Column(name = "PROJECT_ID",nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    public String getProjectCode() {
        return projectCode;
    }

    @Override
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    @Column(name = "HOUSE_CODE", nullable = false,length = 32, unique = true)
    @NotNull
    @Size(max = 32)
    public String getHouseCode() {
        return HouseCode;
    }

    public void setHouseCode(String houseCode) {
        HouseCode = houseCode;
    }

    @Override
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE",length = 20, nullable = false)
    @NotNull
    public ContractType getType() {
        return type;
    }

    @Override
    public void setType(ContractType type) {
        this.type = type;
    }

    @Column(name = "PASSWORD", length = 50, nullable = false)
    @NotNull
    @Size(max = 50)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TIME",nullable = false)
    @NotNull
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 20, nullable = false)
    @NotNull
    public ContractStatus getStatus() {
        return status;
    }

    public void setStatus(ContractStatus status) {
        this.status = status;
    }

    @Column(name = "ATTACH_EMP_ID", nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    public String getAttachEmpId() {
        return attachEmpId;
    }

    public void setAttachEmpId(String attachEmpId) {
        this.attachEmpId = attachEmpId;
    }

    @Column(name ="ATTACH_EMP_NAME", nullable = false, length = 50)
    public String getAttachEmpName() {
        return attachEmpName;
    }

    public void setAttachEmpName(String attachEmpName) {
        this.attachEmpName = attachEmpName;
    }

    @Column(name = "CONTRACT_PRICE", nullable = false, length = 21, scale = 3)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "CONTRACT", columnDefinition = "LONGTEXT")
    public String getContext() {
        return context;
    }

    @Override
    public void setContext(String context) {
        this.context = context;
    }

    @Override
    @Column(name = "CONTRACT_VERSION", nullable = false)
    public int getContractVersion() {
        return contractVersion;
    }

    @Override
    public void setContractVersion(int contractVersion) {
        this.contractVersion = contractVersion;
    }

    @Version
    @Column(name = "VERSION",nullable = false)
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }


    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    public ContractOwner getContractOwner() {
        return contractOwner;
    }

    public void setContractOwner(ContractOwner contractOwner) {
        this.contractOwner = contractOwner;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "POOL_TYPE", length = 32, nullable = false)
    @NotNull
    public PoolType getPoolType() {
        return poolType;
    }

    public void setPoolType(PoolType poolType) {
        this.poolType = poolType;
    }

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true, mappedBy = "houseContract")
    public Set<BusinessPool> getBusinessPools() {
        return businessPools;
    }

    public void setBusinessPools(Set<BusinessPool> businessPools) {
        this.businessPools = businessPools;
    }

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true, mappedBy = "houseContract")
    public Set<ContractNumber> getContractNumbers() {
        return contractNumbers;
    }

    public void setContractNumbers(Set<ContractNumber> contractNumbers) {
        this.contractNumbers = contractNumbers;
    }
}
