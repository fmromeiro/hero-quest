package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class EnemyFunctions {
    public static Function<Enemy, List<Point>> moveRandomly = (Enemy enemy) -> {
        Character hero = Dungeon.getInstance().getHero();
        if (hero == null)
            return null;
        List<Point> path = new ArrayList<>();
        Point current = enemy.getPosition();
        List<Point> possibleSteps = new LinkedList<>(Point.crossAdjacents);
        for(int stepsRemaining = enemy.getSteps(); stepsRemaining > 0; stepsRemaining--) {
            Collections.shuffle(possibleSteps);
            if (Point.manhattanDistance(current, hero.getPosition()) == 1)
                break;
            int attempt = 0;
            while (attempt <= 3) {
                Point destination = Point.sum(current, possibleSteps.get(attempt));
                Entity atDestination = Dungeon.getInstance().entityAt(destination);
                if (Dungeon.getInstance().isActive(destination)
                        && atDestination != null && atDestination.canBeOverlapped()) {
                    path.add(current);
                    current = destination;
                    break;
                }
                attempt++;
            }
            if (attempt == 4)
                break;
        }
        path.add(current);
        return path;
    };

    public static Function<Enemy, List<Point>> followHero = (Enemy enemy) -> {
        Character hero = Dungeon.getInstance().getHero();
        if (hero == null)
            return null;
        List<Point> path = aStar(enemy.getPosition(), hero.getPosition());
        int steps = enemy.getSteps();
        if (path != null && path.size() > steps)
            path = path.subList(0, steps);
        return path;
    };

    private static List<Point> reconstructPath(Map<Point, Point> cameFrom, Point current) {
        List<Point> path = new LinkedList<>();
        path.add(current);
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            path.add(0, current);
        }
        return path;
    }

    private static List<Point> aStar(Point start, Point goal) {
        Map<Point, Point> cameFrom = new HashMap<>();

        Map<Point, Integer> gScore = new HashMap<>();
        gScore.put(start, 0);

        Map<Point, Integer> fScore = new HashMap<>();
        fScore.put(start, Point.manhattanDistance(start, goal));

        PriorityQueue<Point> openSet = new PriorityQueue<Point>(
                Comparator.comparingInt(a -> fScore.getOrDefault(a, Integer.MAX_VALUE)));
        openSet.add(start);

        Point[] possibleSteps = Point.crossAdjacents.toArray(new Point[0]);

        int bestPathCost = Integer.MAX_VALUE;
        List<Point> bestPath = null;

        while (!openSet.isEmpty()) {
            Point current = openSet.poll();
            if (current.equals(goal))
                return reconstructPath(cameFrom, current);

            Entity atCurrentPosition = Dungeon.getInstance().entityAt(current);
            if (!Dungeon.getInstance().isActive(current)
                    || (!current.equals(start) && atCurrentPosition != null && !atCurrentPosition.canBeOverlapped()))
                continue;

            if (Point.manhattanDistance(current, goal) < bestPathCost) {
                bestPathCost = Point.manhattanDistance(current, goal);
                bestPath = reconstructPath(cameFrom, current);
            }

            if (openSet.size() > 100 && reconstructPath(cameFrom, current).size() >= 50)
                return bestPath;

            for (Point step : possibleSteps) {
                Point destination = Point.sum(current, step);
                int tentativeGScore = gScore.getOrDefault(current, Integer.MAX_VALUE) + 1;
                if (tentativeGScore < gScore.getOrDefault(destination, Integer.MAX_VALUE)) {
                    cameFrom.put(destination, current);
                    gScore.put(destination, tentativeGScore);
                    fScore.put(destination, tentativeGScore + Point.manhattanDistance(destination, goal));
                    if (!openSet.contains(destination))
                        openSet.add(destination);
                }
            }
        }
        return null;
    }
}
