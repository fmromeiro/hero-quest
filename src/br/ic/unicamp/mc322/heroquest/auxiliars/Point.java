package br.ic.unicamp.mc322.heroquest.auxiliars;

public class Point {
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

    public void sum(Point operand) {
        this.moveTo(new Point(this.x + operand.x, this.y + operand.y));
    }

    public void subtract(Point operand) {
        this.moveTo(new Point(this.x - operand.x, this.y - operand.y));
    }

    public int manhattanDistance(Point other) {
        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
    }

    public boolean equals(Point other) { return this.x == other.x && this.y == other.y; }
}
