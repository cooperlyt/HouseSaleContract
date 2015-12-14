package com.dgsoft.cms.action;

import com.dgsoft.cms.model.Article;
import com.dgsoft.common.utils.seam.MultiOperatorEntityQuery;
import com.dgsoft.common.utils.seam.RestrictionGroup;

import java.util.Arrays;

/**
 * Created by cooper on 12/13/15.
 */
public abstract class ArticleQuery extends MultiOperatorEntityQuery<Article> {


    public ArticleQuery() {
        RestrictionGroup mainRestriction = new RestrictionGroup("and", Arrays.asList(RESTRICTIONS));
        //mainRestriction.getChildren().add();
        setRestrictionGroup(mainRestriction);
        setRestrictionLogicOperator("and");
        //setOrderColumn("a.publishTime");
        setOrder("a.fixTop desc, a.publishTime desc, a.id ");
        setMaxResults(10);
    }

    protected static final String[] RESTRICTIONS = {
            "a.category.id = #{articleCategoryHome.instance.id}"
    };





    private String searchKey;

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }
}
