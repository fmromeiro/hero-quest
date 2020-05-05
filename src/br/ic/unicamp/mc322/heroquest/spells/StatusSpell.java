package br.ic.unicamp.mc322.heroquest.spells;

public class StatusSpell extends Spell{
    public enum Status {SLEEPY, SCARED, PARALYZED, BLINDED, HYPNOTIZED}
    Status status;
    public StatusSpell(String name, Element spellElement, int range, Status status) {
        super(name, spellElement, range);
        this.status = status;
    }
}
