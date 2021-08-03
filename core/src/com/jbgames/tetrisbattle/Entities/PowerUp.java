package com.jbgames.tetrisbattle.Entities;


import com.jbgames.tetrisbattle.Controllers.Player;

public class PowerUp {

    public enum Item {
        NONE("NONE"),
        INSTANT_FALL("Instant\nFall"),
        MIRROR("Mirror"),
        NO_ROTATION("No\nRotation"),
        SWAP("Swap");

        String name;
        Item(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static void useItem(Player attacker, Player target, Item item) {
        target.setPopUp("Opponent used " + item.getName() + "!");
        target.setShowPopUp(true);
        switch (item) {
            case INSTANT_FALL:
                target.placeBlockInstant();
                break;
            case MIRROR:
                target.setItemAffectGameScreen(true);
                target.activateItem(item, 3f);
                break;
            case SWAP:
                BlockTypes temp = attacker.getActiveBlock().getType();
                attacker.getActiveBlock().setNewBlock(target.getActiveBlock().getType());
                target.getActiveBlock().setNewBlock(temp);
                break;
            case NO_ROTATION:
                target.setItemAffectGameScreen(true);
                target.activateItem(item, 5f);
        }
    }




}
