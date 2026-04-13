package com.narxoz.rpg;

import com.narxoz.rpg.combatant.DungeonBoss;
import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.observer.EventPublisher;
import com.narxoz.rpg.strategy.AggressiveStrategy;
import com.narxoz.rpg.strategy.BalancedStrategy;
import com.narxoz.rpg.strategy.DefensiveStrategy;

import java.util.Arrays;
import java.util.List;


public class Main {

    public static void main(String[] args) {

        Hero erlan  = new Hero("Erlan",  120, 28, 10, new AggressiveStrategy());
        Hero aisha  = new Hero("Aisha",  150, 18, 18, new DefensiveStrategy());
        Hero timur  = new Hero("Timur",  100, 22, 12, new BalancedStrategy());

        List<Hero> party = Arrays.asList(erlan, aisha, timur);

        System.out.println("=== Party assembled ===");
        for (Hero h : party) {
            System.out.printf("  %-8s | HP: %d | ATK: %d | DEF: %d | Strategy: %s%n",
                    h.getName(), h.getMaxHp(), h.getAttackPower(),
                    h.getDefense(), h.getStrategy().getName());
        }
        EventPublisher publisher = new EventPublisher();
        DungeonBoss boss = new DungeonBoss("Shadow Tyrant", 300, 30, 8, publisher);
        publisher.subscribe(boss);

        System.out.println("\n=== Boss ===");
        System.out.printf("  %-14s | HP: %d | ATK: %d | DEF: %d | Strategy: %s%n",
                boss.getName(), boss.getMaxHp(), boss.getEffectiveDamage(),
                boss.getEffectiveDefense(), boss.getStrategy().getName());

        // TODO (student): Create a DungeonBoss with meaningful stats
        // TODO (student): Instantiate and register all 5 observers
        // TODO (student): Create a DungeonEngine and run the encounter
        // TODO (student): Print the EncounterResult at the end
    }
}
