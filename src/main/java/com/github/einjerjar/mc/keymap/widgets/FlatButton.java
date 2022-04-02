package com.github.einjerjar.mc.keymap.widgets;

import com.github.einjerjar.mc.keymap.screen.Tooltipped;
import com.github.einjerjar.mc.keymap.utils.Utils;
import com.github.einjerjar.mc.keymap.utils.WidgetUtils;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class FlatButton extends FlatWidget<FlatButton> implements Selectable, Tooltipped {
    Text text;
    Text tooltip = null;
    ButtonAction action;

    public void click() {
        action.onAction(this);
    }

    @Override
    public SelectionType getType() {
        if (focused) return SelectionType.FOCUSED;
        if (hovered) return SelectionType.HOVERED;
        return SelectionType.NONE;
    }

    public FlatButton setTooltip(Text tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }

    @Override
    public List<Text> getToolTips() {
        return tooltip != null ? new ArrayList<>() {{
            add(tooltip);
        }} : new ArrayList<>();
    }

    @Override
    public Text getFirstToolTip() {
        return Utils.safeGet(getToolTips(), 0);
    }

    public interface ButtonAction {
        void onAction(FlatButton button);
    }

    public FlatButton(int x, int y, int w, int h, Text text) {
        super(FlatButton.class, x, y, w, h);
        this.text = text;
        this.setDrawBg(true).setDrawBorder(true).setDrawShadow(true);
    }

    public FlatButton(Class<FlatButton> self, int x, int y, int w, int h, Text text) {
        super(self, x, y, w, h);
        this.text = text;
        this.setDrawBg(true).setDrawBorder(true).setDrawShadow(true);
    }

    public FlatButton setAction(ButtonAction action) {
        this.action = action;
        return self;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (hovered) {
            setFocused(true);
            playDownSound();
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        setFocused(false);
        if (hovered && action != null) {
            action.onAction(this);
            return true;
        }
        return false;
    }

    @Override
    public void renderWidget(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int cBg   = color.bg.getVariant(enabled, focused, hovered);
        int cBor  = color.border.getVariant(enabled, focused, hovered);
        int cText = color.text.getVariant(enabled, focused, hovered);

        if (drawBorder) WidgetUtils.fillBox(this, matrices, x, y, w, h, cBg);
        if (drawBg) WidgetUtils.drawBoxOutline(this, matrices, x, y, w, h, cBor);
        drawCenteredText(matrices, text, x, y, drawShadow, cText);
    }
}
