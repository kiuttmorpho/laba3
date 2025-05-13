/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.List;

public class MonsterStorage {
    private List<Monster> monsters;
    private String storageType; // JSON, XML или YAML

    public MonsterStorage(String storageType) {
        this.monsters = new ArrayList<>();
        this.storageType = storageType;
    }

    public void addMonster(Monster monster) {
        monsters.add(monster);
    }

    public List<Monster> getMonsters() {
        return new ArrayList<>(monsters);
    }

    public String getStorageType() {
        return storageType;
    }
}
