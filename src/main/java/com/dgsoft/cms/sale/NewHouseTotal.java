package com.dgsoft.cms.sale;

import org.jboss.seam.annotations.Name;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cooper on 12/20/15.
 */
@Name("newHouseTotal")
public class NewHouseTotal extends DataFetch{


    @Override
    protected String getDataType() {
        return "TOTAL_INFO";
    }

    @Override
    protected Map<String, String> getParams() {
        return new HashMap<String, String>(0);
    }

    private TotalData totalData;

    public TotalData getTotalData() {
        if (totalData == null){
            totalData = new TotalData(getJsonData());
        }
        return totalData;
    }


    public static class TotalData{

        private JSONObject jsonObject;

        public TotalData(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        public Long getProjectCount(){
            try {
                return jsonObject.getLong("projectCount");
            } catch (JSONException e) {
                return Long.valueOf(0);
            }
        }

        public Long getBuildCount(){
            try {
                return jsonObject.getLong("buildCount");
            } catch (JSONException e) {
                return Long.valueOf(0);
            }
        }

        //总套数
        public Long getTotalCount(){
            try {
                return jsonObject.getLong("allCount");
            } catch (JSONException e) {
                return Long.valueOf(0);
            }
        }

        //总住宅套数
        public Long getHomeCount(){
            try {
                return jsonObject.getLong("homeCount");
            } catch (JSONException e) {
                return Long.valueOf(0);
            }
        }

        //非住宅套数
        public Long getOtherCount(){
            return getTotalCount() - getHomeCount();
        }

        //销售套数
        public Long getSaleCount(){
            try {
                return jsonObject.getLong("saleCount");
            } catch (JSONException e) {
                return Long.valueOf(0);
            }
        }

        //销售住宅套数
        public Long getSaleHomeCount(){
            try {
                return jsonObject.getLong("saleHomeCount");
            } catch (JSONException e) {
                return Long.valueOf(0);
            }
        }

        //可售套数
        public Long getUnsaleCount(){
            Long result = getTotalCount() - getSaleCount();
            if (result > 0)
                return result;
            else
                return Long.valueOf(0);
        }

        //销售非住宅套数
        public Long getSaleOtherCount(){
            return getSaleCount() - getSaleHomeCount();
        }

        //可售住宅套数
        public Long getUnsaleHomeCount(){
            Long result = getHomeCount() - getSaleHomeCount();
            if (result > 0)
                return result;
            else
                return Long.valueOf(0);
        }

        //可售非住宅套数
        public Long getUnsaleOtherCount(){
            Long result = getUnsaleCount() - getUnsaleHomeCount();
            if (result > 0)
                return result;
            else
                return Long.valueOf(0);
        }

        //总面积
        public BigDecimal getTotalArea(){
            try {
                return new BigDecimal(jsonObject.getString("allArea"));
            } catch (JSONException e) {
                return BigDecimal.ZERO;
            }
        }

        //销售面积
        public BigDecimal getSaleArea(){
            try {
                return new BigDecimal(jsonObject.getString("saleArea"));
            } catch (JSONException e) {
                return BigDecimal.ZERO;
            }
        }

        //销售住宅面积
        public BigDecimal getSaleHomeArea(){
            try {
                return new BigDecimal(jsonObject.getString("saleHomeArea"));
            } catch (JSONException e) {
                return BigDecimal.ZERO;
            }
        }

        //可售面积
        public BigDecimal getUnsaleArea(){
            BigDecimal result = getTotalArea().subtract(getSaleArea());
            if (result.compareTo(BigDecimal.ZERO) > 0)
                return result;
            else
                return BigDecimal.ZERO;
        }


        //住宅面积
        public BigDecimal getHomeArea(){
            try {
                return new BigDecimal(jsonObject.getString("homeArea"));
            } catch (JSONException e) {
                return BigDecimal.ZERO;
            }
        }

        //非住宅面积
        public BigDecimal getOtherArea(){
            BigDecimal result = getTotalArea().subtract(getHomeArea());
            if (result.compareTo(BigDecimal.ZERO) > 0)
                return result;
            else
                return BigDecimal.ZERO;
        }

        //销售非住宅面积
        public BigDecimal getSaleOtherArea(){
            BigDecimal result = getSaleArea().subtract(getSaleHomeArea());
            if (result.compareTo(BigDecimal.ZERO) > 0)
                return result;
            else
                return BigDecimal.ZERO;
        }

        //可售住宅面积
        public BigDecimal getUnsaleHomeArea(){
            BigDecimal result = getSaleArea().subtract(getSaleHomeArea());
            if (result.compareTo(BigDecimal.ZERO) > 0)
            return result;
            else
                return BigDecimal.ZERO;
        }

        //可售非住宅面积
        public BigDecimal getUnsaleOtherArea(){
            BigDecimal result = getUnsaleArea().subtract(getUnsaleHomeArea());
            if (result.compareTo(BigDecimal.ZERO) > 0)
            return result;
            else
                return BigDecimal.ZERO;
        }

        //销售金额
        public BigDecimal getSaleMoney(){
            try {
                return new BigDecimal(jsonObject.getString("saleMoney"));
            } catch (JSONException e) {
                return BigDecimal.ZERO;
            }
        }

        //住宅销售金额
        public BigDecimal getSaleHomeMoney(){
            try {
                return new BigDecimal(jsonObject.getString("saleHomeMoney"));
            } catch (JSONException e) {
                return BigDecimal.ZERO;
            }
        }

        //非住宅销售金额
        public BigDecimal getSaleOtherMoney(){
            BigDecimal result = getSaleMoney().subtract(getSaleHomeMoney());
            if (result.compareTo(BigDecimal.ZERO) > 0)
            return result;
            else
                return BigDecimal.ZERO;
        }

    }

}
