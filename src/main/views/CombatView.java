package main.views;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import main.Driver;
import main.attacks.Attack;
import main.character.Character;
import main.character.Player;
import main.character.NPC;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import static javafx.scene.layout.GridPane.setHalignment;
import static javafx.scene.layout.GridPane.setValignment;

public class CombatView{
    private Player player;
    private NPC npc;
    
    private GridPane combatRoot, playerSection, attackMenuGrid;
    private Scene combatScene;
    private Button commandAttack, commandItems, commandFlee, commandBack;
    private HBox playerCommands;
    private AnchorPane NPCSection, playerHealth, attackMenu, itemsMenu, endPane;
    private ImageView NPCImageView, combatField, commandAttackView, commandItemsView, commandFleeView, commandBackView,
            menuTextBox, attackGraphic, endTextBox;
    private Rectangle NPCHealthBorder, NPCHealthBar, playerHealthBorder, playerHealthBar;
    private Text NPCNameText, NPCHealthText, souljaBoyText, playerHealthText, NPCDialogueText, endText;

    private ArrayList<Button> attackButtons;

    private int NPCAttackIndex = 0;

    /**
     * Callback for when the combat is finished
     *
     * @param victory whether the player won or not.
     */
    private Callback onFinish;

    /**
     * Sets up a scene representing combat containing the player and the NPC they are fighting.
     *
     * @param npc The NPC being fought.
     * @param onFinish the callback for when combat ends.
     */
    public CombatView(NPC npc, Callback onFinish) {
        this.player = Driver.getPlayer();
        this.npc = npc;
        this.onFinish = onFinish;
        loadAttacks();
        setUp();
    }

    //%// Loader Methods

    /**
     * Turns the player's attacks into buttons and loads them in the attackButtons array list.
     * Each button upon press performs an "attack" in the combat view.
     */
    private void loadAttacks() {
        attackButtons = new ArrayList<>();
        Attack currAttack;

        System.out.println(player.getAttacks().size());
        for (int i = 0; i < 4 && i < player.getAttacks().size(); i++) {
            currAttack = player.getAttacks().get(i);
            attackButtons.add(i, new Button(currAttack.getName()));
            attackButtonEvent(attackButtons.get(i), currAttack);
            attackButtons.get(i).setId("attack_button");
        }
    }

    /**
     * Sets up the combat GUI.
     */
    private void setUp()  {
        combatRoot = new GridPane();
        combatScene = new Scene(combatRoot, 1280, 720, Color.BLACK);
        combatScene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource(
                        "../../resources/stylesheets/combat_view.css"
                )).toExternalForm()
        );

        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();
        ColumnConstraints col1 = new ColumnConstraints();
        
        row1.setPercentHeight(50.0);
        row2.setPercentHeight(50.0);
        col1.setPercentWidth(100.0);

        combatRoot.getRowConstraints().addAll(row1, row2);
        combatRoot.getColumnConstraints().add(col1);
        combatRoot.setAlignment(Pos.CENTER);

        combatRoot.setBackground(new Background(new BackgroundFill(
                Color.valueOf("#000000"),
                new CornerRadii(0),
                new Insets(0)
        )));

        //*// Row 1: "NPC Section"

        NPCSection = new AnchorPane();
        NPCImageView = new ImageView(new Image(npc.getImagePath()));
        combatField = new ImageView(new Image("./resources/sprites/combat/combat_field.png"));
        NPCHealthBorder = new Rectangle(120, 20);
        NPCHealthBar = new Rectangle(108 * healthRatio(npc), 8, colorHealth(npc));
        NPCNameText = new Text(npc.getName());
        NPCHealthText = new Text(npc.getCurrentHP() + "/" + npc.getMaxHP());
        attackGraphic = new ImageView(new Image("/resources/sprites/combat/slash/slash6.png"));
        NPCDialogueText = new Text("");

        NPCSection.getChildren().addAll(
                combatField,
                NPCImageView,
                NPCHealthBorder,
                NPCHealthBar,
                NPCNameText,
                NPCHealthText,
                attackGraphic,
                NPCDialogueText
        );

        NPCImageView.setX((1280.0 / 2) - NPCImageView.getImage().getWidth() / 2);
        NPCImageView.setY((720.0 / 4) - NPCImageView.getImage().getHeight() / 2);

        attackGraphic.setX((1280.0 / 2) - attackGraphic.getImage().getWidth() / 2);
        attackGraphic.setY((720.0 / 4) - attackGraphic.getImage().getHeight() / 2);

        combatField.setX((1280.0 / 2) - combatField.getImage().getWidth() / 2);
        combatField.setY((720.0 * (2.0/5)) - combatField.getImage().getHeight() / 2);

        NPCHealthBorder.setStrokeWidth(4.0);
        NPCHealthBorder.setStroke(Color.WHITE);
        NPCHealthBorder.setX((1280.0 / 2) + NPCImageView.getImage().getWidth() / 2);
        NPCHealthBorder.setY((720.0 / 4) - 100);

        NPCHealthBar.setX(NPCHealthBorder.getX() + 6);
        NPCHealthBar.setY(NPCHealthBorder.getY() + 6);

        NPCNameText.setId("npc_name");
        NPCNameText.setFill(Color.WHITE);
        NPCNameText.setX(NPCHealthBorder.getX());
        NPCNameText.setY(NPCHealthBorder.getY() - 6);

        NPCHealthText.setId("npc_health");
        NPCHealthText.setFill(Color.WHITE);
        NPCHealthText.setX(NPCHealthBorder.getX() + NPCHealthBorder.getWidth() - 8 * NPCHealthText.getText().length());
        NPCHealthText.setY(NPCHealthBorder.getY() + NPCHealthBorder.getHeight() + 16);

        NPCDialogueText.setId("npc_dialogue");
        NPCDialogueText.setFill(Color.LIME);
        NPCDialogueText.setWrappingWidth(400);
        NPCDialogueText.setX(-420 + NPCImageView.getX());
        NPCDialogueText.setY(150);
        NPCDialogueText.setTextAlignment(TextAlignment.CENTER);

        combatRoot.add(NPCSection, 0, 0);

        //*// Row 2.1: "Player Section"

        playerSection = new GridPane();

        RowConstraints pRow1 = new RowConstraints();
        pRow1.setPercentHeight(50.0);

        RowConstraints pRow2 = new RowConstraints();
        pRow2.setPercentHeight(50.0);

        playerSection.getRowConstraints().addAll(pRow1, pRow2);

        //* HBox 1: "Player Commands"

        playerCommands = new HBox();
        commandAttack = new Button();
        commandAttackView = new ImageView(new Image(menuPNGPath("butt_attack")));
        commandItems = new Button();
        commandItemsView = new ImageView(new Image(menuPNGPath("butt_items")));
        commandFlee = new Button();
        commandFleeView = new ImageView(new Image(menuPNGPath("butt_flee")));

        playerCommands.setId("player_commands");
        playerCommands.setSpacing(40);
        playerCommands.setAlignment(Pos.CENTER);

        commandAttack.setId("command_attack");
        commandAttack.setGraphic(commandAttackView);
        commandAttackEvent();

        commandItems.setId("command_items");
        commandItems.setGraphic(commandItemsView);
        //TODO: add event for commandItems

        commandFlee.setId("command_flee");
        commandFlee.setGraphic(commandFleeView);
        //TODO: add event for commandFlee

        playerCommands.getChildren().addAll(commandAttack, commandItems, commandFlee);

        //* HBox 2: "Player Health"

        playerHealth = new AnchorPane();

        playerHealthBorder = new Rectangle(480,60);
        playerHealthBar = new Rectangle(464 * healthRatio(player), 44, colorHealth(player));
        souljaBoyText = new Text("YOU");
        playerHealthText = new Text(player.getCurrentHP() + "/" + player.getMaxHP());

        playerHealthBorder.setStroke(Color.WHITE);
        playerHealthBorder.setStrokeWidth(4);
        playerHealthBorder.setX((1280.0 / 2) - playerHealthBorder.getWidth() - 60);

        playerHealthBar.setX(playerHealthBorder.getX() + 8);
        playerHealthBar.setY(playerHealthBorder.getY() + 8);

        souljaBoyText.setId("soulja_boy");
        souljaBoyText.setFill(Color.WHITE);
        souljaBoyText.setX(playerHealthBorder.getX() - 90);
        souljaBoyText.setY(playerHealthBorder.getY() + playerHealthBorder.getHeight()/2 + 10);

        playerHealthText.setId("player_health");
        playerHealthText.setFill(Color.WHITE);
        playerHealthText.setX(playerHealthBorder.getX() + playerHealthBorder.getWidth() + 40);
        playerHealthText.setY(playerHealthBorder.getY() + playerHealthBorder.getHeight()/2 + 10);

        playerHealth.getChildren().addAll(playerHealthBorder, playerHealthBar, souljaBoyText, playerHealthText);

        //*// Row 2-ish: "Text Boxes"

        menuTextBox = new ImageView(new Image(menuPNGPath("combat_textbox")));
        endTextBox = new ImageView(new Image(menuPNGPath("combat_textbox")));

        menuTextBox.setX(1280.0 / 2 - menuTextBox.getImage().getWidth() / 2);
        menuTextBox.setY(720.0 / 4 - menuTextBox.getImage().getHeight());

        endTextBox.setX(menuTextBox.getX());
        endTextBox.setY(menuTextBox.getY());

        commandBack = new Button();
        commandBackView = new ImageView(new Image(menuPNGPath("back_arrow")));

        commandBack.setId("command_back");
        commandBack.setGraphic(commandBackView);
        commandBackEvent();

        commandBack.setLayoutX(1280.0 / 2 - commandBackView.getImage().getWidth() / 2);
        commandBack.setLayoutY(180);

        //*// Row 2.2: "Attack Menu"

        attackMenu = new AnchorPane();
        attackMenuGrid = new GridPane();

        RowConstraints attackMenuGridRow1 = new RowConstraints(80);
        RowConstraints attackMenuGridRow2 = new RowConstraints(80);

        ColumnConstraints attackCol1 = new ColumnConstraints(340);
        ColumnConstraints attackCol2 = new ColumnConstraints(300);
        ColumnConstraints attackCol3 = new ColumnConstraints(300);
        ColumnConstraints attackCol4 = new ColumnConstraints(340);

        attackMenuGrid.setAlignment(Pos.CENTER);
        setValignment(attackMenuGrid, VPos.CENTER);
        setHalignment(attackMenuGrid, HPos.CENTER);

        attackMenuGrid.getRowConstraints().addAll(attackMenuGridRow1, attackMenuGridRow2);
        attackMenuGrid.getColumnConstraints().addAll(attackCol1, attackCol2,attackCol3, attackCol4);


        attackMenu.getChildren().addAll(menuTextBox, attackMenuGrid, commandBack);

        for (int i = 0, j = 1, k = 0; i < attackButtons.size() && i < 4; i++) {
            attackMenuGrid.add(attackButtons.get(i), j, k);

            k++;
            if (k >= 2) {
                j++;
                k = 0;
            }
        }

        for (Node child : attackMenuGrid.getChildren()) {
            setValignment(child, VPos.BOTTOM);
            setHalignment(child, HPos.CENTER);
        }

        //*// Row 2.3: "End Box"

        endPane = new AnchorPane();
        endText = new Text("You've perished!");

        endText.setFill(Color.WHITE);
        endText.setId("end_text");
        endText.setX(1280.0/2 - 300 + 20);
        endText.setY(80);

        endPane.getChildren().addAll(endTextBox, endText);

        //*// Draw

        playerSection.add(playerHealth, 0, 1);
        playerSection.add(playerCommands, 0, 0);
        playerSection.setAlignment(Pos.CENTER);
        combatRoot.add(playerSection, 0, 1);

    }

    /**
     * Performs the NPC's turn. That is:
     * 1. The NPC takes damage and wobbles.
     * 2. An attack dialogue is drawn.
     * 3. The player takes damage from an NPC attack and "wobbles."
     * Also, checks whether the player or the NPC has perished -> if so, end combat.
     */
    private void performNPCTurn(){
        PauseTransition pause1 = new PauseTransition(Duration.seconds(0.7));
        PauseTransition pause2 = new PauseTransition(Duration.seconds(1.5));
        PauseTransition pause3 = new PauseTransition(Duration.seconds(1.5));

        pause1.setOnFinished(e -> {
            updateNPCHealth();
            wobbleNPC();
            endCombat();
            if (npc.isAlive()) {
                pause2.play();
            }
        });

        pause2.setOnFinished(e -> {
            drawNPCDialogue();
            pause3.play();
        });

        pause3.setOnFinished(e -> {
            if (NPCAttackIndex == npc.getAttacks().size()) {
                NPCAttackIndex = 0;
            }

            npc.getAttacks().get(NPCAttackIndex).dealDamage(npc, player);

            NPCAttackIndex++;

            updatePlayerHealth();

            aliveCheck();
            endCombat();

            NPCDialogueText.setText("");
            wobblePlayer();

            if (player.isAlive()) {
                restorePlayerSection();
            }
        });

        pause1.play();

    }

    //%// Update Methods

    /**
     * Updates the NPC's health bar to match their HP and maximum HP value.
     */
    public void updateNPCHealth() {
        NPCHealthBar.setFill(colorHealth(npc));
        NPCHealthBar.setWidth(108 * healthRatio(npc));
        NPCHealthText.setText(npc.getCurrentHP() + "/" + npc.getMaxHP());
    }

    /**
     * Updates the Player's health bar to match their HP and maximum HP value.
     */
    public void updatePlayerHealth() {
        playerHealthBar.setFill(colorHealth(player));
        playerHealthBar.setWidth(464 * healthRatio(player));
        playerHealthText.setText(player.getCurrentHP() + "/" + player.getMaxHP());
    }

    /**
     * Restores the playerSection grid-pane.
     */
    public void restorePlayerSection() {
        combatRoot.add(playerSection, 0, 1);
    }

    /**
     * Clears the playerSection grid-pane to make room for a menu.
     */
    public void clearPlayerSection() {
        gridPaneRemove(combatRoot, 1, 0);
    }

    /**
     * Clears the NPCSection Anchor-pane.
     */
    public void clearNPCSection() {
        gridPaneRemove(combatRoot, 0, 0);
    }

    /**
     * Displays the attacks that the player can perform.
     */
    public void displayAttacks() {
        clearPlayerSection();
        combatRoot.add(attackMenu, 0, 1);
    }

    /**
     * Displays the end of the combat.
     */
    public void displayEnd() {
        clearPlayerSection();
        combatRoot.add(endPane, 0, 1);
    }

    /**
     * Draws an attack animation on top of the NPC.
     */
    public void drawAttackGraphic() {
        String path = "/resources/sprites/combat/slash/slash";

        IntegerProperty i = new SimpleIntegerProperty(1);
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(0.1),
                e -> {
                    attackGraphic.setImage(new Image(path + i.get() + ".png"));
                    i.set(i.get() + 1);
                }
            )
        );
        timeline.setCycleCount(6);
        timeline.play();

    }

    /**
     * Shakes the NPC's sprite side to side.
     */
    public void wobbleNPC() {
        NPCImageView.setX(NPCImageView.getX() + 5);
        IntegerProperty i = new SimpleIntegerProperty(-1);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.05),
                        e -> {
                            NPCImageView.setX(NPCImageView.getX() + i.get() * 10);
                            i.set(i.get() * -1);
                        }
                )
        );
        timeline.setCycleCount(6);
        timeline.play();
        NPCImageView.setX(NPCImageView.getX() - 5);
    }

    /**
     * Chooses a random message from the NPC's possible attack dialogue array list and draws it.
     */
    public void drawNPCDialogue() {
        Random random = new Random();
        if (!npc.getAttackDialogue().isEmpty()) {
            int i = random.nextInt(npc.getAttackDialogue().size());
            NPCDialogueText.setText(npc.getAttackDialogue().get(i));
        }
    }

    /**
     * Shakes the entire GUI side to side to represent the player being attacked by the NPC.
     */
    public void wobblePlayer() {
        combatRoot.setLayoutX(combatRoot.getLayoutX() + 5);
        IntegerProperty i = new SimpleIntegerProperty(-1);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.1),
                        e -> {
                            combatRoot.setLayoutX(combatRoot.getLayoutX() + i.get() * 10);
                            i.set(i.get() * -1);
                        }
                )
        );
        timeline.setCycleCount(4);
        timeline.play();
        combatRoot.setLayoutX(combatRoot.getLayoutX() - 5);
    }

    /**
     * Concludes combat and displays a final message depending on the combat outcome,
     *
     * @return whether the player won or lost.
     */
    public void endCombat() {
        if (!player.isAlive()) {
            clearNPCSection();
            endText.setText("You've perished!");
            displayEnd();
            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(e -> onFinish.call(false));
            pause.play();
        } else if (!npc.isAlive()){
            PauseTransition pause1 = new PauseTransition(Duration.seconds(0.8));
            PauseTransition pause2 = new PauseTransition(Duration.seconds(1));

            pause1.setOnFinished(e -> {
                NPCImageView.setOpacity(0.5);
                NPCHealthBorder.setOpacity(0);
                NPCHealthBar.setOpacity(0);
                NPCNameText.setOpacity(0);
                NPCHealthText.setOpacity(0);
            });

            pause2.setOnFinished(e -> {
                endText.setText("You've won!");
                displayEnd();
                PauseTransition pause = new PauseTransition(Duration.seconds(3));
                pause.setOnFinished(_e -> onFinish.call(true));
                pause.play();
            });

            pause1.play();
            pause2.play();

        }
    }

    /**
     * Checks whether not if the player's or the NPC's HP has dropped to zero or lower. If so, pronounce them dead.
     */
    public void aliveCheck() {
        if (player.getCurrentHP() <= 0) {
            player.pronounceDead();
        }
        if (npc.getCurrentHP() <= 0) {
            npc.pronounceDead();
        }
    }

    //%// Event Setters

    /**
     * Adds an event to a button so that it performs an attack and then switches to the NPC's turn.
     *
     * @param button The button that the event is being added to.
     * @param attack The attack that will be performed.
     */
    public void attackButtonEvent(Button button, Attack attack) {
        button.setOnAction(e -> {
            attack.dealDamage(player, npc);
            aliveCheck();
            clearPlayerSection();
            drawAttackGraphic();
            performNPCTurn();
        });
    }

    /**
     * The "commandBack" button takes the player back to the command list from the attack menu with this event.
     */
    public void commandBackEvent() {
        commandBack.setOnAction(e -> {
            clearPlayerSection();
            restorePlayerSection();
        });
    }

    /**
     * The "commandAttack" button pulls up the attack menu with this event.
     */
    public void commandAttackEvent() {
        commandAttack.setOnAction(e -> displayAttacks());
    }

    //%// Utility Methods

    /**
     * Restores the scene stored in this class.
     */
    public Scene returnScene() {
        return combatScene;
    }

    /**
     * Removes all the elements at a certain row and column in a grid-pane.
     *
     * @param gridPane The grid-pane that elements are being removed from.
     * @param row The row to remove at.
     * @param col The column to remove at.
     */
    public void gridPaneRemove(GridPane gridPane, int row, int col) {
        gridPane.getChildren().removeIf(
                node -> GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col
        );
    }

    /**
     * Checks a character's HP and maximum HP values and returns a Color class depending on the ratio between the two
     * values.
     *
     * > 0.3: returns WHITE.
     * <= 0.3: returns RED (representing a 'critical' state of HP).
     *
     * @param character The character whose HP and maximum HP values are being checked.
     * @return The correct color.
     */
    private Color colorHealth(Character character) {
        return (healthRatio(character) <= 0.3) ? Color.RED : Color.WHITE;
    }

    /**
     * Checks a character's HP and maximum HP values and returns the ratio between the two values.
     *
     * @param character The character whose HP and maximum HP values are being checked.
     * @return The ratio between HP and maximum HP.
     */
    private double healthRatio(Character character) {
        return (double) character.getCurrentHP() / character.getMaxHP();
    }

    /**
     * Returns the file path for a .png file that is to be used in the combat view as a menu interactable.
     *
     * @param file The name of the .png file.
     * @return The .png's filepath.
     */
    public String menuPNGPath(String file) {
        return "./resources/sprites/combat/menu/" + file + ".png";
    }
}
