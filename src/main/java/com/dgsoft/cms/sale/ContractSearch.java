package com.dgsoft.cms.sale;

import org.jboss.seam.annotations.Name;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cooper on 1/1/16.
 */
@Name("contractSearch")
public class ContractSearch extends DataFetch {

    private String contractNumber;

    private String personNumber;

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getPersonNumber() {
        return personNumber;
    }

    public void setPersonNumber(String personNumber) {
        this.personNumber = personNumber;
    }

    @Override
    protected String getDataType() {
        return "CONTRACT_SEARCH";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String,String> result = new HashMap<String, String>(2);
        result.put("contractNumber",contractNumber);
        result.put("personNumber",personNumber);
        return result;
    }
}
