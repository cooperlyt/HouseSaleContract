package com.dgsoft.cms.action;

import com.dgsoft.cms.model.Article;
import com.dgsoft.common.EntityHomeAdapter;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cooper on 12/7/15.
 */
@Name("articleHome")
public class ArticleHome extends EntityHomeAdapter<Article>{


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

    private String htmlContent;

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    protected boolean verifyUpdateAvailable() {
        saveContent();
        return true;
    }


    protected boolean verifyPersistAvailable() {
        saveContent();
        return true;
    }

    private void saveContent(){
        if (Article.ArticleViewType.HTML.equals(getInstance().getViewType())){
            getInstance().setContext(htmlContent);
        }
        getInstance().setPublishTime(new Date());

    }

    @In(required = false)
    private ArticleCategoryHome articleCategoryHome;

    public String editArticle(){
        if (isIdDefined()){
            return "edit-" + getInstance().getCategory().getType().name() + "-article";
        }else{
            if (articleCategoryHome != null && articleCategoryHome.isIdDefined()){
                getInstance().setCategory(articleCategoryHome.getInstance());

                return "create-" + getInstance().getCategory().getType().name() + "-article";
            }else{
                return "create-article";
            }

        }

    }


    @Factory(scope = ScopeType.SESSION)
    public List<Article.ArticleViewType> getAllowEditorTypes(){
        List<Article.ArticleViewType> result = new ArrayList<Article.ArticleViewType>(5);
        result.add(Article.ArticleViewType.HTML);
        result.add(Article.ArticleViewType.PDF);
        result.add(Article.ArticleViewType.WORD);
        result.add(Article.ArticleViewType.TEXT);
        result.add(Article.ArticleViewType.URL_LINK);
        return result;
    }

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


    @Override
    public String saveOrUpdate(){
        super.saveOrUpdate();
        return "save-" + getInstance().getCategory().getType().name();
    }

}
