package com.dgsoft.cms.action;

import com.dgsoft.cms.model.ArticleCategory;
import com.dgsoft.common.EntityHomeAdapter;
import org.jboss.seam.annotations.Name;

/**
 * Created by cooper on 12/8/15.
 */
@Name("articleCategoryHome")
public class ArticleCategoryHome extends EntityHomeAdapter<ArticleCategory> {

    @Override
    protected boolean verifyPersistAvailable() {
        getInstance().setPri(getEntityManager().createQuery("select max(ac.pri) from ArticleCategory ac" , Integer.class).getSingleResult());

        getInstance().setType(ArticleCategory.CategoryType.News);
        return true;
    }
}
