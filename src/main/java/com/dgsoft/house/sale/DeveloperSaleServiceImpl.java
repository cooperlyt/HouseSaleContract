package com.dgsoft.house.sale;

import com.dgsoft.common.system.RunParam;
import com.dgsoft.developersale.*;

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
