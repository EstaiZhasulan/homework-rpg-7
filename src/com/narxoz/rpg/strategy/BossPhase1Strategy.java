package com.narxoz.rpg.strategy;

public class BossPhase1Strategy implements CombatStrategy {

    @Override
    public int calculateDamage(int basePower) {
        return basePower;
    }

    @Override
    public int calculateDefense(int baseDefense) {
        return baseDefense;
    }

    @Override
    public String getName() {
        return "Boss Phase 1 (Measured)";
    }
}
