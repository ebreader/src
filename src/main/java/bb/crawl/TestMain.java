package bb.crawl;


import bb.simple.SimpleNodeSelector;

public class TestMain {
    public static void main(String[] args){
        try {

            Crawler crawler=new Crawler();
            crawler.setCrawlingURL("https://ctext.org/zhuangzi/zhs");
            crawler.setBasicURL("https://ctext.org");
            SimpleNodeSelector selector=new SimpleNodeSelector();
            selector.parse("html.body.div[3].div[0].div[6].a(href!$=chapters/zhs){link=href,name=#}",null);
            crawler.setChapterSelector(selector);
            selector=new SimpleNodeSelector();
            selector.parse("#content2.table[1].tbody.tr.td[0-1]{name=%}",null);
            crawler.setTitleSelector(selector);
            selector=new SimpleNodeSelector();
            selector.parse("#content3.table[2].tbody.tr.td(class=ctext){content=%}",null);
            crawler.setContentSelector(selector);



            crawler.crawling();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
