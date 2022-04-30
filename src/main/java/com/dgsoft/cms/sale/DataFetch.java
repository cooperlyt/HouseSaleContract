package com.dgsoft.cms.sale;

import com.dgsoft.cms.sale.exceptions.OwnerServerException;
import com.dgsoft.common.system.RunParam;
import org.jboss.seam.log.Logging;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by cooper on 12/20/15.
 */
public abstract class DataFetch {

    protected abstract String getDataType();

    protected abstract Map<String,String> getParams();

    protected JSONObject getJsonData(){
        try {

            String address = RunParam.instance().getParamValue("saleInfoDataProviderURL");

            address += "?type=" + getDataType();

            for(Map.Entry<String,String> entry: getParams().entrySet()){
                address += "&"+entry.getKey() + "=" + entry.getValue();
            }

            URLEncoder.encode(address,"UTF-8");
            Logging.getLog(getClass()).debug("get data:" + address);

            URL url = new URL(address);

            URLConnection conn = url.openConnection();
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            String result = sb.toString();
            if (result == null || result.trim().equals("")){
                throw new OwnerServerException("no data result");
            }
            return new JSONObject(sb.toString());


        } catch (MalformedURLException e) {
            throw new OwnerServerException(e);
        } catch (UnsupportedEncodingException e) {
            throw new OwnerServerException(e);
        } catch (IOException e) {
            throw new OwnerServerException(e);
        } catch (JSONException e) {
            throw new OwnerServerException(e);
        }
    }


    public interface JSONObjectConverter {

        Object convert(JSONObject jsonObject,String key) throws JSONException;
    }

    public class SimapleJSONObjectConverter implements JSONObjectConverter{

        @Override
        public Object convert(JSONObject jsonObject,String key) throws JSONException {
            return jsonObject.get(key);
        }
    }

    protected Map<String,Object> jsonObjectToMap(JSONObject jsonObject, JSONObjectConverter converter) throws JSONException {
        Map<String,Object> result = new HashMap<String, Object>(jsonObject.length());
        Iterator it = jsonObject.keys();
        while (it.hasNext()){
            String key = (String) it.next();
            result.put(key,converter.convert(jsonObject,key));
        }
        return result;
    }

    protected List<Map<String,Object>> jsonObjectArrayToList(JSONArray jsonArray, JSONObjectConverter converter) throws JSONException {
        List<Map<String,Object>> result  = new ArrayList<Map<String, Object>>(jsonArray.length());
        for (int i = 0 ; i< jsonArray.length(); i++){
            result.add(jsonObjectToMap(jsonArray.getJSONObject(i),converter));
        }
        return result;
    }

    public List<Map<String,Object>> simpleJsonObjectArrayToList(JSONArray jsonArray){
        if (jsonArray == null){
            return new ArrayList<Map<String, Object>>(0);
        }
        try {
            return jsonObjectArrayToList(jsonArray,new SimapleJSONObjectConverter());
        } catch (JSONException e) {
            throw new IllegalArgumentException("data error");
        }
    }

}
