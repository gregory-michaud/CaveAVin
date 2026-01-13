package fr.eni.cave.exception;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;
import java.util.stream.Collectors;

@ControllerAdvice
@AllArgsConstructor
public class AppExceptionHandler {

    private MessageSource messageSource;

    // Préciser les exceptions à gérer
    @ExceptionHandler(value={Exception.class})
    public ResponseEntity<String> capturerException(Exception ex)
    {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ex.getMessage());
    }

    @ExceptionHandler(value={MethodArgumentNotValidException.class})
    public ResponseEntity<String> capturerMethodArgumentNotValidException(MethodArgumentNotValidException ex, Locale locale)
    {
        String titreMsg = messageSource.getMessage("notvalidexception", null, locale);
        String message = titreMsg + " " + ex.getFieldErrors().stream().map(e->e.getDefaultMessage()).collect(Collectors.joining(" - "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
}
