package br.ic.unicamp.mc322.heroquest.entities;

public abstract class StaticEntity implements Entity {
    private boolean seen;

    public void setAsSeen() { this.seen = true; }

    public boolean isSeen() { return this.seen; }
}
