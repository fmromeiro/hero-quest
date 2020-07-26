package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;

import java.util.*;
import java.util.stream.Collectors;

public class Dungeon {
    public final static int WIDTH = 36, HEIGHT = 27;
    private final Tile[][] map;
    private final Set<Integer> visitedRooms;
    private final Set<Integer> openRooms;
    private final List<Tile> visitedTiles;
    private int nextRoomId;

    private static Dungeon instance = null;

    public final int MAX_ROOM = 50;

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
        this.openRooms = new HashSet<>();
        this.nextRoomId = 0;
        this.visitedTiles = new ArrayList<>();
    }


    // Dungeon building
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
                        map[y][x].setEntity(new Wall());
                    else
                        map[y][x].setEntity(null);

        for (int y = 0; y < HEIGHT; y++) {
            if (map[y][0].isInRoom(roomId))
                map[y][0].setEntity(new Wall());
            if (map[y][WIDTH - 1].isInRoom(roomId))
                map[y][WIDTH - 1].setEntity(new Wall());
        }

        for (int x = 0; x < WIDTH; x++) {
            if (map[0][x].isInRoom(roomId))
                map[0][x].setEntity(new Wall());
            if (map[HEIGHT - 1][x].isInRoom(roomId))
                map[HEIGHT - 1][x].setEntity(new Wall());
        }
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

    private boolean allAdjacentBelongToSameRoom(int y, int x, int roomId) {
        for (int dy = -1; dy <= 1; dy++)
            for (int dx = -1; dx <= 1; dx++)
                if (!map[y + dy][x + dx].isInRoom(roomId))
                    return false;
        return true;
    }


    // Accessors
    public void clearRooms() {
        for(Tile[] tiles : map)
            for(Tile tile : tiles)
                tile.removeFromEveryRoom();
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

    public Character getHero() {
        Tile possibleHero = Arrays.stream(this.map)
                .flatMap(Arrays::stream)
                .filter(tile -> tile.getEntity() instanceof Character)
                .filter(tile -> ((Character)tile.getEntity()).isHero())
                .findAny().orElse(null);
        if (possibleHero != null)
            return (Character) possibleHero.getEntity();
        return null;
    }

    public List<Entity> getEntities() {
        return Arrays.stream(this.map)
                .flatMap(Arrays::stream)
                .map(Tile::getEntity)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<Entity> getSecondaryEntities() {
        return Arrays.stream(this.map)
                .flatMap(Arrays::stream)
                .map(Tile::getSecondaryEntity)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public Entity entityAt(Point point) {
        return map[point.getY()][point.getX()].getEntity();
    }


    // Entity modifiers
    public void addEntity(Entity entity, Point position) {
        this.map[position.getY()][position.getX()].setEntity(entity);

        if (entity instanceof Character && ((Character)entity).isHero()) {
            this.visitedRooms.addAll(this.map[position.getY()][position.getX()].getRooms());
        }
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

        if (entity instanceof Character && ((Character) entity).isHero()) {
            this.visitedRooms.addAll(this.map[whereTo.getY()][whereTo.getX()].getRooms());
        }
    }


    // Visibility
    public void removeEntity(Point position) {
        this.map[position.getY()][position.getX()].removeEntity();
    }

    public void removeSecondaryEntity(Point position) {
        this.map[position.getY()][position.getX()].removeSecondaryEntity();
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
                if (current instanceof StaticEntity && ((StaticEntity)current).isSeen())
                    visibilityMatrix[point.getY()][point.getX()] = true;
                else if (current instanceof Door && ((Door)current).isSeen())
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
                        && (this.map[y][x].getEntity() instanceof StaticEntity))
                    visibilityMatrix[y][x] = true;

        // 2° quadrante
        for (int y = midHeight; y < HEIGHT; y++)
            for (int x = midWidth; x < WIDTH; x++)
                if ((!visibilityMatrix[y][x])
                        && (visibilityMatrix[y - 1][x]
                            && this.map[y - 1][x].getEntity() == null
                            || visibilityMatrix[y][x - 1]
                            && this.map[y][x - 1].getEntity() == null)
                        && this.map[y][x].getEntity() instanceof StaticEntity)
                    visibilityMatrix[y][x] = true;

        // 3º quadrante
        for (int y = 0; y <= midHeight; y++)
            for (int x = midWidth; x < WIDTH; x++)
                if ((!visibilityMatrix[y][x])
                        && (visibilityMatrix[y + 1][x]
                            && this.map[y + 1][x].getEntity() == null
                            || visibilityMatrix[y][x - 1]
                            && this.map[y][x - 1].getEntity() == null)
                        && (this.map[y][x].getEntity() instanceof StaticEntity))
                    visibilityMatrix[y][x] = true;

        // 4º quadrante
        for (int y = 0; y <= midHeight; y++)
            for (int x = 0; x <= midWidth; x++)
                if ((!visibilityMatrix[y][x])
                        && (visibilityMatrix[y + 1][x]
                            && this.map[y + 1][x].getEntity() == null
                            || visibilityMatrix[y][x + 1]
                            && this.map[y][x + 1].getEntity() == null)
                        && (this.map[y][x].getEntity() instanceof StaticEntity))
                    visibilityMatrix[y][x] = true;

    }

    public boolean[][] getHeroVisibility() {
        boolean[][] visibilityMatrix = new boolean[HEIGHT][WIDTH];
        Character hero = getHero();
        if (hero == null)
            return visibilityMatrix;
        visibilityMatrix = getVisibilityFrom(hero.getPosition());
        for (int y = 0; y < HEIGHT; y++)
            for (int x = 0; x < WIDTH; x++) {
                HashSet<Integer> intersection = new HashSet<>(map[y][x].getRooms());
                intersection.retainAll(this.visitedRooms);
                visibilityMatrix[y][x] &= !intersection.isEmpty();
                if (visibilityMatrix[y][x] && this.map[y][x].getEntity() instanceof StaticEntity)
                    ((StaticEntity) this.map[y][x].getEntity()).setAsSeen();
            }
        return visibilityMatrix;
    }

    public Point getRandomFreePoint() {
        ArrayList<Tile> tileList = new ArrayList<>();
        for (Tile[] row : map) {
            for (Tile tile : row) {
                if(tile.getEntity() == null)
                    tileList.add(tile);
            }
        }
        Random rng = new Random();
        return tileList.get(rng.nextInt(tileList.size())).getPosition();
    }



    public void addRandomRooms() {
        Random rng = new Random();for (Tile[] tiles : map)
            addRoom(new Point(0,0 ), new Point(WIDTH - 1, HEIGHT - 1));

        for(int i = 0; i < MAX_ROOM; i++) {
            int width = 2 * (rng.nextInt(3) + 1) + 2;  // gives 4, 6 or 8
            int height = 2 * (rng.nextInt(3) + 1) + 3; // gives 5, 7 or 9
            Point topLeft = new Point(rng.nextInt(WIDTH - 2 - width ) + 1, rng.nextInt(HEIGHT - 2 - height ) + 1);
            Point bottomRight = new Point(topLeft.getX() + width, topLeft.getY() + height);
            addRoom(topLeft, bottomRight);
        }
        reorganizeRooms();
        openPath(0);
        fillIsolatedRooms();
    }

    private void reorganizeRooms() {
        this.clearRooms();
        this.visitedRooms.clear();
        this.openRooms.clear();

        int i = 0;
        for(Tile[] tiles : map)
            for(Tile tile : tiles)
                if(tile.getEntity() == null && !visitedTiles.contains(tile)) {
                    List<Tile> visited = new ArrayList<>();
                    enumerateRoom(visited, tile, i);
                    visitedTiles.addAll(visited);
                    i++;
                }
    }

    private void enumerateRoom(List<Tile> visited, Tile current, int roomId) {
        current.setToRoom(roomId);
        visited.add(current);

        if (current.getEntity() instanceof Wall)
            return;
        if(current.getEntity() instanceof Door)
            return;

        int cX = current.getPosition().getX();
        int cY = current.getPosition().getY();

        if(!visited.contains(map[cY - 1][cX])) enumerateRoom(visited, map[cY - 1][cX], roomId);
        if(!visited.contains(map[cY][cX + 1])) enumerateRoom(visited, map[cY][cX + 1], roomId);
        if(!visited.contains(map[cY + 1][cX])) enumerateRoom(visited, map[cY + 1][cX], roomId);
        if(!visited.contains(map[cY][cX - 1])) enumerateRoom(visited, map[cY][cX - 1], roomId);
    }

    private void openPath(Integer room) {
        openRooms.add(room);
        List<Tile> connectedTiles = Arrays.stream(this.map)
                .flatMap(Arrays::stream)
                .filter(tile -> tile.isInRoom(room))
                .filter(tile -> !isCorner(tile))
                .filter(tile -> tile.getRooms().size() > 1).collect(Collectors.toList());
        Collections.shuffle(connectedTiles);
        for(Tile t : connectedTiles) {
            boolean wasVisited = false;
            for (Integer visited : openRooms)
                if(t.isInRoom(visited) && !visited.equals(room))
                    wasVisited = true;
            if(!wasVisited) {
                t.setEntity(new Door());
                for(Integer roomToVisit : t.getRooms())
                    if(!roomToVisit.equals(room))
                        openPath(roomToVisit);
            }

        }
    }

    private boolean isCorner(Tile t) {
        if(!(t.getEntity() instanceof Wall))
            return false;

        int tX = t.getPosition().getX();
        int tY = t.getPosition().getY();

        if(tX == 0 || tX == WIDTH - 1 || tY == 0 || tY == HEIGHT - 1)
            return true;

        return (map[tY + 1][tX].getEntity() instanceof Wall || map[tY - 1][tX].getEntity() instanceof Wall) && ((map[tY][tX + 1].getEntity() instanceof Wall || map[tY][tX - 1].getEntity() instanceof Wall));
    }

    private void fillIsolatedRooms() {
        for(Tile[] tiles : map)
            for(Tile tile : tiles)
                if(tile.getEntity() == null && !visitedTiles.contains(tile)) {
                    List<Tile> visited = new ArrayList<>();
                    if(!hasWayOut(visited, tile))
                        for(Tile isolatedTile : visited)
                            isolatedTile.setEntity(new Wall());

                    visitedTiles.addAll(visited);
                }
    }

    private boolean hasWayOut(List<Tile> visited, Tile current) {
        if (current.getEntity() instanceof Wall)
            return false;
        if (current.getEntity() instanceof Door)
            return true;

        visited.add(current);

        int cX = current.getPosition().getX();
        int cY = current.getPosition().getY();
        boolean hasWayOut = false;

        if(!visited.contains(map[cY - 1][cX]))
            hasWayOut = hasWayOut(visited, map[cY - 1][cX]);

        if(!visited.contains(map[cY][cX + 1]))
            hasWayOut = hasWayOut || hasWayOut(visited, map[cY][cX + 1]);

        if(!visited.contains(map[cY + 1][cX]))
            hasWayOut = hasWayOut || hasWayOut(visited, map[cY + 1][cX]);

        if(!visited.contains(map[cY][cX - 1]))
            hasWayOut = hasWayOut || hasWayOut(visited, map[cY][cX - 1]);

        return hasWayOut;
    }

}
