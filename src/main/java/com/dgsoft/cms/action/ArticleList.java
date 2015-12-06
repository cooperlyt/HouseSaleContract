package com.dgsoft.cms.action;

import com.dgsoft.cms.model.Article;
import com.dgsoft.cms.model.ArticleCategory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import java.util.Arrays;

/**
 * Created by cooper on 12/6/15.
 */
@Name("articleList")
public class ArticleList extends EntityQuery<Article>{


    private static final String EJBQL = "select a from Article a ";

    private static final String[] RESTRICTIONS = {
            "a.category.id = #{articleList.categoryId}"
    };

    public ArticleList() {
        setEjbql(EJBQL);
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
        setRestrictionLogicOperator("and");
        setOrderColumn("a.publishTime");
        setOrderDirection("desc");
        setMaxResults(10);
    }


    private String searchKey;

    private String categoryId;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public String getCategoryName(){
        if (categoryId == null || categoryId.trim().equals("")){
            return null;
        }
        ArticleCategory category = getEntityManager().find(ArticleCategory.class,categoryId);
        if (category == null){
            return null;
        }
        return category.getName();
    }
}
