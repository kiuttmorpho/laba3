package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Monster;
import model.MonsterStorage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonImporter extends Importer {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public List<MonsterStorage> importData(String filePath) {
        if (filePath.endsWith(".json")) {
            try {
                List<Map<String, Object>> monstersData = mapper.readValue(
                    new File(filePath),
                    mapper.getTypeFactory().constructCollectionType(List.class, Map.class)
                );

                List<MonsterStorage> storages = new ArrayList<>();
                MonsterStorage storage = new MonsterStorage("JSON");
                
                for (Map<String, Object> monsterData : monstersData) {
                    Monster monster = new Monster(
                        (String) monsterData.get("name"),
                        "JSON"
                    );
                    
                    monsterData.forEach((key, value) -> {
                        if (!key.equals("name")) {
                            monster.setAttribute(key, value);
                        }
                    });
                    
                    storage.addMonster(monster);
                }
                
                storages.add(storage);
                return storages;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return passToNext(filePath);
    }
}