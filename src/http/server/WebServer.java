package http.server;

import http.server.exceptions.HttpBadRequestException;
import http.server.exceptions.HttpForbiddenException;
import http.server.exceptions.HttpIllegalResourceException;
import http.server.exceptions.HttpTeapotException;
import http.server.request.HttpRequest;
import http.server.request.HttpRequestBuilder;
import http.server.response.HttpResponse;
import http.server.response.HttpResponseBuilder;
import http.server.response.HttpResponseHeaderField;

import java.io.*;
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

    public static final boolean MAINTENANCE = false;

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

            HttpResponse httpResponse = new HttpResponse();
            httpResponse.setStatusCode(500);
            httpResponse.setProtocolVersion("HTTP/1.0");
            httpResponse.getHttpMessageHeader().addField(HttpResponseHeaderField.SERVER, "Bot");
            if (!MAINTENANCE) {
                try {
                    List<String> requestStr = HttpRequestBuilder.readRequest(in);
                    HttpRequest httpRequest = HttpRequestBuilder.buildRequest(requestStr);
                    System.out.println(httpRequest);

                    if (httpRequest != null) {
                        treatRequest(httpRequest, httpResponse);
                    }
                } catch (HttpForbiddenException e) {
                    e.printStackTrace();
                    httpResponse.setStatusCode(403);
                } catch (HttpIllegalResourceException e) {
                    e.printStackTrace();
                    httpResponse.setStatusCode(451);
                } catch (HttpTeapotException e) {
                    e.printStackTrace();
                    httpResponse.setStatusCode(418);
                } catch (HttpBadRequestException | IllegalStateException e) {
                    e.printStackTrace();
                    httpResponse.setStatusCode(400);
                } catch (Exception e) {
                    e.printStackTrace();
                    httpResponse.setStatusCode(500);
                }
            } else {
                httpResponse.setStatusCode(503);
            }
            sendResponse(httpResponse, out);
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
        checkResource(httpRequest.getResource());
        switch (httpRequest.getHttpMethod()) {
            case HEAD: { /* fall down */ }
            case GET: {
                if (ResourceManager.isResourceExists(httpRequest.getResource())) {
                    if (!ResourceManager.isDynamicResource(httpRequest.getResource())) {
                        byte[] content = ResourceManager.readResource(httpRequest.getResource());
                        String contentType = HttpResponseBuilder.getContentType(httpRequest.getResource());
                        if (contentType != null) {
                            httpResponse.setStatusCode(200);
                            httpResponse.setContent(content, contentType);
                        } else {
                            httpResponse.setStatusCode(415);
                        }
                    } else {
                        String extension = ResourceManager.getFileExtension(httpRequest.getResource());
                        if (extension.equalsIgnoreCase("py")) {
                            byte[] content = executeDynamicResource(httpRequest.getResource(), httpRequest.getQueryString()).getBytes();
                            httpResponse.setStatusCode(200);
                            httpResponse.setContent(content, "text/html");
                        } else {
                            httpResponse.setStatusCode(415);
                        }
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
            case TRACE: { /* fall down */ }
            case OPTIONS: { /* fall down */ }
            case CONNECT: { /* fall down */ }
            case PATCH: {
                httpResponse.setStatusCode(501);
                break;
            }
        }

        if (httpRequest.getHttpMethod() == HttpMethod.HEAD) {
            httpResponse.removeContentOnly();
        }
    }

    /**
     * Executes a dynamic resource by creating a new process
     *
     * @param resource the dynamic resource
     * @param queryString teh query string to pass to the dynamic resource for it execution
     * @return the result of the execution of the dynamic resource
     * @throws IOException if an I/O exception is raised
     */
    public static String executeDynamicResource(String resource, String queryString) throws IOException {
        Process process = Runtime.getRuntime().exec("python " + ResourceManager.getResourcePath(resource).toString() + " " + queryString);
        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder res = new StringBuilder();
        String line = in.readLine();
        while (line != null) {
            res.append(line);
            line = in.readLine();
        }
        return res.toString();
    }

    /**
     * Checks the resource of the HTTP request and raise an exception if the file is unavailable
     *
     * @param resource the targeted resource by the HTTP request
     * @throws HttpIllegalResourceException if the resource is unavailable for legal reasons
     * @throws HttpForbiddenException if the resource is forbidden
     * @throws HttpTeapotException teapot easter egg
     */
    public static void checkResource(String resource) throws HttpIllegalResourceException, HttpForbiddenException, HttpTeapotException {
        File resourceFile = new File("resources/" + resource);
        File illegalFile = new File("resources/illegal.html");
        File privateFile = new File("resources/private.html");
        File teapotFile = new File("resources/teapot");
        if (resourceFile.compareTo(illegalFile) == 0) {
            throw new HttpIllegalResourceException();
        } else if (resourceFile.compareTo(privateFile) == 0) {
            throw new HttpForbiddenException();
        } else if (resourceFile.compareTo(teapotFile) == 0) {
            throw new HttpTeapotException();
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
