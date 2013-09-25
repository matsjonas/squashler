package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static ArrayNode newArrayNode() {
        return mapper.createArrayNode();
    }

    public static ObjectNode newObjectNode() {
        return mapper.createObjectNode();
    }

}
