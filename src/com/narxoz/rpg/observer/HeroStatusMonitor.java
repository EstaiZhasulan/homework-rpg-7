package com.narxoz.rpg.observer;

import com.narxoz.rpg.combatant.Hero;

import java.util.List;

public class HeroStatusMonitor implements GameObserver {

    private final List<Hero> party;

    public HeroStatusMonitor(List<Hero> party) {
        this.party = party;
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event.getType() != GameEventType.HERO_LOW_HP
                && event.getType() != GameEventType.HERO_DIED) return;

        System.out.println("[HeroStatus] === Party Status Update ===");
        for (Hero hero : party) {
            String status = hero.isAlive() ? "ALIVE" : "DEAD";
            double pct = (double) hero.getHp() / hero.getMaxHp() * 100;
            System.out.printf("[HeroStatus]   %-12s | HP: %3d/%-3d (%5.1f%%) | Strategy: %-12s | %s%n",
                    hero.getName(), hero.getHp(), hero.getMaxHp(), pct,
                    hero.getStrategy().getName(), status);
        }
        System.out.println("[HeroStatus] ==============================");
    }
}
