package br.ic.unicamp.mc322.heroquest.controller;

import br.ic.unicamp.mc322.heroquest.items.Equipment;

public interface Renderer {
    void printWholeMap();

    void printVisibleMap();

    void announcePlayerTurn();

    void announceMoveTurn();

    void alertMoveActionUsed();

    void alertMainActionUsed();

    void announceAttackTurn();

    void printCurrentWeapon(Equipment currentWeapon);

    void alertCouldNotInterpretCommand();

    void announceAttackTurnEnd();

    void alertMissingTarget();
}
