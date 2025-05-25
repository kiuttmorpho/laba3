package view;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

public class MonsterInfoDialog extends JDialog {
    private final Monster monster;

    public MonsterInfoDialog(JFrame parent, Monster monster) {
        super(parent, "Информация о монстре", true);
        this.monster = monster;
        setupUI();
    }

    private void setupUI() {
        setSize(650, 550);
        setLocationRelativeTo(getParent());
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Основная информация
        addLabelAndField(mainPanel, "ID:", String.valueOf(monster.getId()), false);
        addLabelAndField(mainPanel, "Имя:", monster.getName(), true);
        addLabelAndField(mainPanel, "Описание:", monster.getDescription(), true);
        addLabelAndField(mainPanel, "Функция:", monster.getFunction(), true);
        
        // Характеристики
        addLabelAndNumberField(mainPanel, "Уровень опасности:", monster.getDanger(), 0, 10);
        addLabelAndField(mainPanel, "Место обитания:", monster.getHabitat(), true);
        addLabelAndDateField(mainPanel, "Первое упоминание:", monster.getFirstMention());
        
        // Параметры
        addLabelAndField(mainPanel, "Иммунитеты:", String.join(", ", monster.getImmunities()), true);
        addLabelAndNumberField(mainPanel, "Рост:", monster.getHeight(), 0, 1000);
        addLabelAndField(mainPanel, "Вес:", monster.getWeight(), true);
        addLabelAndField(mainPanel, "Время активности:", monster.getActivityTime(), true);

        // Рецепт (если есть)
        if (monster.getRecipe() != null) {
            addLabelAndField(mainPanel, "Ингредиенты:", 
                String.join(", ", monster.getRecipe().getIngredients()), false);
            addLabelAndField(mainPanel, "Время приготовления:", 
                monster.getRecipe().getPreparationTime() + " минут", false);
            addLabelAndField(mainPanel, "Эффективность:", 
                monster.getRecipe().getEffectiveness(), false);
        }

        // Кнопка закрытия
        JButton closeButton = new JButton("Закрыть");
        closeButton.addActionListener(e -> dispose());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(buttonPanel);

        add(new JScrollPane(mainPanel));
    }

    private void addLabelAndField(JPanel panel, String label, String value, boolean editable) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(150, 25)); // Ширина метки
        row.add(lbl);
        
        JTextField field = new JTextField(value != null ? value : "", 25);
        field.setEditable(editable);
        if (editable) {
            field.addActionListener(e -> updateField(field, label));
        }
        row.add(field);
        
        panel.add(row);
        panel.add(Box.createVerticalStrut(5));
    }

    private void addLabelAndNumberField(JPanel panel, String label, int value, int min, int max) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(150, 25));
        row.add(lbl);
        
        JTextField field = new JTextField(String.valueOf(value), 5);
        field.addActionListener(e -> {
            try {
                int num = Integer.parseInt(field.getText());
                if (num >= min && num <= max) {
                    updateField(field, label);
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Введите число от " + min + " до " + max,
                    "Ошибка ввода", 
                    JOptionPane.ERROR_MESSAGE);
                field.setText(String.valueOf(value));
            }
        });
        row.add(field);
        
        panel.add(row);
        panel.add(Box.createVerticalStrut(5));
    }

    private void addLabelAndDateField(JPanel panel, String label, String dateValue) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(150, 25));
        row.add(lbl);
        
        JFormattedTextField dateField = new JFormattedTextField(
            new SimpleDateFormat("yyyy-MM-dd"));
        dateField.setColumns(10);
        
        try {
            if (dateValue != null && !dateValue.isEmpty()) {
                dateField.setValue(new SimpleDateFormat("yyyy-MM-dd").parse(dateValue));
            }
        } catch (Exception e) {
            dateField.setValue(null);
        }
        
        dateField.addPropertyChangeListener("value", evt -> {
            if (dateField.getValue() != null) {
                monster.setFirstMention(new SimpleDateFormat("yyyy-MM-dd")
                    .format(dateField.getValue()));
            }
        });
        
        row.add(dateField);
        panel.add(row);
        panel.add(Box.createVerticalStrut(5));
    }

    private void updateField(JTextField field, String fieldName) {
        String value = field.getText();
        switch (fieldName) {
            case "Имя:" -> monster.setName(value);
            case "Описание:" -> monster.setDescription(value);
            case "Функция:" -> monster.setFunction(value);
            case "Уровень опасности:" -> monster.setDanger(Integer.parseInt(value));
            case "Место обитания:" -> monster.setHabitat(value);
            case "Иммунитеты:" -> {
                monster.getImmunities().clear();
                for (String immunity : value.split(",")) {
                    String trimmed = immunity.trim();
                    if (!trimmed.isEmpty()) {
                        monster.getImmunities().add(trimmed);
                    }
                }
            }
            case "Рост:" -> monster.setHeight(Integer.parseInt(value));
            case "Вес:" -> monster.setWeight(value);
            case "Время активности:" -> monster.setActivityTime(value);
        }
    }
}