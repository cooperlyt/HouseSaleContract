package com.dgsoft.house.sale;

import com.dgsoft.house.sale.model.SystemParam;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.framework.EntityQuery;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: cooperlee
 * Date: 6/8/13
 * Time: 3:52 PM
 */
@Name("runParam")
@AutoCreate
@Scope(ScopeType.APPLICATION)
@Startup
public class RunParam {


    @In(create = true)
    private EntityManager entityManager;

    private Map<String, SystemParam> systemParams = new HashMap<String, SystemParam>();

    @Create
    @Transactional
    public void load() {

        loadParams();

    }

    private void loadParams(){
        systemParams.clear();
        for (SystemParam param : entityManager.createQuery("select param from SystemParam param",SystemParam.class).getResultList()) {
            systemParams.put(param.getId(), param);
        }

    }

    public void refresh(){
        loadParams();
    }




    public String getParamValue(String name){
        SystemParam systemParam = systemParams.get(name);
        if (systemParam == null){
            throw new IllegalArgumentException("not have system Param:" + name);
        }else{
            return systemParam.getValue();
        }
    }


    public static RunParam instance()
    {
        if ( !Contexts.isEventContextActive() )
        {
            throw new IllegalStateException("no active event context");
        }
        return (RunParam) Component.getInstance(RunParam.class, ScopeType.APPLICATION);
    }

}
