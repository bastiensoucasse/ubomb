package fr.ubx.poo.engine;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.BitSet;

import static javafx.scene.input.KeyCode.*;

public final class Input {
    private final BitSet keyboardBitSet = new BitSet();

    public Input(Scene scene) {
        EventHandler<KeyEvent> keyPressedEventHandler = event -> {
            keyboardBitSet.set(event.getCode().ordinal(), true);
        };

        EventHandler<KeyEvent> keyReleasedEventHandler = event -> {
            keyboardBitSet.set(event.getCode().ordinal(), false);
        };

        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyPressedEventHandler);
        scene.addEventFilter(KeyEvent.KEY_RELEASED, keyReleasedEventHandler);
    }

    public void clear() {
        keyboardBitSet.clear();
    }

    public boolean isBomb() {
        return is(SPACE);
    }

    public boolean isExit() {
        return is(ESCAPE);
    }

    public boolean isKey() {
        return is(ENTER);
    }

    public boolean isMoveDown() {
        return is(DOWN) && !is(UP);
    }

    public boolean isMoveLeft() {
        return is(LEFT) && !is(RIGHT);
    }

    public boolean isMoveRight() {
        return is(RIGHT) && !is(LEFT);
    }

    public boolean isMoveUp() {
        return is(UP) && !is(DOWN);
    }

    private boolean is(KeyCode key) {
        return keyboardBitSet.get(key.ordinal());
    }
}
