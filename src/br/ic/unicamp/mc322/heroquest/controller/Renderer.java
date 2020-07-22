package br.ic.unicamp.mc322.heroquest.controller;

import br.ic.unicamp.mc322.heroquest.entities.Dungeon;
import br.ic.unicamp.mc322.heroquest.entities.Tile;

import java.util.HashSet;
import java.util.Set;

public class Renderer {
    public static void printWholeMap(Dungeon dungeon) {
        StringBuilder mapString = new StringBuilder("   ");
        Tile[][] tiles = dungeon.getMap();
        Set<Integer> rooms = dungeon.getRoomIds();
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

    public static void printVisibleMap(Dungeon dungeon) {
        StringBuilder mapString = new StringBuilder("   ");
        Tile[][] tiles = dungeon.getMap();
        boolean[][] visibilityMatrix = dungeon.getHeroVisibility();
        for (int y = 0; y < tiles.length; y++) {
            mapString.append("\n");
            for (int x = 0; x < tiles[y].length; x++)
                mapString.append(visibilityMatrix[y][x]
                        ? tiles[y][x].getStringRepresentation()
                        : "▒▒");
        }

        System.out.println(mapString);
    }
}
