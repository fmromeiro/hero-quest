package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.items.Item;

import java.util.ArrayList;
import java.util.List;

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

}
