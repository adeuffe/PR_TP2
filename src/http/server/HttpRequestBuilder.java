package http.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRequestBuilder {

    public static List<String> readRequest(BufferedReader in) throws IOException {
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
            int r = in.read(data, 0, length);
            String requestBody = new String(data);
            requestStr.add(requestBody);
        }
        return requestStr;
    }
    public static HttpRequest buildRequest(List<String> requestStr) {
        if (requestStr.isEmpty()) {
            return null;
        }
        String requestGeneralHeader = extractRequestGeneralHeader(requestStr);
        List<String> requestHeader = extractRequestHeader(requestStr);
        String requestBody = extractRequestBody(requestStr);
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
