package fileprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import model.Bestiarum;
import model.Monster;
import org.yaml.snakeyaml.Yaml;

public class YamlFileImporter implements FileImporter {
    private FileImporter nextProcessor;
    
    @Override
    public void setNextProcessor(FileImporter nextProcessor) {
        this.nextProcessor = nextProcessor;
    }

    @Override
    public List<Monster> importFile(String filePath) {
        if (filePath.toLowerCase().endsWith(".yml") || filePath.toLowerCase().endsWith(".yaml")) {
            try (FileInputStream inputStream = new FileInputStream(filePath)) {
                Yaml yaml = new Yaml();
                Map<String, Object> root = yaml.load(inputStream);
                Object bestiarumObj = root.get("bestiarum");
                if (bestiarumObj == null) {
                    return new ArrayList<>();
                }
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(bestiarumObj);
                Bestiarum bestiarum = mapper.readValue(json, Bestiarum.class);
                List<Monster> monsters = bestiarum.getMonsters();
                for (Monster m : monsters) {
                    m.setSource(filePath);
                }
                return monsters;
            } catch (Exception e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        } else if (nextProcessor != null) {
            return nextProcessor.importFile(filePath);
        }
        return new ArrayList<>();
    }
}