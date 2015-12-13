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

        //getInstance().setType(ArticleCategory.CategoryType.News);
        return true;
    }


    public String getTypeName(){
        if (getInstance().getType() == null){
            return null;
        }
        return getInstance().getType().name();
    }

    public void setTypeName(String name){
        if (name == null || name.trim().equals("")){
            getInstance().setType(null);
        }
        getInstance().setType(ArticleCategory.CategoryType.valueOf(name));
    }
}
