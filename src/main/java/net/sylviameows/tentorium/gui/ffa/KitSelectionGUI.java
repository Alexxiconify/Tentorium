package net.sylviameows.tentorium.gui.ffa;

import net.kyori.adventure.text.Component;
import net.sylviameows.tentorium.gui.EmptyComponent;
import net.sylviameows.tentorium.utilities.Palette;
import net.sylviameows.tentorium.utilities.gui.AbstractGUI;
import net.sylviameows.tentorium.utilities.gui.Template;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public class KitSelectionGUI extends AbstractGUI {
    public static final Template TEMPLATE = new Template(
            "Select Kit",
            Arrays.asList(
                    "/ . / . R . / . /",
                    "# / W . . . A / #",
                    "/ . / . S . / . /"
            )
    );

    public KitSelectionGUI() {
        super(TEMPLATE);

        TEMPLATE.addButton(this, 'W', new SelectKitButton(
                Component.text("Knight").color(Palette.LIME),
                Material.IRON_SWORD, List.of(
                        Component.text("Follow the footsteps of the trained knight").color(Palette.GRAY),
                        Component.text("with speedy movement and swift attacks.").color(Palette.GRAY),
                        Component.empty(),
                        Component.text("\uD83D\uDDE1 ᴍᴇʟᴇᴇ").color(Palette.DARK_GRAY)
                ), "knight"
        ));
        TEMPLATE.addButton(this, 'R', new SelectKitButton(
                Component.text("Royal").color(Palette.YELLOW),
                Material.GOLDEN_HELMET, List.of(
                        Component.text("Use expensive equipment fit for royalty").color(Palette.GRAY),
                        Component.text("to squish your foes in battle.").color(Palette.GRAY),
                        Component.empty(),
                        Component.text("\uD83D\uDDE1 ᴍᴇʟᴇᴇ").color(Palette.DARK_GRAY)
                ),
                "royal"
        ));
        TEMPLATE.addButton(this, 'A', new SelectKitButton(
                Component.text("Archer").color(Palette.AQUA),
                Material.BOW, List.of(
                        Component.text("Fight from range using your archery prowess").color(Palette.GRAY),
                        Component.text("to pierce the hearts of your enemies.").color(Palette.GRAY),
                        Component.empty(),
                        Component.text("\uD83C\uDFF9 ʀᴀɴɢᴇᴅ").color(Palette.DARK_GRAY)
                ),
                "archer"
        ));

        TEMPLATE.addButton(this, 'S', new SelectKitButton(
                Component.text("Shuffle").color(Palette.WHITE),
                Material.FLINT, List.of(
                        Component.text("Selects a random one of these kits").color(Palette.GRAY),
                        Component.text("after each time you perish.").color(Palette.GRAY),
                        Component.empty(),
                        Component.text("⇄ ʀᴀɴᴅᴏᴍ").color(Palette.DARK_GRAY)
                ),
                "shuffle"
        ));

        TEMPLATE.addButtons(this, '#', new EmptyComponent(EmptyComponent.Style.DARK));
        TEMPLATE.addButtons(this, '/', new EmptyComponent(EmptyComponent.Style.LIGHT));
        TEMPLATE.addButtons(this, '.', new EmptyComponent(EmptyComponent.Style.INVISIBLE));
    }
}
