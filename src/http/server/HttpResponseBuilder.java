package http.server;

public class HttpResponseBuilder {

    public static void setContentType(HttpResponse httpResponse, String contentType) {
        httpResponse.getHttpMessageHeader().addField("Content-type", contentType);
    }

    public static void setContentLength(HttpResponse httpResponse, int contentLength) {
        httpResponse.getHttpMessageHeader().addField("Content-length", Integer.toString(contentLength));
    }
}
