package com.dgsoft.house.sale.developer;

import com.dgsoft.developersale.*;
import com.dgsoft.house.BuildGridMapBlockInfo;
import com.dgsoft.house.BuildGridMapInfo;
import com.dgsoft.house.BuildGridMapRowInfo;
import com.dgsoft.house.sale.DeveloperSaleServiceImpl;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by cooper on 9/11/15.
 */
@Name("buildSaleGridMap")
public class BuildSaleGridMap {

    @In
    private LogonInfo logonInfo;

    private String buildCode;

    @In
    private EntityManager entityManager;

    //private SaleBuildGridMap saleBuildGridMap;
    private SaleBuild saleBuild;

    private List<SaleBuildGridMap> saleBuildGridMapList = null;

    private int pageIndex = 0;

    public String getBuildCode() {
        return buildCode;
    }

    public void setBuildCode(String buildCode) {
        if (buildCode == null || buildCode.trim().equals("") || !buildCode.equals(this.buildCode)){
            saleBuildGridMapList = null;
            pageIndex = 0;
        }
        this.buildCode = buildCode;
    }

    public boolean isBuildDefined(){
        return buildCode != null && !buildCode.trim().equals("");
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        if (isBuildDefined() && (this.pageIndex != pageIndex)){
            saleBuildGridMapList = null;
        }
        this.pageIndex = pageIndex;
    }

    public List<SaleBuildGridMap> getSaleBuildGridMapList() {
        if (isBuildDefined() && (saleBuildGridMapList == null)){
            saleBuildGridMapList = DeveloperSaleServiceImpl.instance().getBuildGridMap(buildCode);

            List<String> prepareContractHouseCodes = entityManager.createQuery("select contract.houseCode from HouseContract contract where contract.status = 'PREPARE' and contract.groupId = :groupId", String.class)
                    .setParameter("groupId", logonInfo.getGroupCode()).getResultList();

            for(SaleBuildGridMap saleBuildGridMap: saleBuildGridMapList){
                for(BuildGridMapRowInfo row: saleBuildGridMap.getRows()){
                    for(BuildGridMapBlockInfo block: row.getBlocks()){
                        if (block.getHouse() != null && ((SaleHouse)block.getHouse()).getStatus().isCanSale()){
                            if (prepareContractHouseCodes.contains(block.getHouse().getHouseCode())){
                                ((SaleHouse) block.getHouse()).setStatus(SaleStatus.PREPARE_CONTRACT);
                            }
                        }
                    }
                }
            }



        }
        return saleBuildGridMapList;
    }


    public SaleBuildGridMap getSaleBuildGridMap(){
        List<SaleBuildGridMap> result = getSaleBuildGridMapList();

        if (!isBuildDefined() || (result == null) ||
                result.isEmpty() || ( pageIndex > (getSaleBuildGridMapList().size() - 1))){
            return null;

        }
        return result.get(pageIndex);
    }

    public SaleBuild getSelectBuild() {
        if (isBuildDefined()){
            for(SaleProject project: ((DeveloperLogonInfo)logonInfo).getSaleProjects()) {
                for (SaleBuild build : project.getSaleBuildList()) {
                    if (build.getBuildCode().equals(buildCode)) {
                        return build;
                    }
                }
            }
        }

        return null;

    }
}
