package http.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The class represents the header section of an HTTP message (request & response)
 *
 * @author Loïc DUBOIS-TERMOZ
 * @author Alexandre DUFOUR
 */
public class HttpMessageHeader {

    private final Map<String, String> fields;
    private HttpMessageType httpMessageType;

    /**
     * The constructor of an HttpMessageHeader instance
     *
     * @param httpMessageType the type of the message (REQUEST or RESPONSE)
     */
    public HttpMessageHeader(HttpMessageType httpMessageType) {
        this.fields = new HashMap<>();
        this.httpMessageType = httpMessageType;
    }

    /**
     * The constructor of an HttpMessageHeader instance.
     * The fields lines specified in parameters are check before added in the object and if the format is not
     * the one expected an exception is raises
     *
     * @param fieldsLines     the fields lines where each field line must have the format (.*): (.*) to be valid
     * @param httpMessageType the HTTP message type (RESPONSE or REQUEST) associated with this header
     * @throws Exception if the field is invalid (wrong format or unknown field)
     */
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

    /**
     * Returns a clone of the header fields list
     *
     * @return a clone of the header fields list
     */
    public Map<String, String> getFields() {
        return new HashMap<>(this.fields);
    }

    /**
     * Returns the HTTP message type associated with this header
     *
     * @return the HTTP message type associated with this header
     */
    public HttpMessageType getHttpMessageType() {
        return this.httpMessageType;
    }

    /**
     * Sets the HTTP message type associated with this header
     *
     * @param httpMessageType the HTTP message type associated with this header
     */
    public void setHttpMessageType(HttpMessageType httpMessageType) {
        this.httpMessageType = httpMessageType;
    }

    /**
     * Searches and returns the field value associated with the specified key
     *
     * @param keyField the key of the targeted field value
     * @return the field value of the specified key if exists
     */
    public String getField(String keyField) {
        if (this.fields.containsKey(keyField)) {
            return this.fields.get(keyField);
        }
        return null;
    }

    /**
     * Forms and returns the field line from the specified key field
     *
     * @param keyField the key field used to gets the field and form it
     * @return the field line corresponding to the specified key field
     */
    public String getFieldLine(String keyField) {
        return keyField + ": " + this.getField(keyField) + "\r\n";
    }

    /**
     * Adds the field (key + value) to this header if it's a valid field header
     *
     * @param key   the key of the field to add to this header
     * @param value the value of the field to add to this header
     * @throws Exception if the field key is unknown for the server
     */
    public void addField(String key, String value) throws Exception {
        HttpMessageField httpMessageType;
        if (this.httpMessageType == HttpMessageType.REQUEST) {
            httpMessageType = HttpRequestHeaderField.getFieldFromName(key);
        } else {
            httpMessageType = HttpResponseHeaderField.getFieldFromName(key);
        }
        if (httpMessageType == null) {
            throw new Exception("Unknown field key: " + key);
        }
        this.fields.put(httpMessageType.getFieldName(), value);
    }

    /**
     * Adds the field (key + value) to this header if the specified HTTP message field enum instance isn't wrong
     *
     * @param httpMessageField the HTTP message field to add to this header if has the good instance
     * @param value            the value of the field to add to this header
     * @throws Exception if the HTTP message field enum instance is wrong
     */
    public void addField(HttpMessageField httpMessageField, String value) throws Exception {
        if (this.httpMessageType == HttpMessageType.REQUEST && httpMessageField instanceof HttpResponseHeaderField
                || this.httpMessageType == HttpMessageType.RESPONSE && httpMessageField instanceof HttpRequestHeaderField) {
            throw new Exception("The specified HTTP message field object is instance of the wrong enum");
        }
        this.fields.put(httpMessageField.getFieldName(), value);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{"
                + "fields=" + this.fields + ","
                + "httpMessageType=" + this.httpMessageType
                + "}";
    }
}
