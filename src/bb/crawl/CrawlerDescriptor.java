package bb.crawl;

import java.util.Properties;

public class CrawlerDescriptor {
    private String name;
    private String website;
    private String crawlingURL;
    private String basicURL;
    private String titleSelector;
    private String chapterSelector;
    private String contentSelector;
    private boolean singleThread;
    public void init(Properties properties){
        name=properties.getProperty("name");
        website=properties.getProperty("website");
        crawlingURL=properties.getProperty("crawlingURL");
        basicURL=properties.getProperty("basicURL");
        titleSelector=properties.getProperty("titleSelector");
        chapterSelector=properties.getProperty("chapterSelector");
        contentSelector=properties.getProperty("contentSelector");
        if(basicURL.startsWith("#")){
            singleThread=true;
            basicURL=basicURL.replace("#","");
        }else{
            singleThread=false;
        }
    }
    public boolean isSingleThread(){
        return singleThread;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setCrawlingURL(String crawlingURL) {
        this.crawlingURL = crawlingURL;
    }

    public void setBasicURL(String basicURL) {
        this.basicURL = basicURL;
        if(basicURL.startsWith("#")){
            singleThread=true;
            this.basicURL=basicURL.replace("#","");
        }else{
            singleThread=false;
        }
    }

    public void setTitleSelector(String titleSelector) {
        this.titleSelector = titleSelector;
    }

    public void setChapterSelector(String chapterSelector) {
        this.chapterSelector = chapterSelector;
    }

    public void setContentSelector(String contentSelector) {
        this.contentSelector = contentSelector;
    }

    public String getName() {
        return name;
    }
    public String getWebsite() {
        return website;
    }
    public String getCrawlingURL() {
        return crawlingURL;
    }
    public String getBasicURL() {
        return basicURL;
    }
    public String getTitleSelector() {
        return titleSelector;
    }
    public String getChapterSelector() {
        return chapterSelector;
    }
    public String getContentSelector() {
        return contentSelector;
    }
}
