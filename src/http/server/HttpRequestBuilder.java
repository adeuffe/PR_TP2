package http.server;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is a controller for reading and building an HTTP request
 *
 * @author Loïc DUBOIS-TERMOZ
 * @author Alexandre DUFOUR
 */
public class HttpRequestBuilder {

    /**
     * Reads the HTTP request from the specified BufferedReader
     *
     * @param in the reader of the input of the HTTP socket server
     * @return a list of strings where each of them represents a line of the HTTP request
     * @throws Exception if I/O exception or the HTTP request received is invalid
     */
    public static List<String> readRequest(BufferedReader in) throws Exception {
        List<String> requestStr = new ArrayList<>();
        String line = in.readLine();
        int length = 0;
        while (line != null && !line.equals("")) {
            Pattern pattern = Pattern.compile("(.*): (.*)");
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && matcher.group(1).equalsIgnoreCase("Content-Length")) {
                length = Integer.parseInt(matcher.group(2));
            }
            requestStr.add(line);
            line = in.readLine();
        }
        if (length > 0) {
            requestStr.add("");
            char[] data = new char[length];
            in.read(data, 0, length);
            String requestBody = new String(data);
            requestStr.add(requestBody);
        }
        return requestStr;
    }

    /**
     * Builds the HTTP request from it string form
     *
     * @param requestStr the string form of the HTTP request to build
     * @return the HTTP request built from the specified string form
     * @throws Exception if the string form is invalid
     */
    public static HttpRequest buildRequest(List<String> requestStr) throws Exception {
        if (requestStr.isEmpty()) {
            return null;
        }
        String requestLine = extractRequestLine(requestStr);
        List<String> requestHeader = extractRequestHeader(requestStr);
        String requestBody = extractRequestBody(requestStr);
        return new HttpRequest(requestLine, requestHeader, requestBody);
    }

    /**
     * Extracts the first line of the string HTTP request (the request line)
     *
     * @param requestStr the string form of the HTTP request
     * @return the request line of the HTTP request
     */
    public static String extractRequestLine(List<String> requestStr) {
        return requestStr.get(0);
    }

    /**
     * Extracts the request header lines from the specified HTTP request form
     *
     * @param requestStr the HTTP request form
     * @return the list of lines of the header of the specified HTTP request
     */
    public static List<String> extractRequestHeader(List<String> requestStr) {
        List<String> requestHeader = new ArrayList<>(requestStr.size());
        for (int i = 1; i < requestStr.size(); i++) {
            String requestLine = requestStr.get(i);
            if (requestLine.equals("")) {
                // End of the header section of the request reached, stop the extraction
                break;
            }
            requestHeader.add(requestLine);
        }
        return requestHeader;
    }

    /**
     * Extracts the request body lines from the specified HTTP request form
     *
     * @param requestStr the HTTP request form
     * @return the body data of the specified HTTP request
     */
    public static String extractRequestBody(List<String> requestStr) {
        String requestBody = "";
        boolean isBodySection = false;
        for (int i = 1; i < requestStr.size(); i++) {
            String requestLine = requestStr.get(i);
            if (!isBodySection) {
                if (requestLine.equals("")) {
                    // Body section reached
                    isBodySection = true;
                }
                continue;
            }
            requestBody = requestLine;
        }
        return requestBody;
    }
}
