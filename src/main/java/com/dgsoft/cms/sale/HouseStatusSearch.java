package com.dgsoft.cms.sale;

import org.jboss.seam.annotations.Name;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cooper on 1/1/16.
 */
@Name("houseStatusSearch")
public class HouseStatusSearch extends DataFetch{

    private String ownerCardNumber;

    private String ownerPersonNumber;

    public String getOwnerCardNumber() {
        return ownerCardNumber;
    }

    public void setOwnerCardNumber(String ownerCardNumber) {
        this.ownerCardNumber = ownerCardNumber;
    }

    public String getOwnerPersonNumber() {
        return ownerPersonNumber;
    }

    public void setOwnerPersonNumber(String ownerPersonNumber) {
        this.ownerPersonNumber = ownerPersonNumber;
    }

    @Override
    protected String getDataType() {
        return "HOUSE_STATUS_SEARCH";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String,String> result = new HashMap<String, String>(2);
        result.put("cardNumber",ownerCardNumber);
        result.put("personNumber",ownerPersonNumber);
        return result;
    }
}
