package http.server;

import com.sun.javaws.exceptions.InvalidArgumentException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpMessageHeader {

    private final Map<String, String> fields;
    private HttpMessageType httpMessageType;

    public HttpMessageHeader(HttpMessageType httpMessageType) {
        this.fields = new HashMap<>();
        this.httpMessageType = httpMessageType;
    }

    public HttpMessageHeader(List<String> fieldsLines, HttpMessageType httpMessageType) throws Exception {
        this(httpMessageType);
        for (String fieldLine : fieldsLines) {
            try {
                Pattern pattern = Pattern.compile("(.*): (.*)");
                Matcher matcher = pattern.matcher(fieldLine);
                if (matcher.find()) {
                    String key = matcher.group(1);
                    String value = matcher.group(2);
                    this.addField(key, value);
                } else {
                    throw new IllegalStateException();
                }
            } catch (IllegalStateException e) {
                throw new IllegalStateException("The field \"" + fieldLine + "\" is an invalid header field format", e);
            }
        }
    }

    public Map<String, String> getFields() {
        return new HashMap<>(this.fields);
    }

    public HttpMessageType getHttpMessageType() {
        return this.httpMessageType;
    }

    public void setHttpMessageType(HttpMessageType httpMessageType) {
        this.httpMessageType = httpMessageType;
    }

    public boolean hasField(String keyField) {
        return this.fields.containsKey(keyField);
    }

    public String getField(String keyField) {
        return this.fields.get(keyField);
    }

    public String getFieldLine(String keyField) {
        return keyField + ": " + this.getField(keyField) + "\r\n";
    }

    public void addField(String key, String value) throws Exception {
        HttpMessageField httpMessageType;
        if (this.httpMessageType == HttpMessageType.REQUEST) {
            httpMessageType = HttpRequestHeaderField.getFieldFromName(key);
        } else {
            httpMessageType = HttpResponseHeaderField.getFieldFromName(key);
        }
        if (httpMessageType == null) {
            throw new Exception("Invalid field name: " + key);
        }
        this.fields.put(httpMessageType.getFieldName(), value);
    }

    public void addField(HttpMessageField httpMessageField, String value) {
        this.fields.put(httpMessageField.getFieldName(), value);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{"
                + "fields=" + this.fields
                + "}";
    }
}
