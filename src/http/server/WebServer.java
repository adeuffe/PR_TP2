package http.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * Example program from Chapter 1 Programming Spiders, Bots and Aggregators in
 * Java Copyright 2001 by Jeff Heaton
 * <p>
 * WebServer is a very simple web-server. Any request is responded with a very
 * simple web-page.
 *
 * @author Jeff Heaton
 * @version 1.0
 */
public class WebServer {

    /**
     * WebServer constructor.
     */
    protected void start() {
        ServerSocket s;

        System.out.println("Webserver starting up on port 80");
        System.out.println("(press ctrl-c to exit)");
        try {
            // create the main server socket
            s = new ServerSocket(3000);
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return;
        }

        System.out.println("Waiting for connection");
        for (; ; ) {
            try {
                // wait for a connection
                Socket remote = s.accept();
                // remote is now the connected socket
                System.out.println("Connection, sending data.");
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        remote.getInputStream()));
                PrintWriter out = new PrintWriter(remote.getOutputStream());

                ///// REQUEST - READ /////
                List<String> requestStr = HttpRequestBuilder.readRequest(in);
                HttpRequest httpRequest = HttpRequestBuilder.buildRequest(requestStr);

                if (httpRequest != null) {
                    ///// TREATMENT /////
                    HttpResponse httpResponse = new HttpResponse();
                    httpResponse.setProtocolVersion("HTTP/1.0");
                    httpResponse.getHttpMessageHeader().addField(HttpResponseHeaderField.SERVER, "Bot");

                    switch (httpRequest.getHttpMethod()) {
                        case HEAD: { /* fall down */ }
                        case GET: {
                            String content = ResourceManager.readResource(httpRequest.getResource());
                            httpResponse.setStatusCode(200);
                            httpResponse.setHttpResponseBody(content, "text/html");
                            break;
                        }
                        case POST: {
                            boolean isResourceExists = ResourceManager.isResourceExists(httpRequest.getResource());
                            if (isResourceExists) {
                                ResourceManager.appendResource(httpRequest.getResource(), httpRequest.getHttpRequestBody());
                                httpResponse.setStatusCode(204);
                            } else {
                                WebServer.doResourceAction(httpRequest, httpResponse, ResourceAction.CREATE, 201);
                            }
                            break;
                        }
                        case PUT: {
                            boolean isResourceExists = ResourceManager.isResourceExists(httpRequest.getResource());
                            if (isResourceExists) {
                                WebServer.doResourceAction(httpRequest, httpResponse, ResourceAction.REPLACE, 204);
                            } else {
                                WebServer.doResourceAction(httpRequest, httpResponse, ResourceAction.CREATE, 201);
                            }
                            break;
                        }
                        case DELETE: {
                            WebServer.doResourceAction(httpRequest, httpResponse, ResourceAction.DELETE, 204);
                            break;
                        }
                    }

                    if (httpRequest.getHttpMethod() == HttpMethod.HEAD) {
                        httpResponse.removeHttpResponseBody();
                    }

                    ///// RESPONSE - SEND /////
                    // Send the status line
                    out.println(httpResponse.getStatusLine());
                    // Send the response header
                    httpResponse.getHttpMessageHeader().getFields().forEach((key, value) -> {
                        out.println(httpResponse.getHttpMessageHeader().getFieldLine(key));
                    });
                    // This blank line signals the end of the headers
                    out.println("");
                    if (httpResponse.hasBodySection()) {
                        // Send the response body
                        out.println(httpResponse.getHttpResponseBody());
                    }
                }
                out.flush();
                remote.close();
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }
    }

    public static void doResourceAction(HttpRequest httpRequest, HttpResponse httpResponse, ResourceAction resourceAction, int successStatusCode) throws Exception {
        boolean isDone = false;
        try {
            switch (resourceAction) {
                case CREATE: {
                    isDone = ResourceManager.createResource(httpRequest.getResource(), httpRequest.getHttpRequestBody());
                    httpResponse.addHeaderField(HttpResponseHeaderField.CONTENT_LOCATION, httpRequest.getResource());
                    break;
                }
                case REPLACE: {
                    isDone = ResourceManager.replaceResource(httpRequest.getResource(), httpRequest.getHttpRequestBody());
                    httpResponse.addHeaderField(HttpResponseHeaderField.CONTENT_LOCATION, httpRequest.getResource());
                    break;
                }
                case DELETE: {
                    isDone = ResourceManager.deleteResource(httpRequest.getResource());
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (isDone) {
            httpResponse.setStatusCode(successStatusCode);
        } else {
            httpResponse.setStatusCode(500);
        }
    }

    /**
     * Start the application.
     *
     * @param args Command line parameters are not used.
     */
    public static void main(String[] args) {
        WebServer ws = new WebServer();
        ws.start();
    }
}
