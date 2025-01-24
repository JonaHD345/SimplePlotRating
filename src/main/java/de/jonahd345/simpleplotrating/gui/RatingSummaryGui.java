package de.jonahd345.simpleplotrating.gui;

import com.plotsquared.core.plot.Plot;
import de.jonahd345.simpleplotrating.SimplePlotRating;
import de.jonahd345.simpleplotrating.model.GuiText;
import de.jonahd345.simpleplotrating.util.StringUtil;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class RatingSummaryGui {
    private SimplePlotRating plugin;

    private Gui gui;

    private Player player;

    private Plot plot;

    private int rating;

    private List<Material> ratingMaterials;

    public RatingSummaryGui(SimplePlotRating plugin, Player player, Plot plot, int rating, List<Material> ratingMaterials) {
        this.plugin = plugin;
        this.player = player;
        this.plot = plot;
        this.rating = rating;
        this.ratingMaterials = ratingMaterials;
    }

    public void showGui() {
        this.gui = Gui.gui()
                .title(Component.text(StringUtil.replacePlaceholder(GuiText.getTextWithPrefix(GuiText.RATING_SUMMARY_TITLE), Map.of("%rating%", String.valueOf(this.rating)))))
                .rows(6)
                .create();

        this.gui.setItem(3, 5, ItemBuilder.skull().owner(Bukkit.getOfflinePlayer(this.plot.getOwner())).name(Component.text(StringUtil.replacePlaceholder(GuiText.RATING_SUMMARY_SKULL.getText(), Map.of("%plot_owner%", Bukkit.getOfflinePlayer(this.plot.getOwner()).getName())))).asGuiItem());
        for (int i = 4; i < 7; i++) {
            this.gui.setItem(4, i, ItemBuilder.from(this.ratingMaterials.get(i - 4)).asGuiItem());
        }
        this.gui.setItem(6, 9, ItemBuilder.from(Material.LIME_DYE).name(Component.text(GuiText.RATING_SUMMARY_COMPLETE_ITEM.getText()))
                .asGuiItem(event -> {
                    this.plugin.getPlotRatingManager().setPlotRating(this.plot, this.rating, ratingMaterials, this.player);
                    this.gui.close(this.player);
                    this.player.sendMessage("" + this.rating);
                }));

        this.gui.getFiller().fill(ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).name(Component.text("")).asGuiItem());
        this.gui.setDefaultClickAction(event -> event.setCancelled(true));

        this.gui.open(this.player);
    }
}
