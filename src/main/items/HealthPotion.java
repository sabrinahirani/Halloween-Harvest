package main.items;

import main.Driver;

public class HealthPotion implements Item {
    @Override
    public String getDescription() {
        return "Gain 10 health points by consuming this bitter potion.";
    }

    @Override
    public String getName() {
        return "Health Potion";
    }

    @Override
    public String getIcon() {
        return "/resources/sprites/items/health-potion.png";
    }

    @Override
    public int getPrice() {
        return 10;
    }

    @Override
    public void apply() {
        Driver.getPlayer().addHp(10);
    }

    @Override
    public boolean getPerishable() {
        return true;
    }
}
