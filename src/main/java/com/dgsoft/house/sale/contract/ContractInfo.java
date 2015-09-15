package com.dgsoft.house.sale.contract;

/**
 * Created by cooper on 9/14/15.
 */
public interface ContractInfo {

    String getProjectCode();

    void setProjectCode(String code);

    String getContext();

    void setContext(String context);

    int getContractVersion();

    void setContractVersion(int version);

    ContractType getType();

    void setType(ContractType type);


}
