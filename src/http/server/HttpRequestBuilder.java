package http.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpRequestBuilder {

    public static List<String> readRequest(BufferedReader in) throws IOException {
        List<String> requestStr = new ArrayList<>();
        String line = in.readLine();
        while (line != null && !line.equals("")) {
            requestStr.add(line);
            line = in.readLine();
        }
        return requestStr;
    }

    public static HttpRequest buildRequest(List<String> requestStr) {
        String requestGeneralHeader = extractRequestGeneralHeader(requestStr);
        List<String> requestHeader = extractRequestHeader(requestStr);
        List<String> requestBody = extractRequestBody(requestStr);
        return new HttpRequest(requestGeneralHeader, requestHeader, requestBody);
    }

    public static String extractRequestGeneralHeader(List<String> requestStr) {
        return requestStr.get(0);
    }

    public static List<String> extractRequestHeader(List<String> requestStr) {
        List<String> requestHeader = new ArrayList<>(requestStr.size());
        for (int i = 1; i < requestStr.size(); i++) {
            String requestLine = requestStr.get(i);
            if (requestLine.equals("")) {
                // Body section of the request reached, stop the extraction
                break;
            }
            requestHeader.add(requestLine);
        }
        return requestHeader;
    }

    public static List<String> extractRequestBody(List<String> requestStr) {
        List<String> requestBody = new ArrayList<>(requestStr.size());
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
            requestBody.add(requestLine);
        }
        return requestBody;
    }
}
