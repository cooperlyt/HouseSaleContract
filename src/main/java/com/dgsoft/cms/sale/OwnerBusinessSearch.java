package com.dgsoft.cms.sale;

import org.jboss.seam.annotations.Name;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cooper on 1/1/16.
 */
@Name("ownerBusinessSearch")
public class OwnerBusinessSearch extends DataFetch{

    private String businessId;

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    @Override
    protected String getDataType() {
        return "BUSINESS_SEARCH";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String,String> result = new HashMap<String, String>(1);
        result.put("id",businessId == null ? "" : businessId);
        return result;
    }

    private Map<String,Object> resultData;


    public boolean isNoResult(){
        return getResultData().isEmpty();
    }

    public void refresh(){
        resultData = null;
    }


    public Map<String, Object> getResultData() {
        if (resultData == null){

            try {
                resultData = jsonObjectToMap(getJsonData(), new JSONObjectConverter() {
                    @Override
                    public Object convert(JSONObject jsonObject, String key) throws JSONException {
                        if ("oper".equals(key)){

                            return jsonObjectArrayToList(jsonObject.getJSONArray(key),new SimapleJSONObjectConverter());

                        }else{
                            return jsonObject.get(key);
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
                resultData = new HashMap<String, Object>(0);
            }
        }
        return resultData;
    }
}
