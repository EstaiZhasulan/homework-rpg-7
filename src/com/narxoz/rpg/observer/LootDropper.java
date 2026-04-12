package com.narxoz.rpg.observer;

import java.util.Random;

public class LootDropper implements GameObserver {

    private static final String[][] PHASE_LOOT = {
            {},
            {"Enchanted Shield", "Shadow Cloak", "Iron Will Potion"},
            {"Cursed Blade", "Berserker Amulet", "Rage Tonic"},
            {"Crown of the Fallen", "Void Crystal", "Ultimate Power Orb", "Boss Soul Shard"}
    };

    private final Random random = new Random(13L);

    @Override
    public void onEvent(GameEvent event) {
        if (event.getType() == GameEventType.BOSS_PHASE_CHANGED) {
            int phase = event.getValue();
            dropLoot("Phase " + phase + " transition", PHASE_LOOT[phase]);

        } else if (event.getType() == GameEventType.BOSS_DEFEATED) {
            dropLoot("Boss defeated", PHASE_LOOT[3]);
        }
    }

    private void dropLoot(String occasion, String[] lootTable) {
        if (lootTable.length == 0) return;
        String item = lootTable[random.nextInt(lootTable.length)];
        System.out.println("[LootDropper] >>> LOOT DROP (" + occasion + "): [ " + item + " ] <<<");
    }
}
