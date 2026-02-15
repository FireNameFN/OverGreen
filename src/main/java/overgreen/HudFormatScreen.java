package overgreen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.CommonColors;

final class HudFormatScreen extends Screen {
    private static final String[] FORMAT_DESCRIPTION = {
        "Replacements:",
        "{x} {y} {z} - coordinates.",
        "{x:0.0} {y:0.00} {z:0.000} - coordinates with custom format.",
        "{bx} {by} {bz} - block coordinates.",
        "{dir} - direction.",
        "{day} - current day.",
        "{nl} - new line."
    };

    private final Screen parent;

    private final HudFormatOption option;

    public HudFormatScreen(Screen parent, HudFormatOption option) {
        super(Component.literal("Permanent HUD Format"));

        this.parent = parent;
        this.option = option;
    }

    @Override
    protected void init() {
        int x = width / 2 - 200;
        int y = height / 2 - 60;

        EditBox edit = new EditBox(font, x, y, 400, 20, Component.literal("Format"));

        edit.setMaxLength(128);

        edit.setValue(option.getValue());

        Button cancelButton = Button.builder(Component.literal("Cancel"), button -> onClose())
            .bounds(x, y + 100, 80, 20)
            .build();

        Button undoButton = Button.builder(Component.literal("Undo"), button -> edit.setValue(option.getValue()))
            .bounds(x + 220, y + 100, 80, 20)
            .build();

        OnPress onSavePress = button -> {
            option.setValue(edit.getValue());

            undoButton.active = false;
            button.active = false;
        };

        Button saveButton = Button.builder(Component.literal("Save"), onSavePress)
            .bounds(x + 320, y + 100, 80, 20)
            .build();

        edit.setResponder(format -> {
            boolean dirty = !format.equals(option.getValue());

            undoButton.active = dirty;
            saveButton.active = dirty;
        });

        addRenderableWidget(edit);
        addRenderableWidget(cancelButton);
        addRenderableWidget(undoButton);
        addRenderableWidget(saveButton);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);

        int x = width / 2 - 195;
        int y = height / 2 - 25;

        for(int i = 0; i < FORMAT_DESCRIPTION.length; i++)
            graphics.drawString(font, FORMAT_DESCRIPTION[i], x, y + i * font.lineHeight, CommonColors.WHITE);
    }

    @Override
    public void onClose() {
        minecraft.setScreen(parent);
    }
}
