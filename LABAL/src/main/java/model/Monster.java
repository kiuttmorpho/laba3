
package model;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnySetter;

public class Monster {
    private String name;
    private String source; // Источник получения информации
    private Map<String, Object> attributes = new HashMap<>();
    
      // Конструкторы, геттеры и сеттеры
    
    @JsonAnySetter
    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public Monster(String name, String source) {
        this.name = name;
        this.source = source;
        this.attributes = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public String getSource() {
        return source;
    }

    public void setAttribute(String key, String value) {
        attributes.put(key, value);
    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }

    public Map<String, String> getAttributes() {
        return new HashMap<>(attributes);
    }
}
