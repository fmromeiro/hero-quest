package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;

import java.util.*;
import java.util.function.BiFunction;

public class EnemyFunctions {
    public static BiFunction<Enemy, List<Entity>, List<Point>> moveRandomly = (Enemy enemy, List<Entity> entities) -> {
        Character hero = ((Character)entities.stream()
                .filter(ent -> ent instanceof Character)
                .filter(ent -> ((Character) ent).isHero())
                .findAny().orElse(null));
        if (hero == null)
            return null;
        List<Point> path = new ArrayList<>();
        Point current = enemy.getPosition();
        List<Point> possibleSteps = Arrays.asList(new Point(0, -1), new Point(1, 0), new Point(0, 1), new Point(-1, 0));
        for(int stepsRemaining = enemy.getSteps(); stepsRemaining > 0; stepsRemaining--) {
            Collections.shuffle(possibleSteps);
            if (Point.manhattanDistance(current, hero.getPosition()) == 1)
                break;
            int attempt = 0;
            while (attempt <= 3) {
                Point destination = Point.sum(current, possibleSteps.get(attempt));
                if (entities.stream().noneMatch(entity -> entity.getPosition().equals(destination))) {
                    path.add(current);
                    current = destination;
                    break;
                }
                attempt++;
            }
        }
        path.add(current);
        return path;
    };

    public static BiFunction<Enemy, List<Entity>, List<Point>> followHero = (Enemy enemy, List<Entity> entities) -> {
        Character hero = ((Character)entities.stream()
                .filter(ent -> ent instanceof Character)
                .filter(ent -> ((Character) ent).isHero())
                .findAny().orElse(null));
        if (hero == null)
            return null;
        List<Point> path = aStar(enemy.getPosition(), hero.getPosition(), entities);
        int steps = enemy.getSteps();
        if (path != null && path.size() > steps)
            path = path.subList(0, steps);
        return path;
    };

    private static List<Point> breadthFirstSearch(Point startingPosition, Point goal, List<Entity> obstacles) {
        Queue<List<Point>> paths = new LinkedList<List<Point>>();
        Point[] possibleSteps = { new Point(0, -1), new Point(1, 0), new Point(0, 1), new Point(-1, 0) };
        Set<Point> visited = new HashSet<>();
        paths.add(Arrays.asList(startingPosition));
        visited.add(startingPosition);
        while (!paths.isEmpty()) {
            List<Point> currentPath = paths.remove();
            Point currentPoint = currentPath.get(currentPath.size() - 1);
            for (Point direction : possibleSteps) {
                Point possibleStep = Point.sum(currentPoint, direction);
                if (possibleStep.equals(goal)) {
                    currentPath.add(possibleStep);
                    return currentPath;
                }
                if (visited.contains(possibleStep)
                        || obstacles.stream().anyMatch(ent -> ent.getPosition().equals(possibleStep)))
                    continue;;
                List<Point> nextPath = new LinkedList<>(currentPath);
                nextPath.add(possibleStep);
                visited.add(possibleStep);
                paths.add(nextPath);
            }
        }
        return null;
    }

    private static List<Point> reconstructPath(Map<Point, Point> cameFrom, Point current) {
        List<Point> path = new LinkedList<>();
        path.add(current);
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            path.add(0, current);
        }
        return path;
    }

    private static List<Point> aStar(Point start, Point goal, List<Entity> obstacles) {
        Map<Point, Point> cameFrom = new HashMap<>();

        Map<Point, Integer> gScore = new HashMap<>();
        gScore.put(start, 0);

        Map<Point, Integer> fScore = new HashMap<>();
        fScore.put(start, Point.manhattanDistance(start, goal));

        PriorityQueue<Point> openSet = new PriorityQueue<Point>(
                Comparator.comparingInt(a -> fScore.getOrDefault(a, Integer.MAX_VALUE)));
        openSet.add(start);

        Point[] possibleSteps = { new Point(0, -1), new Point(1, 0), new Point(0, 1), new Point(-1, 0) };

        while (!openSet.isEmpty()) {
            Point current = openSet.poll();
            if (current.equals(goal))
                return reconstructPath(cameFrom, current);

            if (!current.equals(start) && obstacles.stream().anyMatch(ent -> ent.getPosition().equals(current)))
                continue;

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
