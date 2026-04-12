package com.narxoz.rpg.observer;

import com.narxoz.rpg.combatant.Hero;

import java.util.List;
import java.util.Random;

public class PartySupport implements GameObserver {

    private final List<Hero> party;
    private final int healAmount;
    private final Random random = new Random(77L);

    public PartySupport(List<Hero> party, int healAmount) {
        this.party = party;
        this.healAmount = healAmount;
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event.getType() != GameEventType.HERO_LOW_HP) return;

        List<Hero> living = party.stream()
                .filter(Hero::isAlive)
                .collect(java.util.stream.Collectors.toList());

        if (living.isEmpty()) return;

        Hero target = living.get(random.nextInt(living.size()));
        target.heal(healAmount);
        System.out.println("[PartySupport] Emergency healing! " + target.getName() +
                " restored " + healAmount + " HP. (HP: " + target.getHp() + "/" + target.getMaxHp() + ")");
    }
}
