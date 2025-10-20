package mr.demonid.pk8000.ru.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/**
 * Класс для возврата ошибки.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int errorCode;
    private int status;
    private String error;
    private String message;
    private String path;
}
