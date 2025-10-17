package com.example.cab302a1.ui.view.components;
// ↑ card 폴더에 두었으면 ui.view.card 로 변경

import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public final class HoverInfoButton {

    private HoverInfoButton() {}

    /** Returns a Button (which IS a Node) that shows info when clicked */
    public static Button of(Supplier<String> tooltipSupplier) {
        Button info = new Button("i");
        info.getStyleClass().add("info-btn");
        info.setFocusTraversable(false);
        info.setPrefSize(40, 40);
        info.setMinSize(40, 40);
        info.setMaxSize(40, 40);
        info.setPickOnBounds(true);

        Tooltip tooltip = new Tooltip();
        tooltip.setWrapText(true);
        tooltip.setMaxWidth(300);
        
        // Disable automatic tooltip behavior - we control it manually
        tooltip.setShowDelay(Duration.INDEFINITE);
        tooltip.setShowDuration(Duration.INDEFINITE);
        tooltip.setHideDelay(Duration.INDEFINITE);
        
        info.setTooltip(tooltip);

        AtomicReference<String> cache = new AtomicReference<>(null);

        // Click to toggle tooltip
        info.setOnAction(e -> {
            if (tooltip.isShowing()) {
                // Hide if already showing
                tooltip.hide();
            } else {
                // Load content if needed
                String cached = cache.get();
                if (cached == null) {
                    String text = Objects.toString(
                            tooltipSupplier != null ? tooltipSupplier.get() : "", "");
                    cache.compareAndSet(null, text);
                    cached = text;
                }
                tooltip.setText(cached);
                
                // Show tooltip to the left of the button, not covering it
                var scene = info.getScene();
                if (scene != null && scene.getWindow() != null) {
                    var localPos = info.localToScene(0, 0);
                    tooltip.show(info,
                        scene.getWindow().getX() + localPos.getX() - 310, // Position to the left (300px width + 10px gap)
                        scene.getWindow().getY() + localPos.getY());
                }
            }
        });
        
        // Hide tooltip when clicking elsewhere
        info.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnMousePressed(event -> {
                    var node = event.getPickResult().getIntersectedNode();
                    // Hide if clicking outside the info button
                    if (node != info && !info.getChildrenUnmodifiable().contains(node)) {
                        tooltip.hide();
                    }
                });
            }
        });

        return info;
    }
}
