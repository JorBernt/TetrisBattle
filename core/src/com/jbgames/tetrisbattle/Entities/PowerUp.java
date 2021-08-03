package com.jbgames.tetrisbattle.Entities;


import com.jbgames.tetrisbattle.Controllers.Player;

public class PowerUp {

    public enum Item {
        NONE("NONE"),
        INSTANT_FALL("Instant\nFall");

        String name;
        Item(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static void useItem(Player attacker, Player target, Item item) {
        switch (item) {
            case INSTANT_FALL:
                target.attacked(item);
                break;
        }
    }

}
