package fileprocessor;

import model.Monster;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.util.List;
import model.Bestiarum;

public class XmlFileExporter implements FileExporter {
    private FileExporter nextProcessor;

    @Override
    public void setNextProcessor(FileExporter nextProcessor) {
        this.nextProcessor = nextProcessor;
    }

    @Override
    public boolean exportFile(String filePath, List<Monster> monsters) {
        if (filePath.toLowerCase().endsWith(".xml")) {
            try {
                Bestiarum bestiarum = new Bestiarum();
                bestiarum.setMonsters(monsters);

                JAXBContext context = JAXBContext.newInstance(Bestiarum.class);
                Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                marshaller.marshal(bestiarum, new File(filePath));
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