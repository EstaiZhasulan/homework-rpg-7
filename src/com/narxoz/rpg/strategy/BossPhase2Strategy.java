package com.narxoz.rpg.strategy;

public class BossPhase2Strategy implements CombatStrategy {

    @Override
    public int calculateDamage(int basePower) {
        return (int)(basePower * 1.4);
    }

    @Override
    public int calculateDefense(int baseDefense) {
        return (int)(baseDefense * 0.7);
    }

    @Override
    public String getName() {
        return "Boss Phase 2 (Aggressive)";
    }
}
