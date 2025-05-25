/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fileprocessor;

import java.util.List;
import model.Monster;

public interface FileImporter {
    void setNextProcessor(FileImporter nextProcessor);
    List<Monster> importFile(String filePath);
}
