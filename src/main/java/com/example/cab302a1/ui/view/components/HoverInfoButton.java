package com.example.cab302a1.ui.view.components;
// ↑ card 폴더에 두었으면 ui.view.card 로 변경

import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public final class HoverInfoButton {

    private HoverInfoButton() {}

    /** Returns a Button (which IS a Node) */
    public static Button of(Supplier<String> tooltipSupplier) {
        Button info = new Button("!");
        info.getStyleClass().add("info-btn");

        Tooltip tooltip = new Tooltip();
        info.setTooltip(tooltip);

        AtomicReference<String> cache = new AtomicReference<>(null);

        tooltip.setOnShowing(e -> {
            String cached = cache.get();
            if (cached == null) {
                String text = Objects.toString(
                        tooltipSupplier != null ? tooltipSupplier.get() : "", "");
                cache.compareAndSet(null, text);
                cached = text;
            }
            tooltip.setText(cached);
        });

        return info;
    }
}
