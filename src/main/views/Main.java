package main.views;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.*;
import main.items.Costume;
import main.items.Item;
import main.character.NPC;
import main.views.CombatView;

public class Main {
    @FXML
    protected VBox rootBox;

    @FXML
    protected TextArea textArea;

    @FXML
    protected Label nbhdLabel;

    @FXML
    protected Label costumeLabel;

    @FXML
    protected ImageView bgView, costumeImgView, npcImgView;

    @FXML
    protected VBox itemsList, inventoryList;

    @FXML
    protected Label killCountLabel, friendCountLabel, candyCountLabel, hpLabel;

    @FXML
    protected Button interactBtn, fightBtn, quitBtn, helpBtn, toggleThemeBtn;

    @FXML
    protected Button buyHealthPotionBtn, buyAttackPotionBtn, buyInvisibilityCloakBtn;

    @FXML
    public void initialize() {
        updateView();
    }

    @FXML
    public void toggleTheme() {
        String cssPath = "/resources/stylesheets/dark.css";
        if (rootBox.getStylesheets().isEmpty()) {
            rootBox.getStylesheets().add(cssPath);
        } else {
            rootBox.getStylesheets().remove(cssPath);
        }
    }

    @FXML
    public void keyListener(KeyEvent evt) throws IOException {
        Driver.getPlayer().updateLocation(evt.getCode());
        updateView();
    }

    @FXML
    protected void triggerInteraction() {
        // TODO: Unimplemented.
    }

    @FXML
    protected void triggerFight() throws IOException {
        NPC npc = Driver.getMap().get(Driver.getPlayer().getLocation()).getNPC();
        Scene gameOver = SceneUtil.loadFXMLScene("GameOver", true);
        gameOver.getStylesheets().add(
            Objects.requireNonNull(getClass().getResource(
            "../../resources/stylesheets/combat_view.css"
            )).toExternalForm()
        );
        Stage combatStage = new Stage();
        combatStage.initModality(Modality.APPLICATION_MODAL);
        combatStage.initOwner(App.getWindow());
        CombatView combatView = new CombatView(npc, (victory) -> {
            combatStage.close();
            if (victory) {
                narrate("You won!");
                npc.onDefeated();
                Driver.getPlayer().updateKillCount(1);
                npcImgView.setImage(null);
                updateStatsLabels();
            } else {
                narrate("You lost!\nGame over!");
                Stage window = (Stage)App.getWindow();
                window.setScene(gameOver);
            }
        });
        combatStage.setScene(combatView.returnScene());
        combatStage.show();
    }

    /**
     * To be called whenever the player moves into a new neighbourhood.
     * Updates the background image, unloads/loads the NPCs/items/costumes,
     * and other GUI updates, as needed.
     */
    public void updateView() {
        Neighbourhood nbhd = Driver.getMap().get(Driver.getPlayer().getLocation());

        // Set bg image
        String imgName = nbhd.getBackground();
        Image bgImg = new Image(getClass().getResource("/resources/assets/backgrounds/" + imgName).toString());
        bgView.setImage(bgImg);

        // Set neighbourhood label
        nbhdLabel.setText(nbhd.getName());

        // Unload current NPC and load new ones (if there are any);
        unloadNPC();
        if (nbhd.getNPC() != null) loadNPC(nbhd.getNPC());

        // Narrate room name
        narrate("You've entered: " + nbhd.getName());

        // Set items in neighbourhood
        itemsList.getChildren().clear();
        ArrayList<Item> items = nbhd.getItems();
        for (Item item : items) {
            HBox itemBox = createItemBox(item);
            itemsList.getChildren().add(itemBox);
        }
    }

    /**
     * Creates an HBox containing item details, to list in the GUI.
     */
    private HBox createItemBox(Item item) {
        // Root container.
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);
        container.prefHeight(Region.USE_COMPUTED_SIZE);
        container.prefWidth(Region.USE_COMPUTED_SIZE);
        // Contains icon and label
        HBox detailContainer = new HBox();
        detailContainer.setAlignment(Pos.CENTER_LEFT);
        detailContainer.prefHeight(Region.USE_COMPUTED_SIZE);
        detailContainer.prefWidth(Region.USE_COMPUTED_SIZE);
        HBox.setHgrow(detailContainer, Priority.ALWAYS);
        // Take/sell button
        HBox btnContainer = new HBox();
        btnContainer.setAlignment(Pos.CENTER_RIGHT);
        btnContainer.prefHeight(Region.USE_COMPUTED_SIZE);
        btnContainer.prefWidth(Region.USE_COMPUTED_SIZE);
        HBox.setHgrow(btnContainer, Priority.NEVER);
        // Add containers to root.
        container.getChildren().add(detailContainer);
        container.getChildren().add(btnContainer);
        // Set image view for item icon
        Image itemImg = new Image(getClass().getResource(item.getIcon()).toString());
        ImageView itemImgView = new ImageView(itemImg);
        itemImgView.setFitWidth(30);
        itemImgView.setFitHeight(30);
        detailContainer.getChildren().add(itemImgView);
        // Set label for item name
        Label itemLabel = new Label(item.getName());
        detailContainer.getChildren().add(itemLabel);
        // Set button for take/sell
        Button takeBtn = new Button("Take");
        takeBtn.setOnAction(_evt -> {
            // Move to the inventory list in GUI.
            itemsList.getChildren().remove(container);
            inventoryList.getChildren().add(container);
            // Move to the player's inventory from the nbhd.
            Driver.getMap().get(Driver.getPlayer().getLocation()).removeItem(item);
            Driver.getPlayer().inventory.add(item);
            takeBtn.setText("Use");
            takeBtn.setOnAction(__evt -> {
                item.apply();
                if (item.getPerishable()) {
                    Driver.getPlayer().inventory.remove(item);
                    inventoryList.getChildren().remove(container);
                }

                if (item instanceof Costume) {
                    updateEquippedCostume();
                }
            });
        });
        btnContainer.getChildren().add(takeBtn);

        return container;
    }

    /**
     * Loads an NPC into the image view in the center pane.
     * This also includes settings click events to indicate the NPC
     * is selected, and adding effects.
     *
     * @param npc the NPC to load.
     */
    public void loadNPC(NPC npc) {
        if (!npc.isAlive()) return;

        // Set image and cursor property.
        Image npcImg = new Image(getClass().getResource(npc.getImagePath()).toString());
        npcImgView.setImage(npcImg);
        npcImgView.setCursor(Cursor.HAND);

        // Create a glow effect for selected NPCs
        DropShadow glow = new DropShadow();
        glow.setBlurType(BlurType.THREE_PASS_BOX);
        glow.setColor(Color.GOLD);
        glow.setSpread(10);
        glow.setRadius(2);
        glow.setOffsetX(0);
        glow.setOffsetY(0);

        // Set glow effect on click.
        npcImgView.setOnMouseClicked(evt -> {
            if (npc.selected) {
                npc.selected = false;
                npcImgView.setEffect(null);

                // Disable interact/fight buttons.
                fightBtn.setDisable(true);
                interactBtn.setDisable(true);
            } else {
                npc.selected = true;
                npcImgView.setEffect(glow);

                // Enable interact/fight buttons.
                fightBtn.setDisable(false);
                interactBtn.setDisable(false);
            }
        });

    }

    /**
     * Unloads the NPC from the ImageView in the center pane.
     * Also removes effects and click events.
     */
    public void unloadNPC() {
        npcImgView.setImage(null);
        npcImgView.setCursor(Cursor.DEFAULT);
        npcImgView.setEffect(null);
        npcImgView.setOnMouseClicked(null);
    }

    /**
     * Display text in the central text pane.
     * @param text
     */
    public void narrate(String text) {
        textArea.setText(text);
    }

    /**
     * Update labels showing kills, friends, candies. The data
     * is pulled from the player instance and labels are updated
     * accordingly.
     */
    public void updateStatsLabels() {
        killCountLabel.setText("Kills: " + Driver.getPlayer().getKillCount());
        friendCountLabel.setText("Friends: " + Driver.getPlayer().getFriendScore());
        candyCountLabel.setText("Candies: " + Driver.getPlayer().getCandiesCollected());
    }

    /**
     * To be called when a new costume is equipped. Updates the
     * costume icon and label in the GUI.
     */
    public void updateEquippedCostume() {
        Costume costume = Driver.getPlayer().getEquippedCostume();
        if (costume == null) return;

        Image costumeImg = new Image(getClass().getResource(costume.getIcon()).toString());
        costumeImgView.setImage(costumeImg);
        costumeLabel.setText(costume.getName());
    }
}
