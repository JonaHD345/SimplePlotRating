package de.jonahd345.simpleplotrating.gui;

import de.jonahd345.simpleplotrating.SimplePlotRating;
import de.jonahd345.simpleplotrating.model.RatingCategory;
import dev.triumphteam.gui.guis.Gui;
import lombok.Getter;
import org.bukkit.entity.Player;

public class RatingGui {
    private SimplePlotRating plugin;

    private Gui gui;

    @Getter
    private int rating;

    public RatingGui(SimplePlotRating plugin) {
        this.plugin = plugin;
    }

    private void setupGui(RatingCategory ratingCategory) {
        this.gui.updateTitle(" " + ratingCategory.getName());
    }

    public void showGui(Player player) {
        this.gui = Gui.gui()
                .rows(6)
                .create();
        this.gui.setDefaultClickAction(event -> {
            event.setCancelled(true);
        });

        for (RatingCategory ratingCategory : this.plugin.getRatingCategories().values()) {
            this.setupGui(ratingCategory);
        }
        this.gui.open(player);
    }
}
