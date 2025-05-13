package controller;

import javax.xml.stream.XMLEventReader;
import model.Monster;
import model.MonsterStorage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XmlImporter extends Importer {
    @Override
    public List<MonsterStorage> importData(String filePath) {
        if (filePath.endsWith(".xml")) {
            try {
                JAXBContext context = JAXBContext.newInstance(XmlMonsterList.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                XmlMonsterList xmlMonsters = (XmlMonsterList) unmarshaller.unmarshal(new File(filePath));

                List<MonsterStorage> storages = new ArrayList<>();
                MonsterStorage storage = new MonsterStorage("XML");
                
                for (XmlMonster xmlMonster : xmlMonsters.getMonsters()) {
                    Monster monster = new Monster(xmlMonster.getName(), "XML");
                    
                    if (xmlMonster.getDanger() != null) {
                        monster.setAttribute("danger", xmlMonster.getDanger());
                    }
                    if (xmlMonster.getHabitat() != null) {
                        monster.setAttribute("habitat", xmlMonster.getHabitat());
                    }
                    // Добавьте другие атрибуты по аналогии
                    
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

// Вспомогательные классы для JAXB
class XmlMonsterList {
    private List<XmlMonster> monsters;

    @jakarta.xml.bind.annotation.XmlElement(name = "monster")
    public List<XmlMonster> getMonsters() {
        return monsters;
    }

    public void setMonsters(List<XmlMonster> monsters) {
        this.monsters = monsters;
    }
}

class XmlMonster {
    private String name;
    private Integer danger;
    private String habitat;

    // Геттеры и сеттеры с аннотациями @XmlElement
}