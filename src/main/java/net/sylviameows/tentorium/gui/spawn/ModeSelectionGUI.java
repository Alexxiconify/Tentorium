package net.sylviameows.tentorium.gui.spawn;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.sylviameows.tentorium.gui.EmptyComponent;
import net.sylviameows.tentorium.gui.IconComponent;
import net.sylviameows.tentorium.utilities.Palette;
import net.sylviameows.tentorium.utilities.gui.AbstractGUI;
import net.sylviameows.tentorium.utilities.gui.Template;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public class ModeSelectionGUI extends AbstractGUI {
    public static final Template TEMPLATE = new Template(
            "Select Mode",
            Arrays.asList(
                    "# / . / I / . / #",
                    "/ S T . F . K P /",
                    "# / . / . / . / #"
            )
    );

    public ModeSelectionGUI() {
        super(TEMPLATE);

        TEMPLATE.addButton(this, 'I', new IconComponent(
                Component.text("Mode Selector").color(Palette.RED_LIGHT),
                Material.COMPASS, List.of(
                        Component.text("Select the mode you would like to play").color(Palette.GRAY),
                        Component.text("with the items below.").color(Palette.GRAY)
                )
        ));
        TEMPLATE.addButton(this, 'S', new SelectModeButton(
                Component.text("Spleef").color(Palette.AQUA),
                Material.SNOWBALL, List.of(
                        Component.text("Equipped with a shovel on thin layers").color(Palette.GRAY),
                        Component.text("of snow you must send you foes to the void.").color(Palette.GRAY)
                ),
                "spleef"
        ));
        TEMPLATE.addButton(this, 'T', new SelectModeButton(
                MiniMessage.miniMessage().deserialize("<rainbow>Rainbow Rumble").append(Component.text(" (TNT Run)").color(Palette.DARK_GRAY)),
                Material.FIRE_CHARGE, List.of(
                        Component.text("Compete with others to be the last").color(Palette.GRAY),
                        Component.text("one running on unstable ground.").color(Palette.GRAY)
                ),
                "tntrun"
        ));

        TEMPLATE.addButton(this, 'F', new SelectModeButton(
                Component.text("Free For All").color(Palette.LIME).append(Component.text(" (FFA)").color(Palette.DARK_GRAY)),
                Material.IRON_SWORD, List.of(
                        Component.text("Use weapons to try and achieve the ").color(Palette.GRAY),
                        Component.text("most kills over your opponents.").color(Palette.GRAY)
                ),
                "ffa"
        ));

        TEMPLATE.addButton(this, 'K', new SelectModeButton(
                Component.text("Knockback").color(Palette.PURPLE_LIGHT),
                Material.COD, List.of(
                        Component.text("Equipped with only a stick you must").color(Palette.GRAY),
                        Component.text("send your opponents over the edge.").color(Palette.GRAY)
                ),
                "knockback"
        ));
        TEMPLATE.addButton(this, 'P', new SelectModeButton(
                Component.text("Parkour").color(Palette.BLUE_DARK),
                Material.FIRE_CHARGE, List.of(
                        Component.text("Jump from block to block for a").color(Palette.GRAY),
                        Component.text("fun little challenge while waiting.").color(Palette.GRAY)
                ),
                "parkour"
        ));

        TEMPLATE.addButtons(this, '#', new EmptyComponent(EmptyComponent.Style.DARK));
        TEMPLATE.addButtons(this, '/', new EmptyComponent(EmptyComponent.Style.LIGHT));
        TEMPLATE.addButtons(this, '.', new EmptyComponent(EmptyComponent.Style.INVISIBLE));
    }
}
