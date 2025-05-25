package fileprocessor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import model.Bestiarum;
import model.Monster;

public class XmlFileImporter implements FileImporter {
    private FileImporter nextProcessor;
    
    @Override
    public void setNextProcessor(FileImporter nextProcessor) {
        this.nextProcessor = nextProcessor;
    }

    @Override
    public List<Monster> importFile(String filePath) {
        if (filePath.toLowerCase().endsWith(".xml")) {
            try {
                JAXBContext context = JAXBContext.newInstance(Bestiarum.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                Bestiarum bestiarum = (Bestiarum) unmarshaller.unmarshal(new File(filePath));
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