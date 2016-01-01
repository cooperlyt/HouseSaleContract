package com.dgsoft.cms.sale;

import com.dgsoft.cms.sale.exceptions.OwnerServerException;
import org.jboss.seam.annotations.Name;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

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
        result.put("buildId", buildId == null ? "" : buildId);
        result.put("projectId", projectId == null ? "" : projectId);
        result.put("cardId", cardId == null ? "" : cardId);
        return result;
    }


    private List<Map.Entry<String,String>> buildList;

    public List<Map.Entry<String, String>> getBuildList() {
        if (buildList == null){
            initData();
        }
        return buildList;
    }

    public String getBuildName(){
        if (buildName == null){
            initData();
        }

        return buildName;
    }


    private List<GridMapData> mapDatas;

    private String projectName;

    private String buildName;

    public String getProjectName() {
        if (projectName == null){
            initData();
        }
        return projectName;
    }

    protected void initData() {
        buildList = new ArrayList<Map.Entry<String, String>>();




        mapDatas = new ArrayList<GridMapData>();
        JSONObject jsonObject = getJsonData();



        try {

            projectName = jsonObject.getString("projectName");
            buildName = jsonObject.getString("buildName");
            JSONArray builds = jsonObject.getJSONArray("builds");
            Map<String,String> gridMap = new HashMap<String, String>(builds.length());
            for(int i=0; i< builds.length(); i++){
                JSONObject buildObj =  builds.getJSONObject(i);
                gridMap.put(buildObj.getString("id"),buildObj.getString("buildName"));
            }
            buildList.addAll(gridMap.entrySet());
            Collections.sort(buildList, new Comparator<Map.Entry<String, String>>() {
                @Override
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                    return o1.getKey().compareTo(o2.getKey());
                }
            });

            JSONArray pageArray = jsonObject.getJSONArray("gridMaps");
            for (int i = 0; i < pageArray.length(); i++) {
                mapDatas.add(new GridMapData(pageArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            throw new OwnerServerException(e);
        }

    }

    public GridMapData getResultGridMap(){
        for(GridMapData gridMapData: getMapDatas()){
            if (gridMapData.getIndex().equals(pageIndex)){
                return gridMapData;
            }
        }
        if (!getMapDatas().isEmpty()){
            pageIndex = getMapDatas().get(0).getIndex();
            return getMapDatas().get(0);
        }
        return null;
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

        private String title;

        private Integer index;

        private int colCount;

        private List<Map<String,Object>> heads;

        private List<GridMapRow> rows;

        public GridMapData(JSONObject gridMapData) throws JSONException {
            title = gridMapData.getString("title");
            index = gridMapData.getInt("index");
            heads = jsonObjectArrayToList(gridMapData.getJSONArray("head"),new SimapleJSONObjectConverter());
            colCount = gridMapData.getInt("colCount");

            JSONArray rowArray = gridMapData.getJSONArray("rows");
            rows = new ArrayList<GridMapRow>(rowArray.length());
            for(int i=0 ; i<rowArray.length(); i++){
                rows.add(new GridMapRow(rowArray.getJSONObject(i)));
            }


        }

        public String getTitle() {
            return title;
        }

        public Integer getIndex() {
            return index;
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
