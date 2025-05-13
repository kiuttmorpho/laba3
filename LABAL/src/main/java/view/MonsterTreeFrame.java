/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import model.Monster;
import model.MonsterStorage;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.List;

public class MonsterTreeFrame extends JFrame {
    private JTree monsterTree;
    private MonsterDetailsFrame detailsFrame;

    public MonsterTreeFrame(List<MonsterStorage> storages) {
        setTitle("Monster Bestiary");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UIUtils.centerWindow(this);

        // Создаем корневой узел дерева
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Monsters");

        // Добавляем чудовищ из каждого хранилища
        for (MonsterStorage storage : storages) {
            DefaultMutableTreeNode storageNode = new DefaultMutableTreeNode(storage.getStorageType());
            
            for (Monster monster : storage.getMonsters()) {
                DefaultMutableTreeNode monsterNode = new DefaultMutableTreeNode(monster.getName());
                storageNode.add(monsterNode);
            }
            
            root.add(storageNode);
        }

        // Создаем модель дерева
        monsterTree = new JTree(new DefaultTreeModel(root));
        monsterTree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) monsterTree.getLastSelectedPathComponent();
            
            if (node != null && node.isLeaf() && !node.isRoot()) {
                // Находим выбранное чудовище
                Monster selectedMonster = findMonster(node.toString(), storages);
                if (selectedMonster != null) {
                    if (detailsFrame == null) {
                        detailsFrame = new MonsterDetailsFrame();
                    }
                    detailsFrame.showMonster(selectedMonster);
                }
            }
        });

        add(new JScrollPane(monsterTree));
    }

    private Monster findMonster(String name, List<MonsterStorage> storages) {
        for (MonsterStorage storage : storages) {
            for (Monster monster : storage.getMonsters()) {
                if (monster.getName().equals(name)) {
                    return monster;
                }
            }
        }
        return null;
    }
}
