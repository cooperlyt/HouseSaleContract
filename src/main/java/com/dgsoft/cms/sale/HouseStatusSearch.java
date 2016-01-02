package com.dgsoft.cms.sale;

import com.dgsoft.cms.sale.exceptions.OwnerServerException;
import org.jboss.seam.annotations.Name;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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


    private Map<String,Object> resultData;

    public boolean isNoResult(){
        return getResultData().isEmpty();
    }

    public Map<String, Object> getResultData() {
        if (resultData == null){
            if (ownerCardNumber == null || ownerCardNumber.trim().equals("") ||
                    ownerPersonNumber == null || ownerPersonNumber.trim().equals("")){
                resultData = new HashMap<String, Object>(0);
            }else{
                try {
                    resultData = jsonObjectToMap(getJsonData(), new JSONObjectConverter() {
                        @Override
                        public Object convert(JSONObject jsonObject, String key) throws JSONException {
                            if ("status".equals(key)){
                                JSONArray jsonArray = jsonObject.getJSONArray(key);
                                List<String> result = new ArrayList<String>(jsonArray.length());
                                for(int i=0;i<jsonArray.length();i++){
                                    result.add(jsonArray.getString(i));
                                }
                                return result;

                            }else{
                                return jsonObject.get(key);
                            }
                        }
                    });
                } catch (JSONException e) {
                    throw new OwnerServerException(e);
                }
            }
        }
        return resultData;
    }
}
