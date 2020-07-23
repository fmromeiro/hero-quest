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
        List<Point> path = breadthFirstSearch(enemy.getPosition(), hero.getPosition(), entities);
        int steps = enemy.getSteps();
        if (path != null && path.size() > steps)
            path = path.subList(0, steps);
        return path;
    };

    public static

    private static List<Point> breadthFirstSearch(Point startingPosition, Point goal, List<Entity> obstacles) {
        Queue<List<Point>> paths = new LinkedList<List<Point>>();
        Point[] possibleSteps = { new Point(0, -1), new Point(1, 0), new Point(0, 1), new Point(-1, 0) };
        Set<Point> visited = new HashSet<>();
        paths.add(Arrays.asList(startingPosition));
        while (!paths.isEmpty()) {
            List<Point> currentPath = paths.remove();
            Point currentPoint = currentPath.get(currentPath.size() - 1);
            visited.add(currentPoint);
            for (Point direction : possibleSteps) {
                Point possibleStep = Point.sum(currentPoint, direction);
                if (possibleStep.equals(goal)) {
                    currentPath.add(currentPoint);
                    return currentPath;
                }
                if (visited.contains(possibleStep)
                        || obstacles.stream().anyMatch(ent -> ent.getPosition().equals(possibleStep)))
                    continue;;
                List<Point> nextPath = new LinkedList<>(currentPath);
                nextPath.add(currentPoint);
                paths.add(nextPath);
            }
        }
        return null;
    }
}
