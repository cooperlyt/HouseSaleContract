package com.dgsoft.cms.sale;

import com.dgsoft.cms.sale.exceptions.OwnerServerException;
import com.dgsoft.common.system.RunParam;
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
import java.util.Map;

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

}
