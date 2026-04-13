package com.narxoz.rpg;

import com.narxoz.rpg.combatant.DungeonBoss;
import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.engine.DungeonEngine;
import com.narxoz.rpg.engine.EncounterResult;
import com.narxoz.rpg.observer.*;
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
        BattleLogger battleLogger      = new BattleLogger();
        AchievementTracker achievementTracker = new AchievementTracker();
        PartySupport partySupport      = new PartySupport(party, 25);
        HeroStatusMonitor heroStatusMonitor = new HeroStatusMonitor(party);
        LootDropper       lootDropper       = new LootDropper();

        publisher.subscribe(battleLogger);
        publisher.subscribe(achievementTracker);
        publisher.subscribe(partySupport);
        publisher.subscribe(heroStatusMonitor);
        publisher.subscribe(lootDropper);

        System.out.println("\n=== Observers registered ===");
        System.out.println("  1. BattleLogger");
        System.out.println("  2. AchievementTracker");
        System.out.println("  3. PartySupport");
        System.out.println("  4. HeroStatusMonitor");
        System.out.println("  5. LootDropper");

        DungeonEngine engine = new DungeonEngine(party, boss, publisher);
        EncounterResult result = engine.run();

        System.out.println("========================================");
        System.out.println("           ENCOUNTER RESULT             ");
        System.out.println("========================================");
        System.out.println("  Outcome        : " + (result.isHeroesWon() ? "HEROES WIN" : "BOSS WINS"));
        System.out.println("  Rounds played  : " + result.getRoundsPlayed());
        System.out.println("  Survivors      : " + result.getSurvivingHeroes() + "/" + party.size());
        System.out.println("========================================");
    }
}
