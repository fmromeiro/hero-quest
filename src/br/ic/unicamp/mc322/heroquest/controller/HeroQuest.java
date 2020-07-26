package br.ic.unicamp.mc322.heroquest.controller;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;
import br.ic.unicamp.mc322.heroquest.entities.Character;
import br.ic.unicamp.mc322.heroquest.entities.*;
import br.ic.unicamp.mc322.heroquest.items.Equipment;
import br.ic.unicamp.mc322.heroquest.items.Weapon;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class HeroQuest {
    private final Scanner scanner = new Scanner(System.in);
    private final Renderer renderer;
    private char entityId = 47;

    public HeroQuest(Renderer renderer) {
        this.renderer = renderer;
    }

    private static void createRandomMap() {
        Dungeon dungeon = Dungeon.getInstance();
        dungeon.addRandomRooms();
    }
    private static void createDefaultMap() {
        Dungeon dungeon = Dungeon.getInstance();

        // Sala 0 - Corredores
        dungeon.addRoom(new Point(0, 0), new Point(35, 26));
        dungeon.removeFromRoom(0, new Point(3, 3), new Point(32, 23));
        dungeon.addToRoom(0, new Point(1, 12), new Point(34, 14));
        dungeon.addToRoom(0, new Point(16, 2), new Point(19, 24));
        dungeon.addToRoom(0, new Point(12, 8), new Point(23, 18));

        // Bloco superior esquerdo - salas de 01 a 05
        dungeon.addRoom(new Point(2, 2), new Point(7, 6));
        dungeon.addRoom(new Point(7, 2), new Point(12, 6));
        dungeon.addRoom(new Point(12, 2), new Point(16, 8));
        dungeon.addRoom(new Point(2, 6), new Point(7, 12));
        dungeon.addRoom(new Point(7, 6), new Point(12, 12));

        // Bloco superior direito - salas de 06 a 10
        dungeon.addRoom(new Point(19, 2), new Point(23, 8));
        dungeon.addRoom(new Point(23, 2), new Point(28, 7));
        dungeon.addRoom(new Point(28, 2), new Point(33, 7));
        dungeon.addRoom(new Point(23, 7), new Point(28, 12));
        dungeon.addRoom(new Point(28, 7), new Point(33, 12));

        // Sala central - 11
        dungeon.addRoom(new Point(14,  10), new Point(21, 16));
        dungeon.removeFromRoom(0, new Point(15, 11), new Point(20, 15));

        // Bloco inferior esquerdo - salas de 12 a 17
        dungeon.addRoom(new Point(2, 14), new Point(6, 19));
        dungeon.addRoom(new Point(6, 14), new Point(9, 18));
        dungeon.addRoom(new Point(9, 14), new Point(12, 18));
        dungeon.addRoom(new Point(2, 19), new Point(7, 24));
        dungeon.addRoom(new Point(7, 18), new Point(12, 24));
        dungeon.addRoom(new Point(12, 18), new Point(16, 24));

        // Bloco inferior direito - salas de 18 a 22
        dungeon.addRoom(new Point(23, 14), new Point(28, 19));
        dungeon.removeFromRoom(18, new Point(23, 19), new Point(23, 19));
        dungeon.addRoom(new Point(28, 14), new Point(33, 19));
        dungeon.addRoom(new Point(19, 18), new Point(24, 24));
        dungeon.addRoom(new Point(24, 19), new Point(28, 24));
        dungeon.addRoom(new Point(28, 19), new Point(33, 24));
    }

    public void runGame() throws Exception {
        this.setUp();
        boolean running = true;
        while (running)
            running = this.mainLoop();
    }

    private void setUp() throws Exception {
        // randomizar inimigos, tesouros, armadilhas etc
        createRandomMap();
        //Dungeon.getInstance().addEntity(new Door(), new Point(15, 8));
        //createDefaultMap();
        //Dungeon.getInstance().addEntity(new Door(), new Point(15, 8));
        for (int i = 0; i < 10; i++) {
            Dungeon.getInstance().addEntity(Treasure.randomTreasure(), Dungeon.getInstance().getRandomFreePoint());

        }
        Dungeon.getInstance().addEntity(Character.getDefaultHero(entityId++), Dungeon.getInstance().getRandomFreePoint());
        //Dungeon.getInstance().addEntity(Character.getMeleeSkeleton("Skeleton"), new Point(16, 25));
        //Dungeon.getInstance().addEntity(Character.getSkeletonMage("Skeleton Mage"), new Point(34, 25));
        //Dungeon.getInstance().addEntity(Character.getGoblin("Goblin"), new Point(32, 25));
        renderer.printVisibleMap();
    }

    private boolean mainLoop() {
        this.handleHeroTurn(Dungeon.getInstance().getHero());
        if (Dungeon.getInstance().getEnemies().size() <= 0) {
            renderer.printHeroVictory();
            return false;
        }
        List<Enemy> enemies = Dungeon.getInstance().getEnemies();
        for (Enemy enemy : enemies) {
            if (!Dungeon.getInstance().getHero().isAlive()) {
                renderer.printHeroDefeat();
                return false;
            }
            if (Dungeon.getInstance().isActive(enemy.getPosition())) {
                this.handleEnemyTurn(enemy);
                renderer.printWholeMap();
            }
        }
        if (!Dungeon.getInstance().getHero().isAlive()) {
            renderer.printHeroDefeat();
            return false;
        }
        return true;
    }

    private void handleHeroTurn(Character character) {
        renderer.announcePlayerTurn();
        boolean moveAction = false, mainAction = false, end = false;
        while (!end && (!moveAction || !mainAction)) {
            if(!moveAction && !mainAction) {
                renderer.printAvailableActions("move", "main action");
            }
            else if(!mainAction) {
                renderer.printAvailableActions("main action");
            }
            else
                renderer.printAvailableActions("move");
            String[] commands = scanner.nextLine().toLowerCase().split("\\s+");
            if (commands.length > 0) {
                if (commands[0].equals("move")) {
                    if (moveAction) {
                        renderer.alertMoveActionUsed();
                        continue;
                    }
                    renderer.announceMoveTurn();
                    moveAction = true;
                    handleMoveInput(character);
                } else if (commands[0].equals("main")) {
                    if (mainAction) {
                        renderer.alertMainActionUsed();
                        continue;
                    }
                    renderer.announceAttackTurn();
                    mainAction = true;
                    handleMainActionInput(character);
                }
            }
            else {
                renderer.alertCouldNotInterpretCommand();
            }
        }
    }

    private void handleMainActionInput(Character character) {
        renderer.printCurrentWeapon(character.getCurrentWeapon());
        boolean actionDone = false;
        while (!actionDone) {
            renderer.printAvailableActions("switch", "equipment", "attack [id]", "collect", "skip");
            String[] commands = scanner.nextLine().toLowerCase().split("\\s+");
            if (commands.length > 0) {
                switch (commands[0]) {
                    case "switch":
                    case "equipment":
                        handleEquipment(character);
                        break;
                    case "attack":
                        if (commands.length > 1) {
                            Entity target = Dungeon.getInstance().getEntityByStringRepresentation(commands[1]).stream()
                                    .findFirst().orElse(null);
                            if (target instanceof Character) {
                                try {
                                    character.attack((Character) target);
                                    if (!((Character) target).isAlive()) {
                                        Dungeon.getInstance().removeEntity(target.getPosition());
                                    }
                                    actionDone = true;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else
                            renderer.alertMissingParameter(commands[0]);
                        break;
                    case "collect":
                        Dungeon.getInstance().getEntities().stream()
                                .filter(ent -> Point.octileDistance(character.getPosition(), ent.getPosition()) == 1)
                                .filter(ent -> ent instanceof Treasure)
                                .forEach(ent -> {
                                    character.collect((Treasure) ent);
                                    Dungeon.getInstance().removeEntity(ent.getPosition());
                                });
                        actionDone = true;
                        break;
                    case "skip":
                        renderer.announceAttackTurnEnd();
                        actionDone = true;
                        break;
                    default:
                        renderer.alertCouldNotInterpretCommand();
                        break;
                }
            }
            else
                renderer.alertCouldNotInterpretCommand();
        }
        renderer.printVisibleMap();
    }

    private void handleEquipment(Character character) {
        boolean end = false;
        while (!end) {
            renderer.printCurrentEquipment(character.getCurrentlyEquipped());
            renderer.printInventory(character.getInventory());
            renderer.printAvailableActions("equip [id]", "righthand", "lefthand", "finish");
            String[] commands = scanner.nextLine().toLowerCase().split("\\s+");
            if (commands.length > 0) {
                switch (commands[0]) {
                    case "equip":
                        if (commands.length > 1) {
                            int id = Integer.parseInt(commands[1]);
                            if (character.getInventory().getOrDefault(id, null) instanceof Equipment) {
                                Equipment equip = (Equipment) character.dropItem(id);
                                character.equip(equip);
                            }
                            else
                                renderer.alertInvalidItem(id);
                        }
                        else
                            renderer.alertMissingParameter(commands[0]);
                        break;
                    case "righthand": character.chooseWeapon(Character.RIGHT_HAND); break;
                    case "lefthand": character.chooseWeapon(Character.LEFT_HAND); break;
                    case "finish":
                        end = true;
                        break;
                    default:
                        renderer.alertCouldNotInterpretCommand();
                }
            }
            else
                renderer.alertCouldNotInterpretCommand();
        }
    }

    private void handleEnemyTurn(Enemy enemy) {
        handleEnemyMove(enemy);
        enemy.attack();
    }

    private void handleMoveInput(Character hero) {
        int steps = 0;
        int limitSteps = hero.getSteps();
        while (steps < limitSteps) {
            renderer.printVisibleMap();
            renderer.printAvailableActions("'w', 'a', 's', 'd'", "open door", "stop");
            renderer.printAvailableSteps(limitSteps - steps);
            String input = scanner.nextLine().toLowerCase();
            String[] commands = input.split("\\s+");
            if (commands.length > 0) {
                if (commands[0].equals("w")) {
                    Dungeon.getInstance().moveEntity(hero, Point.sum(hero.getPosition(), Point.Direction.UP.getPosition()));
                    steps++;
                } else if (commands[0].equals("a")) {
                    Dungeon.getInstance().moveEntity(hero, Point.sum(hero.getPosition(), Point.Direction.LEFT.getPosition()));
                    steps++;
                } else if (commands[0].equals("s")) {
                    Dungeon.getInstance().moveEntity(hero, Point.sum(hero.getPosition(), Point.Direction.DOWN.getPosition()));
                    steps++;
                } else if (commands[0].equals("d")) {
                    Dungeon.getInstance().moveEntity(hero, Point.sum(hero.getPosition(), Point.Direction.RIGHT.getPosition()));
                    steps++;
                } else if (commands[0].equals("open")) {
                    Dungeon.getInstance().getEntities().stream()
                            .filter(ent -> Point.manhattanDistance(hero.getPosition(), ent.getPosition()) == 1)
                            .filter(ent -> ent instanceof Door)
                            .forEach(ent -> ((Door) ent).open());

                }
                else if (commands[0].equals("stop"))
                    break;
                else if (commands[0].equals("guguhacker")) { // cheat to see whole map
                    renderer.printWholeMapv2(Dungeon.getInstance());
                    System.out.println("safadinho...");
                    scanner.nextLine();
                }
            }
        }
        renderer.printVisibleMap();
    }

    private void handleEnemyMove(Enemy enemy) {
        List<Point> path = enemy.move();
        if (path != null && path.size() > 1)
            Dungeon.getInstance().moveEntity(enemy, path.get(path.size() - 2));
    }
}
