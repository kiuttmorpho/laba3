/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import model.Monster;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class MonsterDetailsFrame extends JFrame {
    private JTextArea detailsArea;
    private Monster currentMonster;

    public MonsterDetailsFrame() {
        setTitle("Monster Details");
        setSize(400, 300);
        UIUtils.centerWindow(this);

        detailsArea = new JTextArea();
        detailsArea.setEditable(true);
        detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(new JScrollPane(detailsArea), BorderLayout.CENTER);

        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(e -> saveChanges());
        add(saveButton, BorderLayout.SOUTH);
    }

    public void showMonster(Monster monster) {
        this.currentMonster = monster;
        updateDetails();
        setVisible(true);
    }

    private void updateDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(currentMonster.getName()).append("\n");
        sb.append("Source: ").append(currentMonster.getSource()).append("\n\n");
        sb.append("Attributes:\n");

        for (Map.Entry<String, String> entry : currentMonster.getAttributes().entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        detailsArea.setText(sb.toString());
    }

    private void saveChanges() {
        String newText = detailsArea.getText();
        // Здесь можно добавить логику разбора текста и обновления атрибутов
        JOptionPane.showMessageDialog(this, "Changes saved!");
    }
}
