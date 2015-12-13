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


    public ArticleList() {
        super();
        setEjbql(EJBQL);
    }



}
