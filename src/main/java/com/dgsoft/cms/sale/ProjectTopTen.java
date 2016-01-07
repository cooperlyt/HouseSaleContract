package com.dgsoft.cms.sale;

import com.dgsoft.cms.sale.exceptions.OwnerServerException;
import org.jboss.seam.annotations.Name;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cooper on 12/21/15.
 */
@Name("projectTopTen")
public class ProjectTopTen extends DataFetch{

    public enum TotalTimeType {
        YEAR("project_year_top_10"), QUARTER("project_quarter_top_10"), MONTH("project_month_top_10");

        private String msgKey;

        public String getMsgKey() {
            return msgKey;
        }

        TotalTimeType(String msgKey) {
            this.msgKey = msgKey;
        }
    }

    public TotalTimeType[] getTimeTypes(){
        return TotalTimeType.values();
    }

    @Override
    protected String getDataType() {
        return "PROJECT_SALE_TOP_TEN";
    }

    private TotalTimeType type = TotalTimeType.YEAR;

    public TotalTimeType getType() {
        return type;
    }

    public void setType(TotalTimeType type) {
        this.type = type;
    }

    private Map<String,String> params;

    @Override
    protected Map<String, String> getParams() {
        if (params == null){
            params = new HashMap<String, String>(1);
            params.put("totalTime",type.name());
        }
        return params;
    }

    public void refresh(){
        params = null;
        countTotalData = null;
        areaTotalData = null;
        moneyTotalData = null;
    }

    private List<ProjectSaleTotalData> countTotalData;

    private List<ProjectSaleTotalData> areaTotalData;

    private List<ProjectSaleTotalData> moneyTotalData;

    private void initData(){
      JSONObject data = getJsonData();
        try {

            JSONArray jsonArray = data.getJSONArray("count");
            countTotalData = new ArrayList<ProjectSaleTotalData>(10);
            for (int i = 0 ; i < jsonArray.length(); i++){
                countTotalData.add(new ProjectSaleTotalData(jsonArray.getJSONObject(i).getString("id"),
                        jsonArray.getJSONObject(i).getString("name"),
                        jsonArray.getJSONObject(i).getLong("count")));
            }
        } catch (JSONException e) {
            countTotalData = new ArrayList<ProjectSaleTotalData>(0);
        }
        try{
            areaTotalData = new ArrayList<ProjectSaleTotalData>(10);
            JSONArray jsonArray = data.getJSONArray("area");
            for (int i = 0 ; i < jsonArray.length(); i++){
                areaTotalData.add(new ProjectSaleTotalData(jsonArray.getJSONObject(i).getString("id"),
                        jsonArray.getJSONObject(i).getString("name"),
                        new BigDecimal(jsonArray.getJSONObject(i).getString("area"))));
            }
        } catch (JSONException e) {
            areaTotalData = new ArrayList<ProjectSaleTotalData>(0);
        }
        try{
            moneyTotalData = new ArrayList<ProjectSaleTotalData>(10);
            JSONArray jsonArray = data.getJSONArray("money");
            for (int i = 0 ; i < jsonArray.length(); i++){
                moneyTotalData.add(new ProjectSaleTotalData(jsonArray.getJSONObject(i).getString("id"),
                        jsonArray.getJSONObject(i).getString("name"),
                        new BigDecimal(jsonArray.getJSONObject(i).getString("money"))));
            }
        } catch (JSONException e) {
            moneyTotalData = new ArrayList<ProjectSaleTotalData>(0);
        }
    }

    public List<ProjectSaleTotalData> getCountTotalData() {
        if (countTotalData == null){
            initData();
        }
        return countTotalData;
    }

    public List<ProjectSaleTotalData> getAreaTotalData() {
        if (areaTotalData == null){
            initData();
        }
        return areaTotalData;
    }

    public List<ProjectSaleTotalData> getMoneyTotalData() {
        if (moneyTotalData == null){
            initData();
        }
        return moneyTotalData;
    }

    public class ProjectSaleTotalData{

        private String id;

        private String name;

        private Long longValue;

        private BigDecimal decimalValue;

        public ProjectSaleTotalData(String id, String name, Long longValue) {
            this.id = id;
            this.name = name;
            this.longValue = longValue;
        }

        public ProjectSaleTotalData(String id, String name, BigDecimal decimalValue) {
            this.id = id;
            this.name = name;
            this.decimalValue = decimalValue;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Long getLongValue() {
            return longValue;
        }

        public BigDecimal getDecimalValue() {
            return decimalValue;
        }
    }





}
