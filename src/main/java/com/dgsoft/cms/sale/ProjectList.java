package com.dgsoft.cms.sale;

import org.jboss.seam.annotations.Name;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cooper on 12/22/15.
 */

@Name("projectList")
public class ProjectList extends QueryListDataFetch{


    @Override
    protected String getDataType() {
        return "PROJECT_LIST";
    }

    private String searchKey;

    private String districtCode;

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    @Override
    protected Map<String, String> getSearchParam() {
        Map<String,String> result = new HashMap<String, String>();
        result.put("searchKey",(searchKey == null) ? "" : searchKey);
        result.put("districtCode",(districtCode == null) ? "" : districtCode);
        return result;
    }
}
