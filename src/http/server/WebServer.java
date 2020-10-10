package http.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * The HTTP server
 *
 * @author Loïc DUBOIS-TERMOZ
 * @author Alexandre DUFOUR
 */
public class WebServer {

    /**
     * WebServer constructor
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
            treatConnection(s);
        }
    }

    /**
     * Waits and treats the connection of a client to this HTTP server
     *
     * @param s the socket of this HTTP server
     */
    public static void treatConnection(ServerSocket s) {
        try {
            // wait for a connection
            Socket remote = s.accept();
            // remote is now the connected socket
            System.out.println("Connection, sending data.");
            BufferedReader in = new BufferedReader(new InputStreamReader(remote.getInputStream()));
            OutputStream out = remote.getOutputStream();

            List<String> requestStr = HttpRequestBuilder.readRequest(in);
            HttpRequest httpRequest = HttpRequestBuilder.buildRequest(requestStr);

            if (httpRequest != null) {
                HttpResponse httpResponse = new HttpResponse();
                try {
                    treatRequest(httpRequest, httpResponse);
                } catch (Exception e) {
                    httpResponse.setStatusCode(500);
                }
                sendResponse(httpResponse, out);
            }
            out.flush();
            remote.close();
            System.out.println("Disconnection, wait an other connection...");
            System.out.println();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    /**
     * Treats the HTTP request and defines the HTTP response
     *
     * @param httpRequest  the HTTP request to treat
     * @param httpResponse the HTTP response to define
     * @throws Exception if an exception is raised during the treatment
     */
    public static void treatRequest(HttpRequest httpRequest, HttpResponse httpResponse) throws Exception {
        httpResponse.setProtocolVersion("HTTP/1.0");
        httpResponse.getHttpMessageHeader().addField(HttpResponseHeaderField.SERVER, "Bot");

        switch (httpRequest.getHttpMethod()) {
            case HEAD: { /* fall down */ }
            case GET: {
                if (ResourceManager.isResourceExists(httpRequest.getResource())) {
                    byte[] content = ResourceManager.readResource(httpRequest.getResource());
                    String contentType = HttpResponseBuilder.getContentType(httpRequest.getResource());
                    if (contentType != null) {
                        httpResponse.setStatusCode(200);
                        httpResponse.setContent(content, contentType);
                    } else {
                        httpResponse.setStatusCode(415);
                    }
                } else {
                    httpResponse.setStatusCode(404);
                }
                break;
            }
            case POST: {
                boolean isResourceExists = ResourceManager.isResourceExists(httpRequest.getResource());
                if (isResourceExists) {
                    ResourceManager.appendResource(httpRequest.getResource(), httpRequest.getContent());
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
                if (ResourceManager.isResourceExists(httpRequest.getResource())) {
                    WebServer.doResourceAction(httpRequest, httpResponse, ResourceAction.DELETE, 204);
                } else {
                    httpResponse.setStatusCode(404);
                }
                break;
            }
        }

        if (httpRequest.getHttpMethod() == HttpMethod.HEAD) {
            httpResponse.removeContentOnly();
        }
    }

    /**
     * Does a resource action among the list of {@link ResourceAction} available
     *
     * @param httpRequest       the HTTP request
     * @param httpResponse      the HTTP response to update
     * @param resourceAction    the resource action to realize
     * @param successStatusCode the status code to set if the actions is realized successfully
     * @throws IOException if an I/O exception is raised during creation an replace resource process
     */
    public static void doResourceAction(HttpRequest httpRequest, HttpResponse httpResponse, ResourceAction resourceAction, int successStatusCode) throws IOException {
        boolean isDone = false;
        switch (resourceAction) {
            case CREATE: {
                isDone = ResourceManager.createResource(httpRequest.getResource(), httpRequest.getContent());
                try {
                    httpResponse.addHeaderField(HttpResponseHeaderField.CONTENT_LOCATION, httpRequest.getResource());
                } catch (Exception e) {
                    /* ignored, will never happen */
                }
                break;
            }
            case REPLACE: {
                isDone = ResourceManager.replaceResource(httpRequest.getResource(), httpRequest.getContent());
                try {
                    httpResponse.addHeaderField(HttpResponseHeaderField.CONTENT_LOCATION, httpRequest.getResource());
                } catch (Exception e) {
                    /* ignored, will never happen */
                }
                break;
            }
            case DELETE: {
                isDone = ResourceManager.deleteResource(httpRequest.getResource());
                break;
            }
        }
        if (isDone) {
            httpResponse.setStatusCode(successStatusCode);
        } else {
            httpResponse.setStatusCode(500);
        }
    }

    public static void sendResponse(HttpResponse httpResponse, OutputStream out) throws IOException {
        // Send the status line
        System.out.println("Sent response:");
        out.write(httpResponse.getStatusLine().getBytes());
        System.out.print(httpResponse.getStatusLine());
        // Send the response header
        for (String keyField : httpResponse.getHttpMessageHeader().getFields().keySet()) {
            out.write(httpResponse.getHttpMessageHeader().getFieldLine(keyField).getBytes());
            System.out.print(httpResponse.getHttpMessageHeader().getFieldLine(keyField));
        }
        // This blank line signals the end of the headers
        out.write("\r\n".getBytes());
        System.out.println();
        if (httpResponse.hasBodySection()) {
            // Send the response body
            out.write(httpResponse.getContent());
            System.out.println("<The body>");
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
