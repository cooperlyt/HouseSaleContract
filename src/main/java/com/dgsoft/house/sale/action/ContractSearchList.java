package com.dgsoft.house.sale.action;

import cc.coopersoft.house.sale.data.HouseContract;
import com.dgsoft.common.utils.seam.MultiOperatorEntityQuery;
import com.dgsoft.common.utils.seam.RestrictionGroup;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;

import java.util.Arrays;

/**
 * Created by cooper on 11/8/15.
 */
@Name("contractSearchList")
public class ContractSearchList extends MultiOperatorEntityQuery<HouseContract> {

    private static final String EJBQL = "select c from HouseContract c " +
            "left join fetch c.newHouseContract nc " +

            "left join fetch c.saleProxyPerson " +
            "where c.groupId = #{logonInfo.groupCode}";


    private static final String[] RESTRICTIONS = {
            "lower(c.id) = lower(#{contractSearchList.searchKey})",
            "lower(c.houseCode) = lower(#{contractSearchList.searchKey})",
            "lower(c.contractIndex) like lower(concat('%:\"',#{contractSearchList.searchKey},'\"%'))) "
    };


    private String searchKey;

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public OrderBy getOrderBy(){
        for(OrderBy orderBy: OrderBy.values()){
            if (orderBy.getPath().equals(getOrderColumn())){
                return orderBy;
            }
        }
        return null;
    }

    public void setOrderBy(OrderBy orderBy){
        if (orderBy != null){
            setOrderColumn(orderBy.getPath());
        }else{
            setOrderColumn(null);
        }
    }

    public enum OrderBy{

        CONTRACT_CREATE_TIME("c.createTime"), CONTRACT_ID("c.id");

        private String path;

        public String getPath() {
            return path;
        }

        OrderBy(String path) {
            this.path = path;
        }
    }

    @Factory(value = "contractOrders",scope = ScopeType.SESSION)
    public OrderBy[] getOrders(){
        return OrderBy.values();
    }


    public ContractSearchList() {

            setEjbql(EJBQL);

            setOrderColumn(OrderBy.CONTRACT_CREATE_TIME.getPath());
            setOrderDirection("desc");

            setRestrictionLogicOperator("and");

            setRestrictionGroup(new RestrictionGroup("or", Arrays.asList(RESTRICTIONS)));




    }
}
