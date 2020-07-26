package br.ic.unicamp.mc322.heroquest.auxiliars;
import br.ic.unicamp.mc322.heroquest.entities.*;
import br.ic.unicamp.mc322.heroquest.entities.Character;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class MapXMLBuilder {
    private final Dungeon dungeon;
    private final Document doc;
    public MapXMLBuilder(String fileName) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        File mapFile = new File(fileName);
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        this.doc = dBuilder.parse(mapFile);
        if(!doc.getDocumentElement().getNodeName().equals("Dungeon"))
            throw new Exception("Arquivo inválido");
        dungeon = Dungeon.getInstance();
    }
    public void buildMap()  {
        Element dungeonMap = doc.getDocumentElement();
        Element rooms = (Element) dungeonMap.getElementsByTagName("Rooms").item(0);
        NodeList roomList = dungeonMap.getElementsByTagName("Rooms").item(0).getChildNodes();
        for (int i = 0; i < roomList.getLength(); i++) {
            Node node = roomList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element room = (Element) roomList.item(i);
                if (room.getParentNode() == rooms) {
                    switch (room.getNodeName()) {
                        case "Room":
                            Element topLeft = (Element) room.getElementsByTagName("TopLeft").item(0);
                            Element bottomRight = (Element) room.getElementsByTagName("BottomRight").item(0);
                            dungeon.addRoom(
                                    new Point(Integer.parseInt(topLeft.getElementsByTagName("X").item(0).getChildNodes().item(0).getNodeValue().trim()),
                                            Integer.parseInt(topLeft.getElementsByTagName("Y").item(0).getChildNodes().item(0).getNodeValue().trim())),
                                    new Point(Integer.parseInt(bottomRight.getElementsByTagName("X").item(0).getChildNodes().item(0).getNodeValue().trim()),
                                            Integer.parseInt(bottomRight.getElementsByTagName("Y").item(0).getChildNodes().item(0).getNodeValue().trim())));
                            break;
                        case "RemoveRoom":
                            int index = Integer.parseInt(room.getElementsByTagName("id").item(0).getChildNodes().item(0).getNodeValue().trim());
                            topLeft = (Element) room.getElementsByTagName("TopLeft").item(0);
                            bottomRight = (Element) room.getElementsByTagName("BottomRight").item(0);
                            dungeon.removeFromRoom(index,
                                    new Point(Integer.parseInt(topLeft.getElementsByTagName("X").item(0).getChildNodes().item(0).getNodeValue().trim()),
                                            Integer.parseInt(topLeft.getElementsByTagName("Y").item(0).getChildNodes().item(0).getNodeValue().trim())),
                                    new Point(Integer.parseInt(bottomRight.getElementsByTagName("X").item(0).getChildNodes().item(0).getNodeValue().trim()),
                                            Integer.parseInt(bottomRight.getElementsByTagName("Y").item(0).getChildNodes().item(0).getNodeValue().trim())));
                            break;
                        case "RoomAdd":
                            index = Integer.parseInt(room.getElementsByTagName("id").item(0).getChildNodes().item(0).getNodeValue().trim());
                            topLeft = (Element) room.getElementsByTagName("TopLeft").item(0);
                            bottomRight = (Element) room.getElementsByTagName("BottomRight").item(0);
                            dungeon.addToRoom(index,
                                    new Point(Integer.parseInt(topLeft.getElementsByTagName("X").item(0).getChildNodes().item(0).getNodeValue().trim()),
                                            Integer.parseInt(topLeft.getElementsByTagName("Y").item(0).getChildNodes().item(0).getNodeValue().trim())),
                                    new Point(Integer.parseInt(bottomRight.getElementsByTagName("X").item(0).getChildNodes().item(0).getNodeValue().trim()),
                                            Integer.parseInt(bottomRight.getElementsByTagName("Y").item(0).getChildNodes().item(0).getNodeValue().trim())));
                            break;
                    }
                }
            }
        }

    }

    public void addEntities() throws Exception {
        Element dungeonMap = doc.getDocumentElement();
        Element entities = (Element) dungeonMap.getElementsByTagName("Entities").item(0);
        NodeList entitiesList = entities.getChildNodes();
        if(entities.getElementsByTagName("Hero").getLength() != 1)
            throw new Exception("O mapa necessita ter um único herói");
        Element hero = (Element) entities.getElementsByTagName("Hero").item(0);
        Dungeon.getInstance().addEntity(Character.getDefaultHero("Player"),
                new Point(Integer.parseInt(hero.getElementsByTagName("X").item(0).getChildNodes().item(0).getNodeValue().trim()),
                          Integer.parseInt(hero.getElementsByTagName("Y").item(0).getChildNodes().item(0).getNodeValue().trim())));
        for(int i = 0; i < entitiesList.getLength(); i++) {
            Node node = entitiesList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element entity = (Element) entitiesList.item(i);

                Point position = new Point(Integer.parseInt(entity.getElementsByTagName("X").item(0).getChildNodes().item(0).getNodeValue().trim()),
                                               Integer.parseInt(entity.getElementsByTagName("Y").item(0).getChildNodes().item(0).getNodeValue().trim()));
                Entity newEntity = null;
                switch (entity.getNodeName()) {
                    case "Goblin":
                        newEntity = Character.getGoblin("Goblin");
                        break;
                    case "Skeleton":
                        newEntity = Character.getMeleeSkeleton("Skeleton");
                        break;
                    case "MageSkeleton":
                        newEntity = Character.getSkeletonMage("Mage Skeleton");
                        break;
                    case "Door":
                        newEntity = new Door();
                        break;
                    case "Treasure":
                        newEntity = Treasure.randomTreasure();
                        break;
                }
                    if(newEntity != null)
                        dungeon.addEntity(newEntity, position);
            }
        }
    }
}
