package com.narxoz.rpg.combatant;

import com.narxoz.rpg.observer.EventPublisher;
import com.narxoz.rpg.observer.GameEvent;
import com.narxoz.rpg.observer.GameEventType;
import com.narxoz.rpg.observer.GameObserver;
import com.narxoz.rpg.strategy.BossPhase1Strategy;
import com.narxoz.rpg.strategy.BossPhase2Strategy;
import com.narxoz.rpg.strategy.BossPhase3Strategy;
import com.narxoz.rpg.strategy.CombatStrategy;


public class DungeonBoss implements GameObserver {

    private final String name;
    private int hp;
    private final int maxHp;
    private final int attackPower;
    private final int defense;
    private int currentPhase;
    private CombatStrategy strategy;
    private final EventPublisher publisher;

    public DungeonBoss(String name, int hp, int attackPower, int defense, EventPublisher publisher) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.attackPower = attackPower;
        this.defense = defense;
        this.currentPhase = 1;
        this.strategy = new BossPhase1Strategy();
        this.publisher = publisher;
    }

    public String getName()     { return name; }
    public int getHp()          { return hp; }
    public int getMaxHp()       { return maxHp; }
    public boolean isAlive()    { return hp > 0; }
    public int getCurrentPhase(){ return currentPhase; }
    public CombatStrategy getStrategy() { return strategy; }

    public int getEffectiveDamage() {
        return strategy.calculateDamage(attackPower);
    }

    public int getEffectiveDefense() {
        return strategy.calculateDefense(defense);
    }

    public void takeDamage(int amount) {
        hp = Math.max(0, hp - amount);

        if (hp == 0) {
            publisher.publish(new GameEvent(GameEventType.BOSS_DEFEATED, name, 0));
            return;
        }

        double hpPercent = (double) hp / maxHp;

        if (currentPhase == 1 && hpPercent < 0.60) {
            currentPhase = 2;
            publisher.publish(new GameEvent(GameEventType.BOSS_PHASE_CHANGED, name, 2));
        } else if (currentPhase == 2 && hpPercent < 0.30) {
            currentPhase = 3;
            publisher.publish(new GameEvent(GameEventType.BOSS_PHASE_CHANGED, name, 3));
        }
    }


    @Override
    public void onEvent(GameEvent event) {
        if (event.getType() == GameEventType.BOSS_PHASE_CHANGED
                && event.getSourceName().equals(name)) {
            int phase = event.getValue();
            switch (phase) {
                case 2:
                    strategy = new BossPhase2Strategy();
                    System.out.println("[Boss] " + name + " switches to " + strategy.getName() + "!");
                    break;
                case 3:
                    strategy = new BossPhase3Strategy();
                    System.out.println("[Boss] " + name + " switches to " + strategy.getName() + "!");
                    break;
                default:
                    strategy = new BossPhase1Strategy();
                    break;
            }
        }
    }
}
