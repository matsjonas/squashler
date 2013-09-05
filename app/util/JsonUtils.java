package util;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

public class JsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static ArrayNode newArrayNode() {
        return mapper.createArrayNode();
    }

    public static ObjectNode newObjectNode() {
        return mapper.createObjectNode();
    }

}
