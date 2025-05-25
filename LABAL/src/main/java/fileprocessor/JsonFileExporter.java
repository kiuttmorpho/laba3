package fileprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import model.Monster;
import java.io.File;
import java.util.List;

public class JsonFileExporter implements FileExporter {
    private FileExporter nextProcessor;
    private final ObjectMapper mapper;

    public JsonFileExporter() {
        this.mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT); 
    }

    @Override
    public void setNextProcessor(FileExporter nextProcessor) {
        this.nextProcessor = nextProcessor;
    }

    @Override
    public boolean exportFile(String filePath, List<Monster> monsters) {
        if (filePath.toLowerCase().endsWith(".json")) {
            try {
                mapper.writeValue(new File(filePath), monsters);
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