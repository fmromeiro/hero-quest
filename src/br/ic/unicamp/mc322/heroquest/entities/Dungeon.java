package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;

import java.util.*;
import java.util.stream.Collectors;

public class Dungeon {
    public final static int WIDTH = 36, HEIGHT = 27;
    private final Tile[][] map;
    private final Set<Integer> visitedRooms;
    private int nextRoomId;

    private static Dungeon instance = null;

    public static Dungeon getInstance() {
        if (instance == null)
            instance = new Dungeon();
        return instance;
    }

    private Dungeon() {
        this.map = new Tile[HEIGHT][WIDTH];
        for (int i = 0; i < HEIGHT*WIDTH; i++)
            this.map[i/WIDTH][i%WIDTH] = new Tile(i%WIDTH, i/WIDTH);
        this.visitedRooms = new HashSet<>();
        this.nextRoomId = 0;
    }

    public void addRoom(Point topLeft, Point lowerRight) {
        for (int y = topLeft.getY(); y <= lowerRight.getY(); y++)
            for (int x = topLeft.getX(); x <= lowerRight.getX(); x++)
                map[y][x].setToRoom(this.nextRoomId);

        this.setBordersToRoom(this.nextRoomId++);
    }

    public void setBordersToRoom(int roomId) {
        for (int y = 1; y < HEIGHT - 1; y++)
            for (int x = 1; x < WIDTH - 1; x++)
                if (map[y][x].isInRoom(roomId))
                    if (!allAdjacentBelongToSameRoom(y, x, roomId))
                        map[y][x].setEntity(new Wall(new Point(x, y)));
                    else
                        map[y][x].setEntity(null);

        for (int y = 0; y < HEIGHT; y++) {
            if (map[y][0].isInRoom(roomId))
                map[y][0].setEntity(new Wall(new Point(0, y)));
            if (map[y][WIDTH - 1].isInRoom(roomId))
                map[y][WIDTH - 1].setEntity(new Wall(new Point(WIDTH - 1, y)));
        }

        for (int x = 0; x < WIDTH; x++) {
            if (map[0][x].isInRoom(roomId))
                map[0][x].setEntity(new Wall(new Point(x, 0)));
            if (map[HEIGHT - 1][x].isInRoom(roomId))
                map[HEIGHT - 1][x].setEntity(new Wall(new Point(x, HEIGHT - 1)));
        }
    }

    private boolean allAdjacentBelongToSameRoom(int y, int x, int roomId) {
        for (int dy = -1; dy <= 1; dy++)
            for (int dx = -1; dx <= 1; dx++)
                if (!map[y + dy][x + dx].isInRoom(roomId))
                    return false;
        return true;
    }

    public void addToRoom(int roomId, Point topLeft, Point lowerRight) {
        if (roomId >= this.nextRoomId)
            throw new NoSuchElementException("There's no room with Id " + roomId);

        for (int y = topLeft.getY(); y <= lowerRight.getY(); y++)
            for (int x = topLeft.getX(); x <= lowerRight.getX(); x++)
                map[y][x].setToRoom(roomId);

        this.setBordersToRoom(roomId);
    }

    public void removeFromRoom(int roomId, Point topLeft, Point lowerRight) {
        for (int y = topLeft.getY(); y <= lowerRight.getY(); y++)
            for (int x = topLeft.getX(); x <= lowerRight.getX(); x++)
                map[y][x].removeFromRoom(roomId);

        this.setBordersToRoom(roomId);
    }

    public Tile[][] getMap() {
         return this.map;
    }

    public Set<Integer> getRoomIds() {
        Set<Integer> result = new HashSet<>();
        for (int i = 0; i < this.nextRoomId; i++)
            result.add(i);
        return result;
    }

    public void addEntity(Entity entity, Point position) {
        this.map[position.getY()][position.getX()].setEntity(entity);

        if (entity instanceof Hero) {
            this.visitedRooms.addAll(this.map[position.getY()][position.getX()].getRooms());
        }
    }

    public boolean[][] getVisibilityFrom(Point position) {
        boolean[][] visibilityMatrix = new boolean[HEIGHT][WIDTH];
        LinkedList<Point[]> points = new LinkedList<>();
        for (int i = 0; i < WIDTH; i++) {
            points.add(Point.bresenhamLine(position, new Point(i, 0)));
            points.add(Point.bresenhamLine(position, new Point(i, HEIGHT - 1)));
        }
        for (int i = 0; i < HEIGHT; i++) {
            points.add(Point.bresenhamLine(position, new Point(0, i)));
            points.add(Point.bresenhamLine(position, new Point(WIDTH - 1, i)));
        }
        for (Point[] line : points) {
            boolean visible = true;
            for (Point point : line) {
                visibilityMatrix[point.getY()][point.getX()] |= visible;
                Entity current = this.map[point.getY()][point.getX()].getEntity();
                if (current instanceof Wall && ((Wall)current).isSeen())
                    visibilityMatrix[point.getY()][point.getX()] = true;
                if (visible && current != null && !current.canSeeThrough())
                    visible = false;
            }
        }

        handleVisibilityMatrixEdgeCases(visibilityMatrix, position);

        return visibilityMatrix;
    }

    private void handleVisibilityMatrixEdgeCases(boolean[][] visibilityMatrix, Point fovCenter) {
        int midHeight = fovCenter.getY();
        int midWidth = fovCenter.getX();

        // 1° quadrante
        for (int y = midHeight; y < HEIGHT; y++)
            for (int x = 0; x <= midWidth; x++)
                if ((!visibilityMatrix[y][x])
                        && (visibilityMatrix[y - 1][x]
                        && this.map[y - 1][x].getEntity() == null
                        || visibilityMatrix[y][x + 1]
                        && this.map[y][x + 1].getEntity() == null)
                        && (this.map[y][x].getEntity() instanceof Wall
                        || this.map[y][x].getEntity() instanceof Door))
                    visibilityMatrix[y][x] = true;

        // 2° quadrante
        for (int y = midHeight; y < HEIGHT; y++)
            for (int x = midWidth; x < WIDTH; x++)
                if (!visibilityMatrix[y][x])
                    if (visibilityMatrix[y - 1][x]
                            && this.map[y - 1][x].getEntity() == null
                            || visibilityMatrix[y][x - 1]
                            && this.map[y][x - 1].getEntity() == null)
                        if (this.map[y][x].getEntity() instanceof Wall
                                || this.map[y][x].getEntity() instanceof Door)
                            visibilityMatrix[y][x] = true;

        // 3º quadrante
        for (int y = 0; y <= midHeight; y++)
            for (int x = midWidth; x < WIDTH; x++)
                if ((!visibilityMatrix[y][x])
                        && (visibilityMatrix[y + 1][x]
                        && this.map[y + 1][x].getEntity() == null
                        || visibilityMatrix[y][x - 1]
                        && this.map[y][x - 1].getEntity() == null)
                        && (this.map[y][x].getEntity() instanceof Wall
                        || this.map[y][x].getEntity() instanceof Door))
                    visibilityMatrix[y][x] = true;

        // 4º quadrante
        for (int y = 0; y <= midHeight; y++)
            for (int x = 0; x <= midWidth; x++)
                if ((!visibilityMatrix[y][x])
                        && (visibilityMatrix[y + 1][x]
                        && this.map[y + 1][x].getEntity() == null
                        || visibilityMatrix[y][x + 1]
                        && this.map[y][x + 1].getEntity() == null)
                        && (this.map[y][x].getEntity() instanceof Wall
                        || this.map[y][x].getEntity() instanceof Door))
                    visibilityMatrix[y][x] = true;

    }

    public boolean[][] getHeroVisibility() {
        boolean[][] visibilityMatrix = new boolean[HEIGHT][WIDTH];
        Hero hero = getHero();
        if (hero == null)
            return visibilityMatrix;
        visibilityMatrix = getVisibilityFrom(hero.getPosition());
        for (int y = 0; y < HEIGHT; y++)
            for (int x = 0; x < WIDTH; x++) {
                HashSet<Integer> intersection = new HashSet<>(map[y][x].getRooms());
                intersection.retainAll(this.visitedRooms);
                visibilityMatrix[y][x] &= !intersection.isEmpty();
                if (visibilityMatrix[y][x] && this.map[y][x].getEntity() instanceof Wall)
                    ((Wall) this.map[y][x].getEntity()).setAsSeen();
            }
        return visibilityMatrix;
    }

    public Hero getHero() {
        Tile possibleHero = Arrays.stream(this.map)
                .flatMap(Arrays::stream)
                .filter(tile -> tile.getEntity() instanceof Hero)
                .findAny().orElse(null);
        if (possibleHero != null)
            return (Hero)possibleHero.getEntity();
        return null;
    }

    public void moveEntity(Entity entity, Point whereTo) {
        Tile possibleEntity = Arrays.stream(this.map)
                .flatMap(Arrays::stream)
                .filter(tile -> tile.getEntity() == entity)
                .findAny().orElse(null);
        if (possibleEntity == null)
            return;

        possibleEntity.removeEntity();

        if (map[whereTo.getY()][whereTo.getX()].canSetEntity())
            map[whereTo.getY()][whereTo.getX()].setEntity(entity);

        if (entity instanceof Hero) {
            this.visitedRooms.addAll(this.map[whereTo.getY()][whereTo.getX()].getRooms());
        }
    }

    public List<Entity> getEntities() {
        return Arrays.stream(this.map)
                .flatMap(Arrays::stream)
                .map(Tile::getEntity)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableList());
    }
}