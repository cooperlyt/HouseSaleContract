package com.dgsoft.cms.action;

import com.dgsoft.cms.model.Article;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import javax.persistence.NoResultException;

/**
 * Created by cooper on 12/7/15.
 */
@Name("articleHome")
public class ArticleHome extends EntityHome<Article>{


//    public static void main(String[] args) {
//        // TODO Auto-generated method stub
//        String content="<p>郎酒15年陈红花郎酒53°500ML，楼兰蛇龙珠戈壁干红（铁盒）750ML，组合
//        价699元。</p>";
//        Pattern patt=Pattern.compile("<[^>]+>([^<]*)</[^>]+>");
//        Matcher m=patt.matcher(content);
//        while(m.find()){
//            content=content.replaceFirst("<[^>]+>([^<]*)</[^>]+>", m.group(1).toString());
//        }
//        System.out.println(content);
//        //郎酒15年陈红花郎酒53°500ML，楼兰蛇龙珠戈壁干红（铁盒）750ML，组合价699元。
//    }


    public String viewArticle(){
        if (isIdDefined()){
            if (!getInstance().getViewType().isOutView()){
                return "webViewArticle";
            }else{
                //TODO operation resource
                return "outView";
            }
        }
        return null;
    }

}
