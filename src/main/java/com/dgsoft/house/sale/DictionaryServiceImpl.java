package com.dgsoft.house.sale;

import com.dgsoft.common.system.DictionaryService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import java.net.MalformedURLException;

/**
 * Created by cooper on 9/18/15.
 */

@Name("dictionary")
@AutoCreate
@Scope(ScopeType.APPLICATION)
public class DictionaryServiceImpl extends DictionaryService {


    public DictionaryServiceImpl() throws MalformedURLException {
    }

    @Override
    protected String getWsdlLocation() {
        return "http://localhost:8080/HMPLAT/DictionaryWS?wsdl";
    }
}
