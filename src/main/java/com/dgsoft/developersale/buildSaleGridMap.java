package com.dgsoft.developersale;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import java.util.List;

/**
 * Created by cooper on 9/10/15.
 */
@Name("buildSaleGridMap")
public class buildSaleGridMap {

    @In
    private LogonInfo logonInfo;

    private String buildCode;

    //private SaleBuildGridMap saleBuildGridMap;
    private SaleBuild saleBuild;

    private List<SaleBuildGridMap>  saleBuildGridMapList = null;

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
        }
        return saleBuildGridMapList;
    }


    public SaleBuildGridMap getSaleBuildGridMap(){
        return getSaleBuildGridMapList().get(pageIndex);
    }

    public SaleBuild getSelectBuild() {
        if (isBuildDefined()){
            for(SaleBuild build: logonInfo.getSaleProject().getSaleBuildList()){
                if (build.getBuildCode().equals(buildCode)){
                    return build;
                }
            }
        }
        return null;

    }
}
