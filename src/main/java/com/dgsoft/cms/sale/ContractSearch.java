package com.dgsoft.cms.sale;

import com.dgsoft.cms.sale.exceptions.OwnerServerException;
import com.dgsoft.house.sale.model.HouseContract;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private Map<String,Object> resultData;

    @In(create = true)
    private EntityManager entityManager;

    public boolean isNoResult(){
        return getResultData().isEmpty();
    }

    public Map<String, Object> getResultData() {
        if (resultData == null){
            if (contractNumber == null || contractNumber.trim().equals("") ||
                    personNumber == null || personNumber.trim().equals("")){
                resultData = new HashMap<String, Object>(0);
            }else{
                try {
                    resultData = jsonObjectToMap(getJsonData(), new SimapleJSONObjectConverter());

                    HouseContract contract = entityManager.find(HouseContract.class, contractNumber);
                    if(contract == null){
                        resultData.put("contract",new ArrayList<String>(0));
                    }else{
                        resultData.put("contract",contract.getContractNumberList());
                        resultData.put("version",contract.getContractVersion());
                        
                    }

                } catch (JSONException e) {
                    throw new OwnerServerException(e);
                }
            }
        }
        return resultData;
    }
}
