package bgzs;

import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.handler.CefCookieAccessFilter;
import org.cef.handler.CefResourceHandler;
import org.cef.handler.CefResourceRequestHandler;
import org.cef.misc.BoolRef;
import org.cef.misc.StringRef;
import org.cef.network.CefRequest;
import org.cef.network.CefResponse;
import org.cef.network.CefURLRequest;

import java.util.HashMap;
import java.util.Map;

public class BGZSResourceRequestHandler implements CefResourceRequestHandler {
    @Override
    public CefCookieAccessFilter getCookieAccessFilter(CefBrowser browser, CefFrame frame, CefRequest request) {
        return null;
    }

    @Override
    public boolean onBeforeResourceLoad(CefBrowser browser, CefFrame frame, CefRequest request) {
        String customUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36";
        Map<String, String> headers =new HashMap<>();
        request.getHeaderMap(headers);
        headers.put("User-Agent", customUserAgent);
        request.setHeaderMap(headers);
        return false;
    }

    @Override
    public CefResourceHandler getResourceHandler(CefBrowser browser, CefFrame frame, CefRequest request) {
        return null;
    }

    @Override
    public void onResourceRedirect(CefBrowser browser, CefFrame frame, CefRequest request, CefResponse response, StringRef new_url) {

    }

    @Override
    public boolean onResourceResponse(CefBrowser browser, CefFrame frame, CefRequest request, CefResponse response) {
        return false;
    }

    @Override
    public void onResourceLoadComplete(CefBrowser browser, CefFrame frame, CefRequest request, CefResponse response, CefURLRequest.Status status, long receivedContentLength) {

    }

    @Override
    public void onProtocolExecution(CefBrowser browser, CefFrame frame, CefRequest request, BoolRef allowOsExecution) {

    }
}
