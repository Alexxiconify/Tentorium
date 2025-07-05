package net.sylviameows.tentorium.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.sylviameows.tentorium.utilities.GameUtilities;
import net.sylviameows.tentorium.utilities.gui.AbstractGUI;
import net.sylviameows.tentorium.utilities.gui.Template;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public class GameModeGUIs {
    // KitSelectionGUI logic
    public static class KitSelectionGUI extends AbstractGUI {
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

            TEMPLATE.addButton(this, 'W', new GuiButtons.SelectKitButton(
                    Component.text("Knight").color(GameUtilities.Palette.LIME),
                    Material.IRON_SWORD, List.of(
                            Component.text("Follow the footsteps of the trained knight").color(GameUtilities.Palette.GRAY),
                            Component.text("with speedy movement and swift attacks.").color(GameUtilities.Palette.GRAY),
                            Component.empty(),
                            Component.text("\uD83D\uDDE1 ᴍᴇʟᴇᴇ").color(GameUtilities.Palette.DARK_GRAY)
                    ), "knight"
            ));
            TEMPLATE.addButton(this, 'R', new GuiButtons.SelectKitButton(
                    Component.text("Royal").color(GameUtilities.Palette.YELLOW),
                    Material.GOLDEN_HELMET, List.of(
                            Component.text("Use expensive equipment fit for royalty").color(GameUtilities.Palette.GRAY),
                            Component.text("to squish your foes in battle.").color(GameUtilities.Palette.GRAY),
                            Component.empty(),
                            Component.text("\uD83D\uDDE1 ᴍᴇʟᴇᴇ").color(GameUtilities.Palette.DARK_GRAY)
                    ),
                    "royal"
            ));
            TEMPLATE.addButton(this, 'A', new GuiButtons.SelectKitButton(
                    Component.text("Archer").color(GameUtilities.Palette.AQUA),
                    Material.BOW, List.of(
                            Component.text("Fight from range using your archery prowess").color(GameUtilities.Palette.GRAY),
                            Component.text("to pierce the hearts of your enemies.").color(GameUtilities.Palette.GRAY),
                            Component.empty(),
                            Component.text("\uD83C\uDFF9 ʀᴀɴɢᴇᴅ").color(GameUtilities.Palette.DARK_GRAY)
                    ),
                    "archer"
            ));

            TEMPLATE.addButton(this, 'S', new GuiButtons.SelectKitButton(
                    Component.text("Shuffle").color(GameUtilities.Palette.WHITE),
                    Material.FLINT, List.of(
                            Component.text("Selects a random one of these kits").color(GameUtilities.Palette.GRAY),
                            Component.text("after each time you perish.").color(GameUtilities.Palette.GRAY),
                            Component.empty(),
                            Component.text("⇄ ʀᴀɴᴅᴏᴍ").color(GameUtilities.Palette.DARK_GRAY)
                    ),
                    "shuffle"
            ));

            TEMPLATE.addButtons(this, '#', new GuiComponents.EmptyComponent(GuiComponents.EmptyComponent.Style.DARK));
            TEMPLATE.addButtons(this, '/', new GuiComponents.EmptyComponent(GuiComponents.EmptyComponent.Style.LIGHT));
            TEMPLATE.addButtons(this, '.', new GuiComponents.EmptyComponent(GuiComponents.EmptyComponent.Style.INVISIBLE));
        }
    }

    // ModeSelectionGUI logic
    public static class ModeSelectionGUI extends AbstractGUI {
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

            TEMPLATE.addButton(this, 'I', new GuiComponents.IconComponent(
                    Component.text("Mode Selector").color(GameUtilities.Palette.RED_LIGHT),
                    Material.COMPASS, List.of(
                            Component.text("Select the mode you would like to play").color(GameUtilities.Palette.GRAY),
                            Component.text("with the items below.").color(GameUtilities.Palette.GRAY)
                    )
            ));
            TEMPLATE.addButton(this, 'S', new GuiButtons.SelectModeButton(
                    Component.text("Spleef").color(GameUtilities.Palette.AQUA),
                    Material.SNOWBALL, List.of(
                            Component.text("Equipped with a shovel on thin layers").color(GameUtilities.Palette.GRAY),
                            Component.text("of snow you must send you foes to the void.").color(GameUtilities.Palette.GRAY)
                    ),
                    "spleef"
            ));
            TEMPLATE.addButton(this, 'T', new GuiButtons.SelectModeButton(
                    MiniMessage.miniMessage().deserialize("<rainbow>Rainbow Rumble").append(Component.text(" (TNT Run)").color(GameUtilities.Palette.DARK_GRAY)),
                    Material.FIRE_CHARGE, List.of(
                            Component.text("Compete with others to be the last").color(GameUtilities.Palette.GRAY),
                            Component.text("one running on unstable ground.").color(GameUtilities.Palette.GRAY)
                    ),
                    "tntrun"
            ));

            TEMPLATE.addButton(this, 'F', new GuiButtons.SelectModeButton(
                    Component.text("Free For All").color(GameUtilities.Palette.LIME).append(Component.text(" (FFA)").color(GameUtilities.Palette.DARK_GRAY)),
                    Material.IRON_SWORD, List.of(
                            Component.text("Use weapons to try and achieve the ").color(GameUtilities.Palette.GRAY),
                            Component.text("most kills over your opponents.").color(GameUtilities.Palette.GRAY)
                    ),
                    "ffa"
            ));

            TEMPLATE.addButton(this, 'K', new GuiButtons.SelectModeButton(
                    Component.text("Knockback").color(GameUtilities.Palette.PURPLE_LIGHT),
                    Material.COD, List.of(
                            Component.text("Equipped with only a stick you must").color(GameUtilities.Palette.GRAY),
                            Component.text("send your opponents over the edge.").color(GameUtilities.Palette.GRAY)
                    ),
                    "knockback"
            ));
            TEMPLATE.addButton(this, 'P', new GuiButtons.SelectModeButton(
                    Component.text("Parkour").color(GameUtilities.Palette.BLUE_DARK),
                    Material.FIRE_CHARGE, List.of(
                            Component.text("Jump from block to block for a").color(GameUtilities.Palette.GRAY),
                            Component.text("fun little challenge while waiting.").color(GameUtilities.Palette.GRAY)
                    ),
                    "parkour"
            ));

            TEMPLATE.addButtons(this, '#', new GuiComponents.EmptyComponent(GuiComponents.EmptyComponent.Style.DARK));
            TEMPLATE.addButtons(this, '/', new GuiComponents.EmptyComponent(GuiComponents.EmptyComponent.Style.LIGHT));
            TEMPLATE.addButtons(this, '.', new GuiComponents.EmptyComponent(GuiComponents.EmptyComponent.Style.INVISIBLE));
        }
    }
} 