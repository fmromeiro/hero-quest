package br.ic.unicamp.mc322.heroquest.auxiliars;

import java.util.Objects;

public class Point {
    public enum Direction {
        UP (new Point(0, -1)),
        RIGHT(new Point(1, 0)),
        DOWN(new Point(0, 1)),
        LEFT(new Point(-1, 0));

        private final Point position;

        Direction(Point direction) { this.position = direction; }

        public Point getPosition() { return new Point(position); }
    }

    private int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point point) {
        this.x = point.x;
        this.y = point.y;
    }

    public int getX() { return this.x; }

    public int getY() { return this.y; }

    public boolean equals(Point other) { return this.x == other.x && this.y == other.y; }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        return this.equals((Point)other);
    }

    public static Point sum(Point a, Point b) {
        return new Point(a.x + b.x, a.y + b.y);
    }

    public static Point[] bresenhamLine(Point start, Point end) {
        int x1, y1, x2, y2;
        x1 = start.x;
        y1 = start.y;
        x2 = end.x;
        y2 = end.y;

        int dx = x2 - x1;
        int dy = y2 - y1;
        boolean is_steep = Math.abs(dy) > Math.abs(dx);

        if (is_steep) {
            int aux;
            aux = x1;
            x1 = y1;
            y1 = aux;

            aux = x2;
            x2 = y2;
            y2 = aux;
        }

        boolean swapped = false;
        if (x1 > x2) {
            int aux;
            aux = x1;
            x1 = x2;
            x2 = aux;

            aux = y1;
            y1 = y2;
            y2 = aux;

            swapped = true;
        }

        dx = x2 - x1;
        dy = y2 - y1;

        double error = Math.round(dx/2.0);
        int ystep = y1 < y2 ? 1 : -1;

        int y = y1;
        Point[] points = new Point[x2 + 1 - x1];
        for(int x = x1; x <= x2; x++) {
            points[x-x1] = is_steep ? new Point(y, x) : new Point(x, y);
            error -= Math.abs(dy);
            if (error < 0) {
                y += ystep;
                error +=  dx;
            }
        }

        if (swapped) {
            int size = x2 + 1 - x1;
            for (int i = 0; i < size / 2; i++) {
                Point aux = points[i];
                points[i] = points[size - 1 - i];
                points[size - 1 - i] = aux;
            }
        }

        return points;
    }

    public static double euclideanDistance(Point a, Point b) {
        return Math.hypot(a.x - b.x, a.y - b.y);
    }

    public static int manhattanDistance(Point a, Point b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }
}
