package com.dgsoft.cms.sale;

import com.dgsoft.cms.sale.exceptions.OwnerServerException;
import org.jboss.seam.annotations.Name;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by cooper on 12/29/15.
 */
@Name("projectHome")
public class ProjectHome extends DataFetch{

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    protected String getDataType() {
        return "PROJECT_INFO";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String,String> result = new HashMap<String, String>(1);
        result.put("id",id);
        return result;
    }

    private String projectName;

    public String getProjectName() {
        if (projectName == null){
            initData();
        }
        return projectName;
    }

    private List<Map<String,Object>> resultList;

    protected void initData(){

        try {
            JSONObject jsonObject = getJsonData();
            resultList = jsonObjectArrayToList(jsonObject.getJSONArray("projects"), new JSONObjectConverter() {
                @Override
                public Object convert(JSONObject jsonObject, String key) throws JSONException {
                    if ("builds".equals(key)) {
                        return jsonObjectArrayToList(jsonObject.getJSONArray(key), new SimapleJSONObjectConverter());
                    } else {
                        return jsonObject.get(key);
                    }
                }
            });
            projectName = jsonObject.getString("name");


        } catch (JSONException e) {
            throw new OwnerServerException(e);
        }
    }


    public List<Map<String,Object>> getResultList(){
        if (resultList == null) {
            initData();
        }
        return resultList;
    }



}
