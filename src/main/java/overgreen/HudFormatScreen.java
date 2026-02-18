package overgreen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.CommonColors;

final class HudFormatScreen extends Screen {
    private static final Component TITLE = Component.translatable("overgreen.config.hud_format.screen.title");
    private static final Component EDIT_BOX = Component.translatable("overgreen.config.hud_format.screen.edit");
    private static final Component BACK_BUTTON = Component.translatable("overgreen.config.hud_format.screen.back");
    private static final Component UNDO_BUTTON = Component.translatable("overgreen.config.hud_format.screen.undo");
    private static final Component SAVE_BUTTON = Component.translatable("overgreen.config.hud_format.screen.save");
    private static final Component FORMAT_DESCRIPTION = Component.translatable("overgreen.config.hud_format.screen.description");

    private final Screen parent;

    private final HudFormatOption option;

    public HudFormatScreen(Screen parent, HudFormatOption option) {
        super(TITLE);

        this.parent = parent;
        this.option = option;
    }

    @Override
    protected void init() {
        int x = width / 2 - 200;
        int y = height / 2 - 60;

        EditBox edit = new EditBox(font, x, y, 400, 20, EDIT_BOX);

        edit.setMaxLength(128);

        edit.setValue(option.getValue());

        Button cancelButton = Button.builder(BACK_BUTTON, button -> onClose())
            .bounds(x, y + 100, 80, 20)
            .build();

        Button undoButton = Button.builder(UNDO_BUTTON, button -> edit.setValue(option.getValue()))
            .bounds(x + 220, y + 100, 80, 20)
            .build();

        undoButton.active = false;

        OnPress onSavePress = button -> {
            option.setValue(edit.getValue());

            undoButton.active = false;
            button.active = false;
        };

        Button saveButton = Button.builder(SAVE_BUTTON, onSavePress)
            .bounds(x + 320, y + 100, 80, 20)
            .build();

        saveButton.active = false;

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
    public void render(GuiGraphics graphics, int x, int y, float delta) {
        super.render(graphics, x, y, delta);

        int drawX = width / 2 - 195;
        int drawY = height / 2 - 35;

        graphics.drawWordWrap(font, FORMAT_DESCRIPTION, drawX, drawY, 390, CommonColors.WHITE);
    }

    @Override
    public void onClose() {
        minecraft.setScreen(parent);
    }
}
