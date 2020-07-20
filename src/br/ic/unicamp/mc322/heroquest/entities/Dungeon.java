package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;

import java.util.*;

public class Dungeon {
    private final int width, height;
    private final Tile[][] map;
    private final Map<Integer, Boolean> visitedRooms;
    private int nextRoomId;

    public Dungeon(int width, int height) {
        this.width = width;
        this.height = height;
        this.map = new Tile[height][width];
        for (int i = 0; i < height*width; i++)
            this.map[i/width][i%width] = new Tile(i%width, i/width);
        this.visitedRooms = new HashMap<>();
        this.nextRoomId = 0;
    }

    public void addRoom(Point topLeft, Point lowerRight) {
        for (int y = topLeft.getY(); y < lowerRight.getY(); y++)
            for (int x = topLeft.getX(); x < lowerRight.getX(); x++)
                map[y][x].setToRoom(this.nextRoomId);

        this.setBordersToRoom(this.nextRoomId++);
    }

    public void setBordersToRoom(int roomId) {
        for (int y = 1; y < height - 1; y++)
            for (int x = 1; x < width - 1; x++)
                if (!allAdjacentBelongToSameRoom(y, x, roomId))
                    map[y][x].setEntity(new Wall(new Point(x, y)));
                else
                    map[y][x].setEntity(null);

    }

    private boolean allAdjacentBelongToSameRoom(int y, int x, int roomId) {
        return map[y - 1][x].isInRoom(roomId)
                && map[y + 1][x].isInRoom(roomId)
                && map[y][x - 1].isInRoom(roomId)
                && map[y][x + 1].isInRoom(roomId);
    }

    public void setDoor(int x, int y) {
        this.map[y][x].setEntity(new Door(new Point(x, y)));
    }

    public void addToRoom(int roomId, Point topLeft, Point lowerRight) {
        if (roomId >= this.nextRoomId)
            throw new NoSuchElementException("There's no room with Id " + roomId);

        for (int y = topLeft.getY(); y < lowerRight.getY(); y++)
            for (int x = topLeft.getX(); x < lowerRight.getX(); x++)
                map[y][x].setToRoom(roomId);

        this.setBordersToRoom(roomId);
    }
}

class Tile {
    private Entity currentEntity;
    private final Set<Integer> rooms;
    private final Point position;

    public Tile(int x, int y) {
        this.currentEntity = null;
        this.rooms = new HashSet<>();
        this.position = new Point(x, y);
    }

    public void setToRoom(int roomIndex) {
        this.rooms.add(roomIndex);
    }

    public Entity getEntity() { return this.currentEntity; }

    public Entity removeEntity() {
        Entity current = this.currentEntity;
        this.currentEntity = null;
        return current;
    }

    public void setEntity(Entity entity) {
        this.currentEntity = entity;
        if (entity != null)
            entity.moveTo(this.position);
    }

    protected boolean isInRoom(int roomId) {
        return this.rooms.contains(roomId);
    }
}