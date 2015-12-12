package com.dgsoft.cms.action;

import com.dgsoft.cms.model.Article;
import com.dgsoft.cms.model.ArticleCategory;
import com.dgsoft.common.utils.seam.MultiOperatorEntityQuery;
import com.dgsoft.common.utils.seam.RestrictionGroup;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import java.util.Arrays;

/**
 * Created by cooper on 12/6/15.
 */
@Name("articleList")
public class ArticleList extends MultiOperatorEntityQuery<Article> {


    private static final String EJBQL = "select a from Article a where a.category.type = 'News'";

    private static final String[] RESTRICTIONS = {
            "a.category.id = #{articleCategoryHome.instance.id}"
    };

    private static final String[] RESTRICTIONS2 = {
            "lower(a.mainTitle) like lower(concat('%',#{articleList.searchKey},'%')) ",
            "lower(a.subTitle) like lower(concat('%',#{articleList.searchKey},'%')) ",
            "lower(a.summary) like lower(concat('%',#{articleList.searchKey},'%'))"
    };


    public ArticleList() {
        setEjbql(EJBQL);
        //setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
        RestrictionGroup mainRestriction = new RestrictionGroup("and",Arrays.asList(RESTRICTIONS));
        mainRestriction.getChildren().add(new RestrictionGroup("or",Arrays.asList(RESTRICTIONS2)));
        setRestrictionGroup(mainRestriction);
        setRestrictionLogicOperator("and");
        //setOrderColumn("a.publishTime");
        setOrder("a.fixTop desc, a.publishTime desc, a.id ");
        setMaxResults(10);
    }


    private String searchKey;

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }




}
