import javafx.scene.input.KeyCode;

/**
 * InputHandler processes and manages player inputs in the game.
 * It tracks the most recent input and provides mechanisms to consume and check for pending inputs.
 * @author Omar Sanad
 * @author Alex Vesely
 */
public class InputHandler {
    private boolean isInputPending;
    private GameController.PlayerInput currentInput;

    /**
     * Registers a key press and maps it to a corresponding player input.
     * @param code the KeyCode representing the player's input
     */
    public void registerInput(KeyCode code) {

        switch (code) {
            case UP -> currentInput = GameController.PlayerInput.UP;
            case DOWN -> currentInput = GameController.PlayerInput.DOWN;
            case LEFT -> currentInput = GameController.PlayerInput.LEFT;
            case RIGHT -> currentInput = GameController.PlayerInput.RIGHT;
            default -> {
                currentInput = null; // Reset or handle invalid input
                isInputPending = false; // No valid input is pending
            }
        }
        if (currentInput != null) {
            isInputPending = true;
        }
    }


    /**
     * Checks if there is a pending player input.
     * @return true if there is an input waiting to be consumed, false otherwise
     */
    public boolean isInputPending() {
        return isInputPending;
    }

    /**
     * Consumes the current player input and marks it as processed.
     * If no input is pending, returns {@code null}.
     * @return the current player input if available, or {@code null} if no input is pending
     */
    public GameController.PlayerInput consumeInput() {
        if (isInputPending) {
            isInputPending = false;
            return currentInput;
        }
        return null;
    }
}
