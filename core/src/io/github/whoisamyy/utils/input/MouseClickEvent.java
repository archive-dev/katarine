package io.github.whoisamyy.utils.input;

import com.badlogic.gdx.math.Vector2;
import io.github.whoisamyy.test.Game;

public class MouseClickEvent {
    private int mouseX;
    private int mouseY;
    private Integer button;
    private Boolean isButtonPressed; //I want it nullable
    private Boolean isDrag;

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

    public MouseClickEvent(int mouseX, int mouseY, Boolean isDrag) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.isDrag = isDrag;
    }

    public float getMouseX() {
        return mouseX/Game.getScreenToWorld();
    }

    public float getMouseY() {
        return Game.getHeight()-(mouseY/Game.getScreenToWorld());
    }

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

    public Vector2 getMousePosition() {
        return new Vector2(getMouseX(), getMouseY());
    }

    public void setMouseX(int mouseX) {
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
}
