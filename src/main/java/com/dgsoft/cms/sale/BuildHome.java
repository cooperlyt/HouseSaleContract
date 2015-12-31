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
 * Created by cooper on 12/31/15.
 */
@Name("buildHome")
public class BuildHome extends DataFetch {


    private String projectId;

    private String cardId;

    private String buildId;

    private int pageIndex;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getBuildId() {
        return buildId;
    }

    public void setBuildId(String buildId) {
        this.buildId = buildId;
    }

    @Override
    protected String getDataType() {
        return "BUILD_INFO";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> result = new HashMap<String, String>(3);
        result.put("buildId", buildId);
        result.put("projectId", projectId);
        result.put("cardId", cardId);
        return result;
    }


    private List<GridMapData> mapDatas;

    protected void initData() {
        mapDatas = new ArrayList<GridMapData>();
        JSONObject jsonObject = getJsonData();

        try {
            JSONArray pageArray = jsonObject.getJSONArray("gridMaps");
            for (int i = 0; i < pageArray.length(); i++) {
                mapDatas.add(new GridMapData(pageArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            throw new OwnerServerException(e);
        }

    }

    public List<GridMapData> getMapDatas(){
        if (mapDatas == null){
            initData();
        }
        return mapDatas;
    }

    public class GridMapRow{

        private String title;

        public List<Map<String,Object>> blocks;

        public GridMapRow(JSONObject jsonObject) throws JSONException {
            title = jsonObject.getString("title");
            blocks = jsonObjectArrayToList(jsonObject.getJSONArray("blocks"),new SimapleJSONObjectConverter());
        }

        public String getTitle() {
            return title;
        }

        public List<Map<String, Object>> getBlocks() {
            return blocks;
        }
    }

    public class GridMapData{

        private int colCount;

        private List<Map<String,Object>> heads;

        private List<GridMapRow> rows;

        public GridMapData(JSONObject gridMapData) throws JSONException {
            heads = jsonObjectArrayToList(gridMapData.getJSONArray("title"),new SimapleJSONObjectConverter());
            colCount = gridMapData.getInt("colCount");

            JSONArray rowArray = gridMapData.getJSONArray("rows");
            rows = new ArrayList<GridMapRow>(rowArray.length());
            for(int i=0 ; i<rowArray.length(); i++){
                rows.add(new GridMapRow(rowArray.getJSONObject(i)));
            }


        }

        public int getColCount() {
            return colCount;
        }

        public List<Map<String, Object>> getHeads() {
            return heads;
        }

        public List<GridMapRow> getRows() {
            return rows;
        }
    }


}
