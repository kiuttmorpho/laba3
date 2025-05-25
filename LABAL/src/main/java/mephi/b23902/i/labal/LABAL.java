package mephi.b23902.i.labal;

import javax.swing.SwingUtilities;
import view.MonsterDatabaseWindow;

public class LABAL {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MonsterDatabaseWindow window = new MonsterDatabaseWindow();
            window.setVisible(true);
        });
    }
}