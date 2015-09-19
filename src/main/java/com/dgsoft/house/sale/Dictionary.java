package com.dgsoft.house.sale;

import com.dgsoft.common.system.DictionaryService;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Unwrap;
import org.jboss.seam.log.Logging;


/**
 * Created by cooper on 9/18/15.
 */

@Name("dictionary")
public class Dictionary {

    private final static DictionaryService dictionaryService = new DictionaryService(){

        @Override
        protected String getWsdlLocation() {
            Logging.getLog(getClass()).debug("call -------------------------------");
            return "http://localhost:8080/HMPLAT/DictionaryWS?wsdl";
        }
    };


    @Unwrap
    public DictionaryService getDictionaryService(){
        return dictionaryService;
    }


}
