package com.narxoz.rpg.observer;

import java.util.HashSet;
import java.util.Set;

public class AchievementTracker implements GameObserver {

    private final Set<String> unlocked = new HashSet<>();
    private int totalDamageDealt = 0;
    private int heroDeathCount = 0;

    @Override
    public void onEvent(GameEvent event) {
        switch (event.getType()) {
            case ATTACK_LANDED:
                totalDamageDealt += event.getValue();
                if (totalDamageDealt >= 100 && unlock("Century of Pain")) {
                    System.out.println("[ACHIEVEMENT] 'Century of Pain' — Party dealt 100+ total damage!");
                }
                if (totalDamageDealt >= 300 && unlock("Unstoppable Force")) {
                    System.out.println("[ACHIEVEMENT] 'Unstoppable Force' — Party dealt 300+ total damage!");
                }
                break;

            case HERO_DIED:
                heroDeathCount++;
                if (heroDeathCount >= 1 && unlock("Blood Price")) {
                    System.out.println("[ACHIEVEMENT] 'Blood Price' — First hero has fallen.");
                }
                break;

            case BOSS_DEFEATED:
                if (heroDeathCount == 0 && unlock("Flawless Victory")) {
                    System.out.println("[ACHIEVEMENT] 'Flawless Victory' — Boss defeated with no hero deaths!");
                }
                if (unlock("Dungeon Cleared")) {
                    System.out.println("[ACHIEVEMENT] 'Dungeon Cleared' — The boss has been slain!");
                }
                break;

            default:
                break;
        }
    }

    private boolean unlock(String achievement) {
        return unlocked.add(achievement);
    }
}
