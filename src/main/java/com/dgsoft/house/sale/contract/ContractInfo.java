package com.dgsoft.house.sale.contract;

import com.dgsoft.house.SaleType;

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

    SaleType getType();

    void setType(SaleType type);


}
