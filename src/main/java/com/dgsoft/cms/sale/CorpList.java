package com.dgsoft.cms.sale;

import com.dgsoft.house.AttachCorpType;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;

import java.util.HashMap;
import java.util.Map;

@Name("corpList")
public class CorpList extends QueryListDataFetch{

  @Factory(value = "corpTypes", scope = ScopeType.APPLICATION)
  public AttachCorpType[] getAttachCorpTypes(){
    return AttachCorpType.values();
  }

  private String searchKey;
  private String typeName;

  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  @Override
  protected String getDataType() {
    return "CORP_REGISTER_LIST";
  }

  @Override
  protected Map<String, String> getSearchParam() {
    Map<String,String> result = new HashMap<String, String>();
    result.put("searchKey",(searchKey == null) ? "" : searchKey);
    result.put("typeName",(typeName == null) ? AttachCorpType.DEVELOPER.name() : typeName);
    return result;
  }
}
