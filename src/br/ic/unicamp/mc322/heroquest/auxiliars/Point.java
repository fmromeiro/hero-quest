package br.ic.unicamp.mc322.heroquest.auxiliars;

public class Point {
    public static final Point UP = new Point(0, -1);
    public static final Point RIGHT = new Point(1, 0);
    public static final Point DOWN = new Point(0, 1);
    public static final Point LEFT = new Point(-1, 0);

    private int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point point) {
        this.x = point.x;
        this.y = point.y;
    }

    public void moveTo(Point point) {
        this.x = point.x;
        this.y = point.y;
    }

    public int getX() { return this.x; }

    public int getY() { return this.y; }

    public void sum(Point other) {
        this.x += other.x;
        this.y += other.y;
    }
}
