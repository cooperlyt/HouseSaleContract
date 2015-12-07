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


    private static final String EJBQL = "select a from Article a ";

    private static final String[] RESTRICTIONS = {
            "a.category.id = #{articleList.categoryId}"
    };

    private static final String[] RESTRICTIONS2 = {
            "lower(a.mainTitle) like lower(concat('%',#{articleList.searchKey},'%'))) ",
            "lower(a.subTitle) like lower(concat('%',#{articleList.searchKey},'%'))) ",
            "lower(a.author) like lower(concat('%',#{articleList.searchKey},'%'))) ",
    };


    public ArticleList() {
        setEjbql(EJBQL);
        //setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
        RestrictionGroup mainRestriction = new RestrictionGroup("and",Arrays.asList(RESTRICTIONS));
        mainRestriction.getChildren().add(new RestrictionGroup("or",Arrays.asList(RESTRICTIONS2)));
        setRestrictionGroup(mainRestriction);
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
