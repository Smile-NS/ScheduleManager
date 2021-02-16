package schedule.manager.schedulemanager.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class QuickJackson implements Operation{

    private final File FILE;
    private JsonNode node;
    private Map<String, Object> map;

    private final ObjectMapper MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    
    public QuickJackson(File file) throws IOException {
        FILE = file;
        if (!FILE.exists()) MAPPER.writeValue(FILE, MAPPER.createObjectNode());
        update();
    }

    public void update() throws IOException{
        node = MAPPER.readTree(FILE);
        String json = MAPPER.writeValueAsString(node);
        map = MAPPER.readValue(json, new TypeReference<Map<String, Object>>() {});
    }

    public void save() throws IOException{
        MAPPER.writeValue(FILE, map);
        update();
    }

    public void put(String key, Object obj) {
        map.put(key, obj);
    }

    public void remove(String key) {
        map.remove(key);
    }

    public void clear() {
        map.clear();
    }

    public Map<String, Object> getJsonAsMap(){
        return map;
    }

    public void setJsonWithMap(Map<String, Object> map){
        this.map = map;
    }

    public JsonNode getJsonAsNode(){
        return node;
    }

    public JsonNode getNode(String fieldPath){
        String[] path = fieldPath.split("\\.");
        JsonNode node = this.node.deepCopy();
        for (String key : path) node = node.get(key);

        return node;
    }

    public String getString(String fieldPath){
        return getNode(fieldPath).asText();
    }

    public boolean getBoolean(String fieldPath){
        return getNode(fieldPath).asBoolean();
    }

    public double getDouble(String fieldPath){
        return getNode(fieldPath).asDouble();
    }

    public int getInt(String fieldPath){
        return getNode(fieldPath).asInt();
    }

    public long getLong(String fieldPath){
        return getNode(fieldPath).asLong();
    }

    public Map<String, Object> getMap(String fieldPath) {
        JsonNode node = getNode(fieldPath);
        try {
            return MAPPER.readValue(MAPPER.writeValueAsString(node), new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            return null;
        }
    }

    public List<Object> getList(String fieldPath) {
        JsonNode node = getNode(fieldPath);
        try {
            return MAPPER.readValue(MAPPER.writeValueAsString(node), new TypeReference<List<Object>>() {});
        } catch (IOException e) {
            return null;
        }
    }

    public ObjectMapper getMapper(){
        return MAPPER;
    }

    public boolean containsKey(String key){
        return map.containsKey(key);
    }

    public boolean containsValue(String key){
        return map.containsValue(key);
    }

    public boolean exists(){
        return FILE.exists();
    }

    public boolean isEmpty(){
        return map.isEmpty();
    }

    public int size(){
        return map.size();
    }
}
