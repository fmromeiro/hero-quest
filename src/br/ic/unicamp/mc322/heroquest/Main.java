package br.ic.unicamp.mc322.heroquest;

import br.ic.unicamp.mc322.heroquest.controller.HeroQuest;
import br.ic.unicamp.mc322.heroquest.controller.TerminalRenderer;

public class Main {

    public static void main(String[] args) throws Exception {
        HeroQuest hq = new HeroQuest(new TerminalRenderer());
        hq.runGame();
    }
}
