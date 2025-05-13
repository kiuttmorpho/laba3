/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.MonsterStorage;

import java.util.List;

public class ChainBuilder {
    public static List<MonsterStorage> buildChainAndProcess(String filePath) {
        // Создаем обработчики в цепочке ответственности
        Importer jsonImporter = new JsonImporter();
        Importer xmlImporter = new XmlImporter();
        Importer yamlImporter = new YamlImporter();

        jsonImporter.setNext(xmlImporter);
        xmlImporter.setNext(yamlImporter);

        // Начинаем обработку с первого обработчика
        return jsonImporter.importData(filePath);
    }
}