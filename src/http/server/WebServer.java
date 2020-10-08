///A Simple Web Server (WebServer.java)

package http.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

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
                    httpResponse.getHttpMessageHeader().addField("Server", "Bot");

                    switch (httpRequest.getHttpMethod()) {
                        case GET: {
                            String data = readResource(httpRequest.getResource());
                            long dataLength = getDataLength(data);
                            httpResponse.setStatusCode(200);
                            HttpResponseBuilder.setContentType(httpResponse, "text/html");
                            httpResponse.setHttpResponseBody(data);
                            break;
                        }
                        case POST: {
                            boolean isResourceExists = ResourceManager.isResourceExists(httpRequest.getResource());
                            if (isResourceExists) {
                                ResourceManager.appendResource(httpRequest.getResource(), httpRequest.getHttpRequestBody());
                                httpResponse.setStatusCode(204);
                            } else {
                                boolean isCreated = ResourceManager.createResource(httpRequest.getResource(), httpRequest.getHttpRequestBody());
                                // TODO: What happens if isCreated == false?
                                httpResponse.setStatusCode(201);
                            }
                            break;
                        }
                        case PUT: {
                            boolean isResourceExists = ResourceManager.isResourceExists(httpRequest.getResource());
                            if (isResourceExists) {
                                boolean isReplaced = ResourceManager.replaceResource(httpRequest.getResource(), httpRequest.getHttpRequestBody());
                                // TODO: What happens if isReplaced == false?
                                httpResponse.setStatusCode(204);
                            } else {
                                boolean isCreated = ResourceManager.createResource(httpRequest.getResource(), httpRequest.getHttpRequestBody());
                                // TODO: What happens if isCreated == false?
                                httpResponse.setStatusCode(201);
                            }
                            HttpResponseBuilder.setContentLocation(httpResponse, httpRequest.getResource());
                            HttpResponseBuilder.setContentType(httpResponse, "text/html");
                            break;
                        }
                        case HEAD: {
                            readResource(httpRequest.getResource());
                            httpResponse.setStatusCode(200);
                            HttpResponseBuilder.setContentType(httpResponse, "text/html");
                            break;
                        }
                        case DELETE: {
                            boolean isDeleted = ResourceManager.deleteResource(httpRequest.getResource());
                            // TODO: What happens if isDeleted == false?
                            if (isDeleted) {
                                httpResponse.setStatusCode(204);
                            }
                            break;
                        }
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

    public static String setHtmlBodyMessage(String body) {
        String data = "<html>";
        data += "\n\r  <body>";
        data += "\n\r       " + body;
        data += "\n\r  </body>";
        data += "\n\r</html>";
        return data;
    }

    public static Path getResourcesPath() {
        return Paths.get("resources");
    }

    public static String readResource(String resource) throws FileNotFoundException {
        String dataList = "";
        Path resourcesPath = getResourcesPath();
        String resourcePath = Paths.get(resourcesPath.toAbsolutePath().normalize().toString(), resource).toAbsolutePath().normalize().toString();
        File resourceFile = new File(resourcePath);
        Scanner reader = new Scanner(resourceFile);
        while (reader.hasNextLine()) {
            String data = reader.nextLine();
            dataList = dataList.concat(data);
        }
        reader.close();
        return dataList;
    }

    public static long getDataLength(String data) {
        return data.getBytes(StandardCharsets.UTF_8).length;
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
