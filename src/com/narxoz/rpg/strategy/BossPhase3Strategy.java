package com.narxoz.rpg.strategy;


public class BossPhase3Strategy implements CombatStrategy {

    @Override
    public int calculateDamage(int basePower) {
        return basePower * 2;
    }

    @Override
    public int calculateDefense(int baseDefense) {
        return 0;
    }

    @Override
    public String getName() {
        return "Boss Phase 3 (Desperate)";
    }
}
