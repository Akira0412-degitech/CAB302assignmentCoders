package com.example.cab302a1.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * A small "!" button that shows a tooltip on hover.
 * Tooltip text is provided by a Supplier&lt;String&gt; and is lazily loaded (computed once on first hover).
 */

public class HoverInfoButton extends Button {

    private final Tooltip tooltip = new Tooltip("Loading...");
    private Supplier<String> contentSupplier;
    private boolean loaded = false;
    private boolean cacheEnabled = true;

    public HoverInfoButton() {
        super("!");
        getStyleClass().add("info-btn"); // Vincent DOOOOO
        setFocusTraversable(false);
        setPrefSize(24, 24);

        tooltip.setWrapText(true);
        tooltip.setMaxWidth(280);
        setTooltip(tooltip);

        // Hover Text calculate and HOver
        setOnMouseEntered(e -> {
            if (!loaded || !cacheEnabled) {
                String text = (contentSupplier != null) ? contentSupplier.get() : "(no content)";
                tooltip.setText(Objects.toString(text, "(no content)"));
                loaded = true;
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

    // Enables or disables caching of the tooltip text (for safety) can delete
    public void setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
        if (!cacheEnabled) this.loaded = false;
    }
}
