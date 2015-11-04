package com.dgsoft.house.sale;

import com.dgsoft.developersale.*;
import com.dgsoft.developersale.wsinterface.DESUtil;
import org.jboss.seam.log.Logging;
import org.json.JSONObject;

import java.net.MalformedURLException;

/**
 * Created by cooper on 9/8/15.
 */
public class DeveloperSaleServiceImpl extends DeveloperSaleService {

    private static DeveloperSaleService saleService;

    private DeveloperSaleServiceImpl() {
    }

    public static DeveloperSaleService instance(){
        if (saleService == null){
                saleService = new DeveloperSaleServiceImpl();

        }
        return saleService;
    }


    @Override
    protected String getWsdlLocation() {
        return RunParam.instance().getParamValue("server_address") +  "DeveloperService?wsdl";
    }
}
