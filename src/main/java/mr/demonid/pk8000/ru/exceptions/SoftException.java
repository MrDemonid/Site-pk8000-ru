package mr.demonid.pk8000.ru.exceptions;

public class SoftException extends ServiceException {

    public SoftException(int code, String message) {
        super(code, message);
    }
}
