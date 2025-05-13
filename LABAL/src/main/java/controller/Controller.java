/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.MonsterStorage;
import view.MonsterTreeFrame;

import javax.swing.*;
import java.io.File;
import java.util.List;

public class Controller {
    public static void main(String[] args) {
        // Устанавливаем директорию программы как директорию по умолчанию
        String currentDir = System.getProperty("user.dir");
        JFileChooser fileChooser = new JFileChooser(currentDir);

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();

            // Обрабатываем файл через цепочку ответственности
            List<MonsterStorage> storages = ChainBuilder.buildChainAndProcess(filePath);

            // Отображаем результат
            SwingUtilities.invokeLater(() -> {
                MonsterTreeFrame frame = new MonsterTreeFrame(storages);
                frame.setVisible(true);
            });
        }
    }
}
