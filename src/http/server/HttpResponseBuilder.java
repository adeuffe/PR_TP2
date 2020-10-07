package http.server;

public class HttpResponseBuilder {

    public static void setContentType(HttpResponse httpResponse, String contentType) {
        httpResponse.getHttpMessageHeader().addField("Content-Type", contentType);
    }

    public static void setContentLength(HttpResponse httpResponse, int contentLength) {
        httpResponse.getHttpMessageHeader().addField("Content-Length", Integer.toString(contentLength));
    }

    public static void setContentLocation(HttpResponse httpResponse, String location) {
        httpResponse.getHttpMessageHeader().addField("Content-Location", location);
    }
}
