package controller;

import model.MonsterStorage;

import java.util.List;

public abstract class Importer {
    protected Importer next;

    public void setNext(Importer next) {
        this.next = next;
    }

    public abstract List<MonsterStorage> importData(String filePath);

    protected List<MonsterStorage> passToNext(String filePath) {
        if (next != null) {
            return next.importData(filePath);
        }
        return null;
    }
}