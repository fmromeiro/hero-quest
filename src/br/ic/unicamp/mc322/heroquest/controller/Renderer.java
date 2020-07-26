package br.ic.unicamp.mc322.heroquest.controller;

import br.ic.unicamp.mc322.heroquest.items.Equipment;
import br.ic.unicamp.mc322.heroquest.items.Item;

import java.util.Map;

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

    void printCurrentEquipment(Map<String, Equipment> character);

    void printInventory(Map<Integer, Item> inventory);

    void alertMissingParameter(String attack);

    void printAvailableActions(String ... actions);

    void alertInvalidItem(int id);

    void printAvailableSteps(int steps);
}
