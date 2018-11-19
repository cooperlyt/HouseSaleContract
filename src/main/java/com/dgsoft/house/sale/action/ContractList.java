package com.dgsoft.house.sale.action;

import cc.coopersoft.house.sale.data.HouseContract;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import java.util.Arrays;

/**
 * Created by cooper on 9/21/15.
 */
@Name("contractList")
public class ContractList extends EntityQuery<HouseContract> {

    private static final int PAGE_ITEM_COUNT = 20;

    private static final String EJBQL = "select c from HouseContract c " +
            "left join fetch c.newHouseContract nc " +

            "left join fetch c.saleProxyPerson " ;

    private static final String[] RESTRICTIONS = {
            "c.groupId = #{logonInfo.groupCode}"
    };

    public ContractList() {
        setEjbql(EJBQL);
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
        setRestrictionLogicOperator("and");
        setOrderColumn("c.createTime");
        setOrderDirection("desc");
        setMaxResults(PAGE_ITEM_COUNT);
    }

    public void more(){
        setMaxResults(getMaxResults() + PAGE_ITEM_COUNT);
    }

}
