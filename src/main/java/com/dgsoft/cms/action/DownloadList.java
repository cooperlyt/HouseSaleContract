package com.dgsoft.cms.action;

import com.dgsoft.common.utils.seam.RestrictionGroup;
import org.jboss.seam.annotations.Name;

import java.util.Arrays;

/**
 * Created by cooper on 12/13/15.
 */
@Name("downloadList")
public class DownloadList extends ArticleQuery{


    private static final String EJBQL = "select a from Article a where a.category.type = 'Download'";


    protected static final String[] RESTRICTIONS2 = {
            "lower(a.mainTitle) like lower(concat('%',#{downloadList.searchKey},'%')) ",
            "lower(a.summary) like lower(concat('%',#{downloadList.searchKey},'%'))"
    };

    public DownloadList() {
        super();
        getRestrictionGroup().getChildren().add(new RestrictionGroup("or", Arrays.asList(RESTRICTIONS2)));
        setEjbql(EJBQL);
    }

}
