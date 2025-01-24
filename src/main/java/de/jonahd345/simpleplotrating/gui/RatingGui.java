package de.jonahd345.simpleplotrating.gui;

import com.plotsquared.core.plot.Plot;
import de.jonahd345.simpleplotrating.SimplePlotRating;
import de.jonahd345.simpleplotrating.model.RatingCategory;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class RatingGui {
    private SimplePlotRating plugin;

    private Gui gui;

    private Player player;

    private Plot plot;

    private int currentRating;

    private int overallRating;

    private RatingCategory currentCategory;

    public RatingGui(SimplePlotRating plugin, Player player, Plot plot) {
        this.plugin = plugin;
        this.player = player;
        this.plot = plot;
    }

    public void showGui() {
        this.gui = Gui.gui()
                .title(Component.text(""))
                .rows(6)
                .create();

        this.gui.setItem(13, ItemBuilder.from(Material.BARRIER).name(Component.text("")).asGuiItem()); // Placeholder
        for (int i = 29; i < 34; i++) {
            this.gui.setItem(i, ItemBuilder.from(Material.WHITE_STAINED_GLASS_PANE).name(Component.text(""))
                    .asGuiItem(event -> this.setRating(event.getSlot() - 28)));
        }
        this.gui.setItem(47, ItemBuilder.from(Material.OAK_DOOR).name(Component.text(""))
                .asGuiItem(event -> this.gui.close(player)));
        this.gui.setItem(49, ItemBuilder.from(Material.RED_DYE).name(Component.text(""))
                .asGuiItem(event -> {
                    this.skipCategory();
                }));
        this.gui.setItem(51, ItemBuilder.from(Material.ARROW).name(Component.text(""))
                .asGuiItem(event -> {
                    if (this.currentRating != 0) {
                        this.nextCategory();
                    }
                }));
        this.gui.getFiller().fill(ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).name(Component.text("")).asGuiItem());
        this.gui.setDefaultClickAction(event -> event.setCancelled(true));

        this.nextCategory();
        this.gui.open(this.player);
    }

    private void nextCategory() {
        if (this.currentCategory != null) {
            this.currentCategory = this.plugin.getRatingCategories().get(this.plugin.getRatingCategories().indexOf(this.currentCategory) + 1);
        } else {
            this.currentCategory = this.plugin.getRatingCategories().getFirst();
        }
        this.currentRating = 0;
        this.resetRating();
        this.gui.updateItem(13, ItemBuilder.from(this.currentCategory.getMaterial()).name(Component.text(this.currentCategory.getName())).asGuiItem());
        if (this.currentCategory.isLastOne()) {
            this.gui.updateItem(51, ItemBuilder.from(Material.ARROW).name(Component.text(""))
                    .asGuiItem(event -> {
                        if (this.currentRating != 0) {
                            new RatingSummaryGui(this.plugin, this.player, this.plot, this.overallRating, this.plugin.getPlotRatingManager().calculateBlocks(this.overallRating))
                                    .showGui();
                        }
                    }));
        }
    }

    private void skipCategory() {
        if (!this.currentCategory.isLastOne()) {
            if (this.currentRating == 0) {
                this.overallRating += 5;
                this.nextCategory();
            }
        } else {
            this.setRating(5);
        }
    }

    private void setRating(int rating) {
        if (this.currentRating == 0) {
            this.currentRating = rating;
            this.overallRating += rating;
            for (int i = 29; i < 29 + rating; i++) {
                this.gui.updateItem(i, ItemBuilder.from(Material.LIME_STAINED_GLASS_PANE).name(Component.text(""))
                        .asGuiItem());
            }
        }
    }

    private void resetRating() {
        for (int i = 29; i < 34; i++) {
            this.gui.updateItem(i, ItemBuilder.from(Material.WHITE_STAINED_GLASS_PANE).name(Component.text(""))
                    .asGuiItem(event -> this.setRating(event.getSlot() - 28)));
        }
    }
}
