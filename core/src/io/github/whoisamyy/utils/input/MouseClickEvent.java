package io.github.whoisamyy.utils.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

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

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
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

}
