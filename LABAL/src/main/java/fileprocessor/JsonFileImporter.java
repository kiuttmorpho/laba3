
package fileprocessor;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import model.Bestiarum;
import model.Monster;

public class JsonFileImporter implements FileImporter {
    private FileImporter nextProcessor;
    private ObjectMapper mapper;
    
    public JsonFileImporter() {
        this.mapper = JsonMapper.builder().enable(DeserializationFeature.UNWRAP_ROOT_VALUE).build();
    }
    
    @Override
    public void setNextProcessor(FileImporter nextProcessor) {
        this.nextProcessor = nextProcessor;
    }

    @Override
    public List<Monster> importFile(String filePath) {
        if (filePath.toLowerCase().endsWith(".json")) {
            try {
                Bestiarum bestiarum = mapper.readValue(new File(filePath), Bestiarum.class);
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