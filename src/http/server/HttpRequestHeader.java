package http.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRequestHeader {

    private final Map<String, String> fields = new HashMap<>();

    public HttpRequestHeader(List<String> fields) throws IllegalStateException {
        super();
        for (String field : fields) {
            try {
                Pattern pattern = Pattern.compile("(.*): (.*)");
                Matcher matcher = pattern.matcher(field);
                if (matcher.find()) {
                    String key = matcher.group(1);
                    String value = matcher.group(2);
                    addField(key, value);
                    // TODO: Check if an header field always terminate by a final line and not a semi-colon like "charset"
                } else {
                    throw new IllegalStateException();
                }
            } catch (IllegalStateException e) {
                throw new IllegalStateException("The field \"" + field + "\" is an invalid header field format", e);
            }
        }
    }

    public Map<String, String> getFields() {
        return new HashMap<>(fields);
    }

    public boolean hasField(String keyField) {
        return fields.containsKey(keyField);
    }

    public String getField(String keyField) {
        return fields.get(keyField);
    }

    public void addField(String key, String value) {
        fields.put(key, value);
    }

    @Override
    public String toString() {
        return "{"
                + "fields=" + this.fields
                + "}";
    }
}
