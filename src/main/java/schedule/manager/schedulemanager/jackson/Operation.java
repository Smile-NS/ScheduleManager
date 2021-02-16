package schedule.manager.schedulemanager.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface Operation {

    void update() throws IOException;
    void save() throws IOException;
    void put(String key, Object obj);
    void remove(String key);
    void clear();

    Map<String, Object> getJsonAsMap();
    void setJsonWithMap(Map<String, Object> map);
    JsonNode getJsonAsNode();

    JsonNode getNode(String fieldPath);
    String getString(String fieldPath);
    boolean getBoolean(String fieldPath);
    double getDouble(String fieldPath);
    int getInt(String fieldPath);
    long getLong(String fieldPath);
    Map<String, Object> getMap(String fieldPath);
    List<Object> getList(String fieldPath);

    ObjectMapper getMapper();
    boolean containsKey(String key);
    boolean containsValue(String key);
    boolean exists();
    boolean isEmpty();
    int size();
}
