package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.items.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory {
    private final List<Item> itemInventory;

    public Inventory() {
        this.itemInventory = new ArrayList<>();
    }

    public void addItem(Item item) {
        itemInventory.add(item);
    }

    public Item getItem(int index) {
        Item item = itemInventory.get(index);
        itemInventory.remove(index);
        return item;
    }

    public Map<Integer, Item> getItems() {
        Map<Integer, Item> inventory = new HashMap<>();
        for (int i = 0; i < itemInventory.size(); i++)
            inventory.put(i, itemAt(i));
        return inventory;
    }

    public Item itemAt(int index) {
        return itemInventory.get(index).copy();
    }
}
