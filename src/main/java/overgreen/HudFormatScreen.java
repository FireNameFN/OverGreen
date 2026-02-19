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

    private int layoutX;

    private int layoutY;

    private int layoutHeight;

    public HudFormatScreen(Screen parent, HudFormatOption option) {
        super(TITLE);

        this.parent = parent;
        this.option = option;
    }

    @Override
    protected void init() {
        int descriptionHeight = font.wordWrapHeight(FORMAT_DESCRIPTION, 400 - 12);

        layoutHeight = descriptionHeight + 50;

        layoutX = width / 2 - 200;
        layoutY = (height - layoutHeight) / 2;

        EditBox edit = new EditBox(font, layoutX, layoutY, 400, 20, EDIT_BOX);

        edit.setMaxLength(128);

        edit.setValue(option.getValue());

        int y = layoutY + descriptionHeight + 30;

        Button cancelButton = Button.builder(BACK_BUTTON, button -> onClose())
            .bounds(layoutX, y, 80, 20)
            .build();

        Button undoButton = Button.builder(UNDO_BUTTON, button -> edit.setValue(option.getValue()))
            .bounds(layoutX + 220, y, 80, 20)
            .build();

        undoButton.active = false;

        OnPress onSavePress = button -> {
            option.setValue(edit.getValue());

            undoButton.active = false;
            button.active = false;
        };

        Button saveButton = Button.builder(SAVE_BUTTON, onSavePress)
            .bounds(layoutX + 320, y, 80, 20)
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
        int backgroundX = layoutX - 6;
        int backgroundY = layoutY - 5;

        int backgroundX2 = layoutX + 406;
        int backgroundY2 = layoutY + layoutHeight + 5;

        int titleY2 = backgroundY - 3;
        int titleY = titleY2 - font.lineHeight * 2;

        graphics.fill(backgroundX, titleY, backgroundX2, titleY2, 0x90000000);

        graphics.drawCenteredString(font, title, width / 2, titleY + (font.lineHeight + 1) / 2, CommonColors.WHITE);

        graphics.fill(backgroundX, backgroundY, backgroundX2, backgroundY2, 0x40000000);

        graphics.drawWordWrap(font, FORMAT_DESCRIPTION, layoutX + 3, layoutY + 25, 394, CommonColors.WHITE);

        super.render(graphics, x, y, delta);
    }

    @Override
    public void onClose() {
        minecraft.setScreen(parent);
    }
}
