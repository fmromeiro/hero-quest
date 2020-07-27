package br.ic.unicamp.mc322.heroquest.controller;

import br.ic.unicamp.mc322.heroquest.entities.Dungeon;
import br.ic.unicamp.mc322.heroquest.entities.Tile;
import br.ic.unicamp.mc322.heroquest.items.Equipment;
import br.ic.unicamp.mc322.heroquest.items.Item;
import br.ic.unicamp.mc322.heroquest.items.Weapon;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TerminalRenderer implements Renderer {
    public void printWholeMap() {
        StringBuilder mapString = new StringBuilder("   ");
        Tile[][] tiles = Dungeon.getInstance().getMap();
        Set<Integer> rooms = Dungeon.getInstance().getRoomIds();
        for (int i = 0; i < tiles[0].length; i++)
            mapString.append(String.format("%02d", i));
        int j = 0;
        for (Tile[] row : tiles) {
            mapString.append(String.format("\n%02d ", j++));
            for (Tile tile : row) {
                Set<Integer> intersection = new HashSet<>(tile.getRooms());
                intersection.retainAll(rooms);
                if (intersection.isEmpty())
                    mapString.append(tile.getStringRepresentation());
                else {
                    int room = intersection.iterator().next();
                    rooms.remove(room);
                    mapString.append(String.format("%02d", room));
                }
            }
        }
        System.out.println(mapString);
    }

    public void printVisibleMap() {
        StringBuilder mapString = new StringBuilder("   ");
        Tile[][] tiles = Dungeon.getInstance().getMap();
        boolean[][] visibilityMatrix = Dungeon.getInstance().getHeroVisibility();
        for (int y = 0; y < tiles.length; y++) {
            mapString.append("\n");
            for (int x = 0; x < tiles[y].length; x++)
                mapString.append(visibilityMatrix[y][x]
                        ? tiles[y][x].getStringRepresentation()
                        : "▒▒");
        }

        System.out.println(mapString);
    }

    public void announcePlayerTurn() {
        System.out.println("Player's turn");
    }

    public void announceMoveTurn() {
        System.out.println("Move action");
    }

    @Override
    public void alertMoveActionUsed() {
        System.out.println("Move action has already been used in this turn");
    }

    @Override
    public void alertMainActionUsed() {
        System.out.println("Main action has already been used in this turn");
    }

    @Override
    public void announceAttackTurn() {
        System.out.println("Starting main action turn!");
    }

    @Override
    public void printCurrentWeapon(Equipment currentWeapon) {
        if (currentWeapon instanceof Weapon) {
            Weapon weapon = (Weapon)currentWeapon;
            System.out.println("Current weapon is " + weapon.getName() +
                    ", adding " + weapon.getModifier() + " combat dice" +
                    (weapon.getRange() == Integer.MAX_VALUE ? " as far as the eye can see" : (" up to a distance of " + weapon.getRange())));
        }
        else
            System.out.println("You are attacking with your bare hands");
    }

    @Override
    public void alertCouldNotInterpretCommand() {
        System.out.println("Couldn't interpret command, please try again");
    }

    @Override
    public void announceAttackTurnEnd() {
        System.out.println("Attack turn finished!");
    }

    @Override
    public void printCurrentEquipment(Map<String, Equipment> equipment) {
        equipment.entrySet().stream()
                .sorted((a, b) -> a.getKey().compareTo(b.getKey()))
                .forEach(entry -> System.out.println(entry.getKey() + ": " + (entry.getValue() != null ? entry.getValue().getName() : "Nothing")));
    }

    @Override
    public void printInventory(Map<Integer, Item> inventory) {
        inventory.entrySet().stream()
                .sorted((a, b) -> a.getKey().compareTo(b.getKey()))
                .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue().getName()));

    }

    @Override
    public void alertMissingParameter(String command) {
        System.out.println("Missing parameters for command " + command);
    }

    @Override
    public void printAvailableActions(String... actions) {
        System.out.println("Available actions are:");
        for (String action : actions) {
            System.out.println("\t" + action);
        }
    }

    @Override
    public void alertInvalidItem(int id) {
        System.out.println("Item with id " + id + " was not found or isn't valid equipment");
    }

    @Override
    public void printAvailableSteps(int steps) {
        System.out.println("You have " + steps + " remaining steps");
    }

    @Override
    public void printHeroVictory() {
        System.out.println("All enemies were defeated! The hero wins!");
    }

    @Override
    public void printHeroDefeat() {
        System.out.println("The hero was defeated... Better luck next time.");

    }

    @Override
    public void printWholeMapv2(Dungeon dungeon) {
        StringBuilder mapString = new StringBuilder("   ");
        Tile[][] tiles = dungeon.getMap();
        for (int i = 0; i < tiles[0].length; i++)
            mapString.append(String.format("%02d", i));
        int j = 0;
        for (Tile[] row : tiles) {
            mapString.append(String.format("\n%02d ", j++));
            for (Tile tile : row) {
                mapString.append(tile.getStringRepresentation());
            }
        }
        System.out.println(mapString);
    }

}
