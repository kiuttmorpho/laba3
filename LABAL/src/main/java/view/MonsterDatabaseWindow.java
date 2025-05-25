package view;

import fileprocessor.*;
import model.Monster;
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MonsterDatabaseWindow extends JFrame {
    private final Map<String, List<Monster>> monsterCollections = new HashMap<>();
    private final JTree monsterTree;
    private final FileImporter fileImporter;
    private final JToolBar toolBar;

    public MonsterDatabaseWindow() {
        setTitle("Monster Database");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);

        fileImporter = new JsonFileImporter();
        FileImporter xmlImporter = new XmlFileImporter();
        FileImporter yamlImporter = new YamlFileImporter();
        fileImporter.setNextProcessor(xmlImporter);
        xmlImporter.setNextProcessor(yamlImporter);

        toolBar = new JToolBar();
        
        JButton importButton = new JButton("Import");
        importButton.addActionListener(e -> importFiles());
        toolBar.add(importButton);
        
        JButton exportButton = new JButton("Export");
        exportButton.addActionListener(e -> exportMonsters());
        toolBar.add(exportButton);
        
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Monsters");
        monsterTree = new JTree(root);
        monsterTree.addTreeSelectionListener(e -> showMonsterInfo());

        setLayout(new BorderLayout());
        add(toolBar, BorderLayout.NORTH);
        add(new JScrollPane(monsterTree), BorderLayout.CENTER);
    }

    private void importFiles() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setMultiSelectionEnabled(true);
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File[] files = fileChooser.getSelectedFiles();
            for (File file : files) {
                List<Monster> monsters = fileImporter.importFile(file.getPath());
                if (!monsters.isEmpty()) {
                    monsterCollections.put(file.getName(), monsters);
                }
            }
            updateTree();
        }
    }

    private void updateTree() {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) monsterTree.getModel().getRoot();
        root.removeAllChildren();
        
        for (Map.Entry<String, List<Monster>> entry : monsterCollections.entrySet()) {
            DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(entry.getKey());
            for (Monster monster : entry.getValue()) {
                fileNode.add(new DefaultMutableTreeNode(monster));
            }
            root.add(fileNode);
        }
        
        ((DefaultTreeModel) monsterTree.getModel()).reload();
    }

    private void showMonsterInfo() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) monsterTree.getLastSelectedPathComponent();
        if (node != null && node.getUserObject() instanceof Monster) {
            Monster monster = (Monster) node.getUserObject();
            new MonsterInfoDialog(this, monster).setVisible(true);
        }
    }

    private void exportMonsters() {
        if (monsterCollections.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No monsters to export!", 
                "Export Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        
        FileNameExtensionFilter jsonFilter = new FileNameExtensionFilter("JSON files (*.json)", "json");
        FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("XML files (*.xml)", "xml");
        FileNameExtensionFilter yamlFilter = new FileNameExtensionFilter("YAML files (*.yml, *.yaml)", "yml", "yaml");
        
        fileChooser.addChoosableFileFilter(jsonFilter);
        fileChooser.addChoosableFileFilter(xmlFilter);
        fileChooser.addChoosableFileFilter(yamlFilter);
        fileChooser.setFileFilter(jsonFilter);
        fileChooser.setAcceptAllFileFilterUsed(false);
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String path = file.getPath();
            
            String extension = "";
            if (fileChooser.getFileFilter() instanceof FileNameExtensionFilter) {
                FileNameExtensionFilter filter = (FileNameExtensionFilter) fileChooser.getFileFilter();
                extension = filter.getExtensions()[0];
                
                if (!path.toLowerCase().endsWith("." + extension)) {
                    path += "." + extension;
                }
            }
            
            FileExporter jsonExporter = new JsonFileExporter();
            FileExporter xmlExporter = new XmlFileExporter();
            FileExporter yamlExporter = new YamlFileExporter();
            jsonExporter.setNextProcessor(xmlExporter);
            xmlExporter.setNextProcessor(yamlExporter);
            
            List<Monster> allMonsters = new ArrayList<>();
            for (List<Monster> monsters : monsterCollections.values()) {
                allMonsters.addAll(monsters);
            }
            
            try {
                boolean success = jsonExporter.exportFile(path, allMonsters);
                
                if (success) {
                    JOptionPane.showMessageDialog(this, 
                        "Export successful!\nFile: " + path, 
                        "Export", 
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    throw new Exception("Export failed");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Export failed!\nError: " + e.getMessage(), 
                    "Export Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}