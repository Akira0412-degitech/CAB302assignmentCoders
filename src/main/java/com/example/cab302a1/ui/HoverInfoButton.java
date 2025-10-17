package com.example.cab302a1.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * A small "i" button that shows info when clicked.
 * Tooltip text is provided by a Supplier<String> and is lazily loaded (computed once on first click).
 */

public class HoverInfoButton extends Button {

    private final Tooltip tooltip = new Tooltip("Loading...");
    private Supplier<String> contentSupplier;
    private boolean loaded = false;
    private boolean cacheEnabled = true;

    public HoverInfoButton() {
        super("i");
        getStyleClass().add("info-btn");
        setFocusTraversable(false);
        setPrefSize(40, 40);
        setMinSize(40, 40);
        setMaxSize(40, 40);
        setPickOnBounds(true);

        tooltip.setWrapText(true);
        tooltip.setMaxWidth(300);
        
        // Disable automatic tooltip behavior - we control it manually
        tooltip.setShowDelay(Duration.INDEFINITE);
        tooltip.setShowDuration(Duration.INDEFINITE);
        tooltip.setHideDelay(Duration.INDEFINITE);
        
        setTooltip(tooltip);

        // Click to toggle tooltip
        setOnAction(e -> {
            if (tooltip.isShowing()) {
                // Hide if already showing
                tooltip.hide();
            } else {
                // Load content if needed
                if (!loaded || !cacheEnabled) {
                    String text = (contentSupplier != null) ? contentSupplier.get() : "(no content)";
                    tooltip.setText(Objects.toString(text, "(no content)"));
                    loaded = true;
                }
                // Show tooltip to the left of the button, not covering it
                tooltip.show(this, 
                    getScene().getWindow().getX() + localToScene(0, 0).getX() - 310, // Position to the left (300px width + 10px gap)
                    getScene().getWindow().getY() + localToScene(0, 0).getY());
            }
        });
        
        // Hide tooltip when clicking elsewhere
        sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnMousePressed(event -> {
                    if (!this.getBoundsInParent().contains(event.getX(), event.getY())) {
                        tooltip.hide();
                    }
                });
            }
        });
    }

    //  Factory method:
    // *Creates a HoverInfoButton and immediately assigns the content supplier.
    public static HoverInfoButton of(Supplier<String> supplier) {
        HoverInfoButton b = new HoverInfoButton();
        b.setContentSupplier(supplier);
        return b;
    }

    //Sets or updates the supplier that provides the tooltip text.
    public void setContentSupplier(Supplier<String> supplier) {
        this.contentSupplier = supplier;
        this.loaded = false;
    }
    
}
