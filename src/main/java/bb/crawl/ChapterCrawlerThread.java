package bb.crawl;

import bb.simple.SimpleException;

public class ChapterCrawlerThread implements Runnable {
    private CrawlChapter chapter;
    private String url;
    private CrawlBook book;
    private Thread thread;
    private Crawler crawler;

    public ChapterCrawlerThread(CrawlChapter chapter, String url, CrawlBook book, Crawler crawler) {
        this.chapter = chapter;
        this.url = url;
        this.book = book;
        this.thread = new Thread(this);
        this.crawler = crawler;
    }

    @Override
    public void run() {
        try {
            crawler.crawlingChapter(url,book,chapter);
        } catch (SimpleException e) {
            crawler.println(e.msg);
        }
    }


    public void start(){
        thread.start();
    }
    public void join() throws CrawlException {
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new CrawlException("crawling chapter thread join interrupt exception");
        }
    }
}
