package com.narxoz.rpg.observer;


public class BattleLogger implements GameObserver {

    @Override
    public void onEvent(GameEvent event) {
        String msg;
        switch (event.getType()) {
            case ATTACK_LANDED:
                msg = event.getSourceName() + " lands an attack for " + event.getValue() + " damage.";
                break;
            case HERO_LOW_HP:
                msg = "CRITICAL: " + event.getSourceName() + " is low on HP! (" + event.getValue() + " HP remaining)";
                break;
            case HERO_DIED:
                msg = event.getSourceName() + " has fallen in battle!";
                break;
            case BOSS_PHASE_CHANGED:
                msg = "*** BOSS enters Phase " + event.getValue() + "! ***";
                break;
            case BOSS_DEFEATED:
                msg = "*** THE BOSS HAS BEEN DEFEATED! Victory! ***";
                break;
            default:
                msg = "[Unknown event: " + event.getType() + "]";
        }
        System.out.println("[LOG] " + msg);
    }
}
