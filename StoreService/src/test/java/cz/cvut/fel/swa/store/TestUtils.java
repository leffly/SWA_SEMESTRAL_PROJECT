package cz.cvut.fel.swa.store;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

class TestUtils {

    private final static ObjectMapper mapper = new ObjectMapper();

    static String toJson(final Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
