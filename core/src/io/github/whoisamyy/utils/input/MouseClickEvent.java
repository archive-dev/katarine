package io.github.whoisamyy.utils.input;

import com.badlogic.gdx.math.Vector2;
import io.github.whoisamyy.editor.Editor;
import io.github.whoisamyy.utils.Utils;

public class MouseClickEvent {
    private float mouseX;
    private float mouseY;
    private Integer button;
    private Boolean isButtonPressed; //I want it nullable
    private Boolean isDrag;
    private Vector2 dragDelta = new Vector2();

    private Boolean isScroll;
    private Float scrollAmountX;
    private Float scrollAmountY;

    public MouseClickEvent(int mouseX, int mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    public MouseClickEvent(Boolean isScroll, Float scrollAmountX, Float scrollAmountY) {
        this.isScroll = isScroll;
        this.scrollAmountX = scrollAmountX;
        this.scrollAmountY = scrollAmountY;
    }


    public MouseClickEvent(int mouseX, int mouseY, int button, Boolean isButtonPressed) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.button = button;
        this.isButtonPressed = isButtonPressed;
    }

    public MouseClickEvent(int mouseX, int mouseY, Vector2 dragDelta, Boolean isDrag) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.dragDelta = dragDelta;
        this.dragDelta.y *= -1;
        this.dragDelta.y /= Utils.PPU;
        this.dragDelta.x /= Utils.PPU;
        this.isDrag = isDrag;
    }

    /**
     * @return x world position of mouse
     */
    public float getMouseX() {
        return mouseX/ Utils.PPU;
    }

    /**
     * @return y world position of mouse
     */
    public float getMouseY() {
        return Editor.instance.getHeight()-(mouseY/ Utils.PPU);
    }

    /**
     * @return world position of mouse
     */
    public Vector2 getMousePosition() {
        return new Vector2(getMouseX(), getMouseY());
    }

    /**
     * Returns {@link Integer} because of how detection of button clicked works. So it may be null.
     * @return
     */
    public Integer getButton() {
        return button;
    }

    public Boolean isButtonPressed() {
        return isButtonPressed;
    }

    public Boolean isDrag() {
        return isDrag;
    }

    public Boolean isScroll() {
        return isScroll;
    }

    public Float getScrollAmountX() {
        return scrollAmountX;
    }

    public Float getScrollAmountY() {
        return scrollAmountY;
    }


    public void setMouseX(float mouseX) {
        this.mouseX = mouseX;
    }

    public void setMouseY(int mouseY) {
        this.mouseY = mouseY;
    }

    public void setButton(Integer button) {
        this.button = button;
    }

    public void setButtonPressed(Boolean buttonPressed) {
        isButtonPressed = buttonPressed;
    }

    public void setDrag(Boolean drag) {
        isDrag = drag;
    }

    public void setScroll(Boolean scroll) {
        isScroll = scroll;
    }

    public void setScrollAmountX(Float scrollAmountX) {
        this.scrollAmountX = scrollAmountX;
    }

    public void setScrollAmountY(Float scrollAmountY) {
        this.scrollAmountY = scrollAmountY;
    }

    public Vector2 getDragDelta() {
        return dragDelta;
    }
}
