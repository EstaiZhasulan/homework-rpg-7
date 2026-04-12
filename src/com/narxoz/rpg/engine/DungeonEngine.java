package com.narxoz.rpg.engine;

import com.narxoz.rpg.combatant.DungeonBoss;
import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.observer.EventPublisher;
import com.narxoz.rpg.observer.GameEvent;
import com.narxoz.rpg.observer.GameEventType;
import com.narxoz.rpg.strategy.AggressiveStrategy;

import java.util.List;


public class DungeonEngine {

    private static final int MAX_ROUNDS = 30;

    private final List<Hero> heroes;
    private final DungeonBoss boss;
    private final EventPublisher publisher;

    public DungeonEngine(List<Hero> heroes, DungeonBoss boss, EventPublisher publisher) {
        this.heroes = heroes;
        this.boss = boss;
        this.publisher = publisher;
    }

    public EncounterResult run() {
        System.out.println("\n========================================");
        System.out.println("   THE CURSED DUNGEON — BOSS ENCOUNTER  ");
        System.out.println("========================================");
        System.out.println("Heroes: " + heroes.size() + "  |  Boss: " + boss.getName() +
                " (HP: " + boss.getHp() + ")");
        System.out.println("========================================\n");

        int round = 0;

        while (boss.isAlive() && anyHeroAlive() && round < MAX_ROUNDS) {
            round++;
            System.out.println("\n--- Round " + round + " | Boss HP: " + boss.getHp() +
                    "/" + boss.getMaxHp() + " [Phase " + boss.getCurrentPhase() + ": " +
                    boss.getStrategy().getName() + "] ---");

            // --- Hero strategy switch demo: switch to Aggressive in round 3 ---
            if (round == 3) {
                Hero firstHero = heroes.get(0);
                firstHero.setStrategy(new AggressiveStrategy());
            }

            // === Heroes attack boss ===
            for (Hero hero : heroes) {
                if (!hero.isAlive()) continue;

                int rawDamage = hero.getEffectiveDamage();
                int bossDefense = boss.getEffectiveDefense();
                int netDamage = Math.max(1, rawDamage - bossDefense);

                System.out.println("[" + hero.getName() + " | " + hero.getStrategy().getName() +
                        "] attacks " + boss.getName() + " for " + netDamage +
                        " damage (raw=" + rawDamage + ", bossDefense=" + bossDefense + ")");

                publisher.publish(new GameEvent(GameEventType.ATTACK_LANDED, hero.getName(), netDamage));
                boss.takeDamage(netDamage);

                if (!boss.isAlive()) break;
            }

            if (!boss.isAlive()) break;

            // === Boss attacks all living heroes ===
            System.out.println("[" + boss.getName() + " | " + boss.getStrategy().getName() +
                    "] counter-attacks!");

            for (Hero hero : heroes) {
                if (!hero.isAlive()) continue;

                int rawDamage = boss.getEffectiveDamage();
                int heroDefense = hero.getEffectiveDefense();
                int netDamage = Math.max(1, rawDamage - heroDefense);

                hero.takeDamage(netDamage);
                publisher.publish(new GameEvent(GameEventType.ATTACK_LANDED, boss.getName(), netDamage));

                System.out.println("  -> " + hero.getName() + " takes " + netDamage +
                        " damage. HP: " + hero.getHp() + "/" + hero.getMaxHp());

                // Fire HERO_LOW_HP if below 30%
                if (hero.isAlive() && (double) hero.getHp() / hero.getMaxHp() < 0.30) {
                    publisher.publish(new GameEvent(GameEventType.HERO_LOW_HP, hero.getName(), hero.getHp()));
                }

                // Fire HERO_DIED if dead
                if (!hero.isAlive()) {
                    publisher.publish(new GameEvent(GameEventType.HERO_DIED, hero.getName(), 0));
                }
            }
        }

        // Compute result
        boolean heroesWon = !boss.isAlive();
        int survivors = (int) heroes.stream().filter(Hero::isAlive).count();

        System.out.println("\n========================================");
        System.out.println(heroesWon
                ? "VICTORY! The party defeated " + boss.getName() + "!"
                : "DEFEAT! The party was wiped out by " + boss.getName() + ".");
        System.out.println("========================================\n");

        return new EncounterResult(heroesWon, round, survivors);
    }

    private boolean anyHeroAlive() {
        return heroes.stream().anyMatch(Hero::isAlive);
    }
}
