package com.dgsoft.cms.action;

import com.dgsoft.cms.model.ArticleCategory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;


/**
 * Created by cooper on 12/8/15.
 */
@Name("articleCategoryList")
public class ArticleCategoryList extends EntityQuery<ArticleCategory> {

    private static final String EJBQL = "select ac from ArticleCategory ac where ac.type = 'News'";

    public ArticleCategoryList() {
        setEjbql(EJBQL);
        //setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
        setRestrictionLogicOperator("and");
        setOrderColumn("ac.pri");
        //setOrderDirection("desc");
        setMaxResults(null);
    }



}
