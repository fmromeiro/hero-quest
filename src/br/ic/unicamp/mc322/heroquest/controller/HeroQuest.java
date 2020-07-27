package br.ic.unicamp.mc322.heroquest.controller;

import br.ic.unicamp.mc322.heroquest.auxiliars.MapXMLBuilder;
import br.ic.unicamp.mc322.heroquest.auxiliars.Point;
import br.ic.unicamp.mc322.heroquest.entities.Character;
import br.ic.unicamp.mc322.heroquest.entities.*;
import br.ic.unicamp.mc322.heroquest.items.Equipment;
import br.ic.unicamp.mc322.heroquest.items.Weapon;
import br.ic.unicamp.mc322.heroquest.spells.Spell;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class HeroQuest {
    private final Scanner scanner = new Scanner(System.in);
    private final Renderer renderer;
    private char entityId = '0';

    public HeroQuest(Renderer renderer) {
        this.renderer = renderer;
    }

    private static void createRandomMap() {
        Dungeon dungeon = Dungeon.getInstance();
        dungeon.addRandomRooms();
    }

    public void createMapFromXML(String dungeonFile) throws Exception {
        MapXMLBuilder builder = new MapXMLBuilder(dungeonFile);
        builder.buildMap();
        builder.addEntities();
    }

    public void runGame() throws Exception {
        this.setUp();
        boolean running = true;
        while (running)
            running = this.mainLoop();
    }

    private void setUp() throws Exception {
        String dungeonFile = askForXML();
        if(dungeonFile.isEmpty()) {
            // randomizar mapa, inimigos, tesouros, armadilhas etc
            createRandomMap();
            boolean end = false;
            Character playerCharacter = null;
            while (!end) {
                renderer.printChooseCharacter();
                String character = scanner.nextLine().trim().toLowerCase();
                switch (character) {
                    case "barbarian": playerCharacter = Character.getBarbarian();
                    case "dwarf": playerCharacter = playerCharacter == null ? Character.getDwarf() : playerCharacter;
                    case "sorcerer": playerCharacter = playerCharacter == null ? Character.getSorcerer() : playerCharacter;
                    case "elf":
                        playerCharacter = playerCharacter == null ? Character.getElf() : playerCharacter;
                        end = true;
                        break;
                    default:
                        renderer.alertCouldNotInterpretCommand();
                }
            }
            Dungeon.getInstance().addEntity(playerCharacter, Dungeon.getInstance().getRandomFreePoint());
        }
        else {
            createMapFromXML(dungeonFile);
        }
        for (int i = 0; i < 10; i++) {
            Dungeon.getInstance().addEntity(Treasure.randomTreasure(), Dungeon.getInstance().getRandomFreePoint());
        }
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
                renderer.printVisibleMap();
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
            else if(!mainAction) {
                renderer.announceAttackTurn();
                mainAction = true;
                handleMainActionInput(character);
            }
            else {
                renderer.announceMoveTurn();
                moveAction = true;
                handleMoveInput(character);
            }
        }
    }

    private void handleMainActionInput(Character character) {
        renderer.printCurrentWeapon(character.getCurrentWeapon());
        renderer.printAvailableSpells(character.getSpellBook());
        boolean actionDone = false;
        while (!actionDone) {
            renderer.printVisibleMap();
            renderer.printAvailableActions("switch", "equipment", "attack [id]", "collect", "cast [spell name] [# tiles to the right] [# tiles up]", "skip");
            String command = scanner.nextLine().toLowerCase();
            String[] commands = command.split("\\s+");
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
                        Dungeon.getInstance().getSecondaryEntities().stream()
                                .filter(ent -> Point.octileDistance(character.getPosition(), ent.getPosition()) <= 1)
                                .filter(ent -> ent instanceof Treasure)
                                .forEach(ent ->  {
                                    character.collect((Treasure)ent);
                                    Dungeon.getInstance().removeSecondaryEntity(ent.getPosition());
                                });
                        actionDone = true;
                        break;
                    case "cast":
                        if (commands.length > 1) {
                            Map.Entry<Integer, Spell> entry = character.getSpellBook().entrySet().stream()
                                    .filter(_entry
                                            -> command.split("\\s+", 1)[1].contains(_entry.getValue().getName().toLowerCase().trim()))
                                    .findFirst().orElse(null);
                            if (entry != null) {
                                try {
                                    int x = Integer.parseInt(commands[commands.length - 2]);
                                    int y = Integer.parseInt(commands[commands.length - 1]);
                                    character.castSpell(entry.getValue(), Point.sum(character.getPosition(), new Point(x, y)));
                                    actionDone = true;
                                } catch (Exception ignored){}
                            }
                            else
                                renderer.alertInvalidSpell();
                        }
                        else
                            renderer.alertCouldNotInterpretCommand();
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
            renderer.printCurrentWeapon(character.getCurrentWeapon());
            renderer.printAvailableActions("equip [id]", "righthand (use the weapon in the right hand to attack enemies)", "lefthand (use the weapon in the left hand to attack enemies)", "finish");
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
        Character hero = Dungeon.getInstance().getHero();
        if (!hero.isAlive())
            Dungeon.getInstance().removeEntity(hero.getPosition());
    }

    private void handleMoveInput(Character hero) {
        int steps = 0;
        int limitSteps = hero.getSteps();
        label:
        while (steps < limitSteps) {
            renderer.printVisibleMap();
            renderer.printAvailableActions("'w', 'a', 's', 'd'", "open door", "stop");
            renderer.printAvailableSteps(limitSteps - steps);
            String input = scanner.nextLine().toLowerCase();
            String[] commands = input.split("\\s+");
            Point direction = null;
            if (commands.length > 0) {
                switch (commands[0]) {
                    case "w":
                        direction = Point.Direction.UP.getPosition();
                    case "a":
                        direction = direction == null ? Point.Direction.LEFT.getPosition() : direction;
                    case "s":
                        direction = direction == null ? Point.Direction.DOWN.getPosition() : direction;
                    case "d":
                        direction = direction == null ? Point.Direction.RIGHT.getPosition() : direction;
                        Point movement = Point.sum(hero.getPosition(), direction);
                        if (Dungeon.getInstance().canSetEntityTo(movement)) {
                            Dungeon.getInstance().moveEntity(hero, Point.sum(hero.getPosition(), direction));
                            steps++;
                        }
                        else
                            renderer.alertCouldNotMove();
                        break;
                    case "open":
                        Dungeon.getInstance().getEntities().stream()
                                .filter(ent -> Point.manhattanDistance(hero.getPosition(), ent.getPosition()) == 1)
                                .filter(ent -> ent instanceof Door)
                                .forEach(ent -> ((Door) ent).open());

                        break;
                    case "stop":
                        break label;
                    case "guguhacker":  // cheat to see whole map
                        renderer.printWholeMapv2(Dungeon.getInstance());
                        System.out.println("safadinho...");
                        scanner.nextLine();
                        break;
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

    public String askForXML() {
        System.out.println("Digite o nome de um arquivo XML para carregar um mapa (Ex: resources/Dungeon.xml) caso contrário o mapa será gerado aleatóriamente");
        return scanner.nextLine();
    }
}
