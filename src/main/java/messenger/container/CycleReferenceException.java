package messenger.container;

/**
 * Обнаружена циклическая зависимость
 */
public class CycleReferenceException extends Exception {
    public CycleReferenceException(String message) {
        super(message);
    }
}
