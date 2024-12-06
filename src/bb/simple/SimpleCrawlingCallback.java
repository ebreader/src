package bb.simple;

import org.jsoup.nodes.Element;

public interface SimpleCrawlingCallback {
    void onLoadSuccess(Element element) throws SimpleException;

}
