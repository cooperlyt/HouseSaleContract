package com.dgsoft.cms.action;

import com.dgsoft.cms.model.ArticleCategory;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cooper on 12/6/15.
 */
@Name("cmsCategoryGroupList")
@Scope(ScopeType.SESSION)
public class CmsCategoryGroupList {

    private final static int COLUMN_COUNT = 5;

    private List<List<ArticleCategory>> result;

    private List<ArticleCategory> allCategorys;

    private void initResult(){
        if (result == null || allCategorys == null){
            result = new ArrayList<List<ArticleCategory>>(COLUMN_COUNT);
            for(int i=0; i<COLUMN_COUNT;i++){
                result.add(new ArrayList<ArticleCategory>());
            }
            allCategorys = ((EntityManager)Component.getInstance("entityManager",true)).createQuery("select category from ArticleCategory category where category.type = 'News' order by category.pri", ArticleCategory.class).getResultList();
            int index = 0;
            for(ArticleCategory c: allCategorys){
                List<ArticleCategory> col = result.get(index);
                col.add(c);
                index++;
                if (index >= COLUMN_COUNT){
                    index = 0;
                }

            }
        }
    }

    public List<ArticleCategory> getAllCategorys() {
        initResult();
        return allCategorys;
    }

    public List<List<ArticleCategory>> getResult() {
        initResult();
        return result;
    }
}
