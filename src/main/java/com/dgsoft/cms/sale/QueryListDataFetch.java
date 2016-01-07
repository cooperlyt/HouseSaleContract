package com.dgsoft.cms.sale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by cooper on 12/22/15.
 */
public abstract class QueryListDataFetch extends DataFetch {


    protected abstract Map<String, String> getSearchParam();


    private Integer firstResult = 0;

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> result = getSearchParam();
        result.put("firstResult", String.valueOf(firstResult));
        return result;
    }

    private JSONObject resultJson;

    public void refresh() {
        resultJson = null;
    }

    public JSONObject getResultJson() {
        if (resultJson == null)
            resultJson = getJsonData();
        return resultJson;
    }

    public JSONArray getResultData() {
        try {
            return getResultJson().getJSONArray("datas");
        } catch (JSONException e) {
            return null;
        }
    }

    public List<Map<String, Object>> getResultMap() {

        JSONArray jsonArray = getResultData();
        if (jsonArray == null){
            return new ArrayList<Map<String, Object>>(0);
        }
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>(jsonArray.length());

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Map<String,Object> resultObj = new HashMap<String, Object>(jsonObject.length());
                Iterator it = jsonObject.keys();
                while (it.hasNext()){
                    String key = (String)it.next();
                    resultObj.put(key,jsonObject.get(key));
                }
                result.add(resultObj);
            }
            return result;
        } catch (JSONException e) {
            throw new IllegalArgumentException("data error");
        }
    }

    public Integer getPageCount() {
        try {
            return getResultJson().getInt("pageCount");
        } catch (JSONException e) {
            return 0;
        }
    }

    public Integer getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(Integer firstResult) {
        this.firstResult = firstResult;
        refresh();
    }

    public Integer getMaxResults() {
        try {
            return getResultJson().getInt("pageSize");
        } catch (JSONException e) {
            return 0;
        }
    }

    public Long getResultCount(){
        try {
            return getResultJson().getLong("recordCount");
        } catch (JSONException e) {
            return Long.valueOf(0);
        }
    }

    public boolean isPreviousExists(){
        try {
            return getResultJson().getBoolean("previousExists");
        } catch (JSONException e) {
            return false;
        }
    }

    public boolean isNextExists(){
        try {
            return getResultJson().getBoolean("nextExists");
        } catch (JSONException e) {
            return false;
        }
    }

    public Long getPage() {

        if ((getFirstResult() == null) || getFirstResult().equals(0)) {
            return Long.valueOf(1);
        }

        return getFirstResult().longValue() / getMaxResults().longValue() + 1;
    }

    private Long maxPageNavCount = Long.valueOf(10);

    public Long getMaxPageNavCount() {
        return maxPageNavCount;
    }

    public void setMaxPageNavCount(Long maxPageNavCount) {
        this.maxPageNavCount = maxPageNavCount;
    }


    private List<Long> showPageNavNumbers;

    public List<Long> getShowPageNavNumbers() {
        initShowPageNavNumbers();
        return showPageNavNumbers;
    }

    private void initShowPageNavNumbers() {
        if (showPageNavNumbers == null) {
            long halfCount = (getMaxPageNavCount().longValue() - 1) / 2;
            long beginPage = getPage().longValue() - halfCount;
            if (beginPage <= 0) {
                beginPage = 1;
            }
            if ((getPageCount().longValue() - beginPage + 1) < getMaxPageNavCount()) {
                beginPage = getPageCount().longValue() - (getMaxPageNavCount() - 1);
            }
            if (beginPage <= 0) {
                beginPage = 1;
            }


            showPageNavNumbers = new ArrayList<Long>(getMaxPageNavCount().intValue());
            for (int i = 0; i < getMaxPageNavCount(); i++) {
                if (beginPage > getPageCount()) {
                    break;
                }
                showPageNavNumbers.add(Long.valueOf(beginPage));

                beginPage++;
            }

            if (showPageNavNumbers.size() > 0 && (showPageNavNumbers.get(0).longValue() > 1)) {
                if (showPageNavNumbers.get(0).longValue() != 2) {
                    showPageNavNumbers.set(0, Long.valueOf(showPageNavNumbers.get(0).longValue() * -1));
                }
                showPageNavNumbers.add(0, Long.valueOf(1));
            }

            if (showPageNavNumbers.size() > 1 &&
                    (showPageNavNumbers.get(showPageNavNumbers.size() - 1).longValue() != getPageCount())) {
                if (showPageNavNumbers.get(showPageNavNumbers.size() - 1).longValue() != (getPageCount().longValue() - 1)) {
                    showPageNavNumbers.set(showPageNavNumbers.size() - 1, Long.valueOf(showPageNavNumbers.get(showPageNavNumbers.size() - 1).longValue() * -1));
                }
                showPageNavNumbers.add(Long.valueOf(getPageCount()));
            }
        }
    }


}
