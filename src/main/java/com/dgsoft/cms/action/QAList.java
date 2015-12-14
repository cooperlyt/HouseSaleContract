package com.dgsoft.cms.action;

import com.dgsoft.common.utils.seam.RestrictionGroup;
import org.jboss.seam.annotations.Name;

import java.util.Arrays;

/**
 * Created by cooper on 12/13/15.
 */
@Name("QAList")
public class QAList extends ArticleQuery{

    private static final String EJBQL = "select a from Article a where a.category.type = 'QA'";

    protected static final String[] RESTRICTIONS2 = {
            "lower(a.mainTitle) like lower(concat('%',#{QAList.searchKey},'%')) "
    };

    public QAList() {
        super();
        getRestrictionGroup().getChildren().add(new RestrictionGroup("or", Arrays.asList(RESTRICTIONS2)));
        setEjbql(EJBQL);
    }



}
