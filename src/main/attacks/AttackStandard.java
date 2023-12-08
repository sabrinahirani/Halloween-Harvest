package main.attacks;

import main.GameType;
import main.character.Character;

public class AttackStandard extends Attack {
    double flatModifier = 0.0;
    double mulModifier = 1.0;

    public AttackStandard(
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
        checkDefense(target);
        checkType(target);

        target.damageHP(calcDamage());

        clearModifiers();
    }

    @Override
    public int calcDamage(){
        double beforeFlat = (atkValue * mulModifier);
        if (beforeFlat > 0 && (beforeFlat + flatModifier) <= 0) {
            return 1;
        }
        return (int) (beforeFlat + flatModifier);
    }

    private void clearModifiers(){
        flatModifier = 0;
        mulModifier = 1.0;
    }

    private void checkDefense(Character target) {
        flatModifier -= target.getDef();
    }

    private void checkType(Character target) {
        int check = getType().compareTo(target.getType().check());

        if (check == 1) {
            mulModifier *= 1.25;
        } else if (check == -1) {
            mulModifier *= 0.75;
        }
    }

    private GameType getType() {
        return type;
    }

}
