package main.items;

public interface Item {
    /**
     * Get item description
     *
     * @return String 
     */
    public String getDescription();

    /**
     * Get item name
     */
    public String getName();

    /**
     * Get file path to the item icon.
     */
    public String getIcon();

    /**
     * Get the "price" of the item, in terms of candies.
     */
    public int getPrice();

    /**
     * Check whether the item perishes after use or not.
     */
    public boolean getPerishable();

    /**
     * Apply the effects of the item.
     */
    public void apply();
}
