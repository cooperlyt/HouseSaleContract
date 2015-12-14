package com.dgsoft.common.system;

import com.dgsoft.house.sale.model.SystemParam;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;


/**
 * Created by cooper on 12/14/15.
 */
@Name("systemParamList")
public class SystemParamList extends EntityQuery<SystemParam>{

    private static final String EJBQL = "select param from SystemParam param";


    public SystemParamList() {
        setEjbql(EJBQL);
        setRestrictionLogicOperator("and");
        //setOrderColumn("a.publishTime");
        setOrder("param.id");
        setMaxResults(null);
    }

    public void save(){
        getEntityManager().flush();
        RunParam.instance().refresh();
    }

    private String selectId;
    private String imgUrl;

    public String getSelectId() {
        return selectId;
    }

    public void setSelectId(String selectId) {
        this.selectId = selectId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void saveImg(){
        SystemParam param = getEntityManager().find(SystemParam.class, selectId);
        param.setValue(imgUrl);
        getEntityManager().flush();
        RunParam.instance().refresh();
    }
}
