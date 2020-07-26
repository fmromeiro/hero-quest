package br.ic.unicamp.mc322.heroquest.controller;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;
import br.ic.unicamp.mc322.heroquest.entities.Character;
import br.ic.unicamp.mc322.heroquest.entities.*;

import java.util.List;
import java.util.Scanner;

public class HeroQuest {
    private final Scanner scanner = new Scanner(System.in);
    private final Renderer renderer;

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
        while (true)
            this.mainLoop();
    }

    private void setUp() throws Exception {
        // randomizar inimigos, tesouros, armadilhas etc
        createRandomMap();
        //Dungeon.getInstance().addEntity(new Door(), new Point(15, 8));
        //createDefaultMap();
        //Dungeon.getInstance().addEntity(new Door(), new Point(15, 8));
        for (int i = 0; i < 5; i++) {
            Dungeon.getInstance().addEntity(Treasure.randomTreasure(), Dungeon.getInstance().getRandomFreePoint());

        }
        Dungeon.getInstance().addEntity(Character.getDefaultHero("Player"), Dungeon.getInstance().getRandomFreePoint());
        //Dungeon.getInstance().addEntity(Character.getMeleeSkeleton("Skeleton"), new Point(16, 25));
        //Dungeon.getInstance().addEntity(Character.getSkeletonMage("Skeleton Mage"), new Point(34, 25));
        //Dungeon.getInstance().addEntity(Character.getGoblin("Goblin"), new Point(32, 25));
        Renderer.printVisibleMap(Dungeon.getInstance());
    }

    private void mainLoop() {
        this.handleHeroTurn(Dungeon.getInstance().getHero());
        List<Enemy> enemies = Dungeon.getInstance().getEnemies();
        for (Enemy enemy : enemies) {
            if (Dungeon.getInstance().isActive(enemy.getPosition())) {
                this.handleEnemyTurn(enemy);
                renderer.printWholeMap();
            }
        }
    }

    private void handleHeroTurn(Character character) {
        renderer.announcePlayerTurn();
        boolean moveAction = false, mainAction = false, end = false;
        while (!end && (!moveAction || !mainAction)) {
            String input = scanner.nextLine().toLowerCase();
            String[] commands = input.split("\\s+");
            if (commands.length > 0) {
                if (commands[0].equals("move")) {
                    if (moveAction) {
                        renderer.alertMoveActionUsed();
                        continue;
                    }
                    renderer.announceMoveTurn();
                    moveAction = true;
                    handleMoveInput(character);
                } else if (commands[0].equals("attack")) {
                    if (mainAction) {
                        renderer.alertMainActionUsed();
                        continue;
                    }
                    renderer.announceAttackTurn();
                    mainAction = true;
                    handleAttackInput(character);
                }
            }
            else {
                renderer.alertCouldNotInterpretCommand();
            }
        }
    }

    private void handleAttackInput(Character character) {
        renderer.printCurrentWeapon(character.getCurrentWeapon());
        boolean attacked = false;
        while (!attacked) {
            String[] commands = scanner.nextLine().toLowerCase().split("\\s+");
            if (commands.length > 0) {
                if (commands[0].equals("switch") || commands[0].equals("equipment")) handleEquipment();
                else if (commands[0].equals("attack")) {
                    if (commands.length > 1) {
                        Entity target = Dungeon.getInstance().getEntityByStringRepresentation(commands[1]).stream()
                                .findFirst().orElse(null);
                        if (target instanceof Character) {
                            try {
                                character.attack((Character) target);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else
                        renderer.alertMissingTarget();
                }
                else if (commands[0].equals("skip")){
                    renderer.announceAttackTurnEnd();
                    attacked = true;
                }

                else
                    renderer.alertCouldNotInterpretCommand();
            }
            else
                renderer.alertCouldNotInterpretCommand();
        }
    }

    private void handleEquipment() {

    }

    private void handleEnemyTurn(Enemy enemy) {
        handleEnemyMove((Enemy)enemy);
    }

    private void handleMoveInput(Character hero) {
        int steps = 0;
        int limitSteps = hero.getSteps();
        while (steps < limitSteps) {
            String input = scanner.nextLine().toLowerCase();
            String[] commands = input.split("\\s+");
            if (commands.length == 2) {
                if (commands[0].equals("move")) {
                    Point.Direction direction = Point.Direction.UP;
                    switch (commands[1]) {
                        case "up":
                            direction = Point.Direction.UP;
                            break;
                        case "right":
                            direction = Point.Direction.RIGHT;
                            break;
                        case "down":
                            direction = Point.Direction.DOWN;
                            break;
                        case "left":
                            direction = Point.Direction.LEFT;
                            break;
                    }
                    Dungeon.getInstance().moveEntity(hero, Point.sum(hero.getPosition(), direction.getPosition()));
                    steps++;
                } else if (commands[0].equals("open")) {
                    Dungeon.getInstance().getEntities().stream()
                            .filter(ent -> Point.manhattanDistance(hero.getPosition(), ent.getPosition()) == 1)
                            .filter(ent -> ent instanceof Door)
                            .forEach(ent -> ((Door) ent).open());

                }
            }
            else if (input.equals("collect")) {
                Dungeon.getInstance().getEntities().stream()
                        .filter(ent -> Point.octileDistance(hero.getPosition(), ent.getPosition()) == 1)
                        .filter(ent -> ent instanceof Treasure)
                        .forEach(ent ->  {
                            hero.collect((Treasure)ent);
                            Dungeon.getInstance().removeEntity(ent.getPosition());
                        });
            }
            else if (input.equals("guguhacker")) {
                Renderer.printWholeMapv2(Dungeon.getInstance());
                System.out.println("safadinho...");
                scanner.nextLine();
            }
            else if (commands[0].equals("stop"))
                break;
        }
    }

    private void handleEnemyMove(Enemy enemy) {
        List<Point> path = enemy.move();
        if (path != null && path.size() > 1)
            Dungeon.getInstance().moveEntity(enemy, path.get(path.size() - 2));
    }
}
