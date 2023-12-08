package main.attacks;

import main.GameType;
import main.character.Character;

public class AttackHeal extends Attack {
    double flatModifier = 0.0;
    double mulModifier = 1.0;

    public AttackHeal(
            int id,
            String name,
            String type,
            String description,
            double atkValue
    ) {
        super(id, name, type, description, atkValue);
    }

    @Override
    public void dealDamage(Character self, Character target) {
        target.damageHP(calcDamage());

        clearModifiers();
    }

    @Override
    public int calcDamage(){
        if (atkValue >= 0) {
            return 1;
        }
        return (int) atkValue;
    }

    private void clearModifiers(){
        flatModifier = 0;
        mulModifier = 1.0;
    }

}
