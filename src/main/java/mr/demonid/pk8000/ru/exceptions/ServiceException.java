package mr.demonid.pk8000.ru.exceptions;

/**
 * Базовый класс исключений.
 */
public abstract class ServiceException extends RuntimeException {
    private final int code;

    public ServiceException(final int code, final String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
