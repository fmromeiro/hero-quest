package br.ic.unicamp.mc322.heroquest.controller;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;
import br.ic.unicamp.mc322.heroquest.entities.*;
import br.ic.unicamp.mc322.heroquest.entities.Character;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class HeroQuest {
    private final Scanner scanner = new Scanner(System.in);

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

    public void setUp() throws Exception {
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

    public void mainLoop() {
        List<Entity> entities = Dungeon.getInstance().getEntities().stream()
                .filter(ent -> ent instanceof Character)
                .collect(Collectors.toList());
        for (Entity entity : entities) {
            this.handleTurn(entity);
            Renderer.printVisibleMap(Dungeon.getInstance());
        }
    }

    public void runGame() throws Exception {
        this.setUp();
        while (true)
            this.mainLoop();
    }

    public void handleTurn(Entity entity) {
        if (((Character) entity).isHero())
            handleMoveInput((Character)entity);
        else if (entity instanceof Enemy)
            handleEnemyMove((Enemy)entity);
    }

    public void handleMoveInput(Character hero) {
        System.out.println("Player's turn (type h for help)");
        String input = scanner.nextLine().toLowerCase();
//        String[] commands = input.split("\\s+");
//        if (commands.length == 2)
            if (input.equals("h")) {
                System.out.println("Commands:\n > 'w', 'a', 's', 'd' (for movement)\n > 'open door'\n > 'collect'");
                input = scanner.nextLine().toLowerCase();
            }

            if (input.equals("w")) {
                Dungeon.getInstance().moveEntity(hero, Point.sum(hero.getPosition(), Point.Direction.UP.getPosition()));
            }
            else if (input.equals("a")) {
                Dungeon.getInstance().moveEntity(hero, Point.sum(hero.getPosition(), Point.Direction.LEFT.getPosition()));
            }
            else if (input.equals("s")) {
                Dungeon.getInstance().moveEntity(hero, Point.sum(hero.getPosition(), Point.Direction.DOWN.getPosition()));
            }
            else if (input.equals("d")) {
                Dungeon.getInstance().moveEntity(hero, Point.sum(hero.getPosition(), Point.Direction.RIGHT.getPosition()));
            }
            else if (input.equals("open door")) {
                Dungeon.getInstance().getEntities().stream()
                        .filter(ent -> Point.manhattanDistance(hero.getPosition(), ent.getPosition()) == 1)
                        .filter(ent -> ent instanceof Door)
                        .forEach(ent -> ((Door)ent).open());

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
    }

    public void handleEnemyMove(Enemy enemy) {
        List<Point> path = enemy.getMovement(Dungeon.getInstance().getEntities());
        if (path != null && path.size() > 1)
            Dungeon.getInstance().moveEntity(enemy, path.get(path.size() - 2));
    }
}
