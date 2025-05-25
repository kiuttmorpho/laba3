package fileprocessor;

import model.Monster;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class YamlFileExporter implements FileExporter {
    private FileExporter nextProcessor;
    private final Yaml yaml;

    public YamlFileExporter() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        options.setIndent(2);
        this.yaml = new Yaml(options);
    }

    @Override
    public void setNextProcessor(FileExporter nextProcessor) {
        this.nextProcessor = nextProcessor;
    }

    @Override
    public boolean exportFile(String filePath, List<Monster> monsters) {
        if (filePath.toLowerCase().endsWith(".yml") || filePath.toLowerCase().endsWith(".yaml")) {
            try (FileWriter writer = new FileWriter(filePath)) {
                // Create the YAML structure manually
                Map<String, Object> bestiarumMap = new LinkedHashMap<>();
                List<Map<String, Object>> monsterList = new ArrayList<>();

                for (Monster monster : monsters) {
                    Map<String, Object> monsterMap = new LinkedHashMap<>();
                    monsterMap.put("id", monster.getId());
                    monsterMap.put("name", monster.getName());
                    monsterMap.put("description", monster.getDescription());
                    monsterMap.put("function", monster.getFunction());
                    monsterMap.put("danger", monster.getDanger());
                    monsterMap.put("habitat", monster.getHabitat());
                    monsterMap.put("first_mention", monster.getFirstMention());
                    monsterMap.put("immunities", monster.getImmunities());
                    monsterMap.put("height", monster.getHeight());
                    monsterMap.put("weight", monster.getWeight());
                    monsterMap.put("activity_time", monster.getActivityTime());
                    monsterMap.put("source", monster.getSource());
                    
                    if (monster.getRecipe() != null) {
                        Map<String, Object> recipeMap = new LinkedHashMap<>();
                        recipeMap.put("ingredients", monster.getRecipe().getIngredients());
                        recipeMap.put("preparation_time", monster.getRecipe().getPreparationTime());
                        recipeMap.put("effectiveness", monster.getRecipe().getEffectiveness());
                        monsterMap.put("recipe", recipeMap);
                    }
                    
                    monsterList.add(monsterMap);
                }
                
                bestiarumMap.put("monster", monsterList);
                Map<String, Object> root = new LinkedHashMap<>();
                root.put("bestiarum", bestiarumMap);

                yaml.dump(root, writer);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else if (nextProcessor != null) {
            return nextProcessor.exportFile(filePath, monsters);
        }
        return false;
    }
}