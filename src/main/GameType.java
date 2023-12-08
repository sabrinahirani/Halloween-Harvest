package main;

public class GameType {

    private String value;

    public GameType (String value) {
        this.value = value;
    }

    /**
     * Getter method to get the value or "type" of the GameType;
     *
     * @return the value of the GameType;
     */
    public String check() {
        return value;
    }

    /**
     * Takes another value (from another main.GameType) and compares it to the
     * value in the current main.GameType. Returns either -1, 0, 1.
     * -1 denotes "not effective".
     * 0 denotes "neither effective nor not effective".
     * 1 denotes "effective".
     *
     * @param valueOther the other value being compared to the current one.
     * @return an int denoting type effectiveness.
     */
    public int compareTo(String valueOther) {
        return switch (value) {
            case "FIEND" -> switch (valueOther) {
                case "BEAST" -> 1;
                case "UNDEAD" -> -1;
                default -> 0;
            };
            case "BEAST" -> switch (valueOther) {
                case "FIEND" -> -1;
                case "UNDEAD" -> 1;
                default -> 0;
            };
            case "UNDEAD" -> switch (valueOther) {
                case "FIEND" -> 1;
                case "BEAST" -> -1;
                default -> 0;
            };
            default -> 0;
        };
    }

    public String getType(){
        return this.value;
    }


}
