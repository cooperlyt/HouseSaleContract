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
public class ArticleList extends ArticleQuery {


    private static final String EJBQL = "select a from Article a where a.category.type = 'News'";


    protected static final String[] RESTRICTIONS2 = {
            "lower(a.mainTitle) like lower(concat('%',#{articleList.searchKey},'%')) ",
            "lower(a.subTitle) like lower(concat('%',#{articleList.searchKey},'%')) ",
            "lower(a.summary) like lower(concat('%',#{articleList.searchKey},'%'))"
    };

    public ArticleList() {
        super();
        getRestrictionGroup().getChildren().add(new RestrictionGroup("or", Arrays.asList(RESTRICTIONS2)));
        setEjbql(EJBQL);
    }



}
