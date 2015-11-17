package com.dgsoft.house.sale.model;

import com.dgsoft.house.SaleType;
import com.dgsoft.house.sale.contract.ContractInfo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by cooper on 9/14/15.
 */
@Entity
@Table(name = "CONTRACT_TEMPLATE", catalog = "CONTRACT")
public class ContractTemplate implements java.io.Serializable, ContractInfo{

    private String id;
    private String name;
    private String projectCode;
    private SaleType type;
    private int contractVersion;
    private String context;

    public ContractTemplate() {
    }

    public ContractTemplate(String projectCode) {
        this.projectCode = projectCode;
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

    @Column(name = "NAME", nullable = false, length = 50)
    @NotNull
    @Size(max = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "PROJECT_ID", nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }


    @Override
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false, length = 20)
    @NotNull
    public SaleType getType() {
        return type;
    }

    @Override
    public void setType(SaleType type) {
        this.type = type;
    }

    @Column(name = "CONTRACT_VERSION", nullable = false)
    public int getContractVersion() {
        return contractVersion;
    }

    public void setContractVersion(int contractVersion) {
        this.contractVersion = contractVersion;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "CONTRACT", columnDefinition = "LONGTEXT")
    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }


    @Transient
    public boolean isCurrentVersion(){
        return getContractVersion() == getType().getCurrentVersion();
    }
}
