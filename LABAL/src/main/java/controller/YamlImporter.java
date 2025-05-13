package controller;

import org.yaml.snakeyaml.Yaml;
import model.Monster;
import model.MonsterStorage;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class YamlImporter extends Importer {
    private static Yaml yaml = new Yaml();

    @Override
    public List<MonsterStorage> importData(String filePath) {
        if (filePath.endsWith(".yaml") || filePath.endsWith(".yml")) {
            try (FileInputStream input = new FileInputStream(filePath)) {
                List<Map<String, Object>> monstersData = yaml.load(input);

                List<MonsterStorage> storages = new ArrayList<>();
                MonsterStorage storage = new MonsterStorage("YAML");
                
                for (Map<String, Object> monsterData : monstersData) {
                    Monster monster = new Monster(
                        (String) monsterData.get("name"),
                        "YAML"
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