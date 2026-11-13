package estoque_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponse> tratarRegraDeNegocio(
            BusinessRuleException exception
    ) {
        ErrorResponse erro = new ErrorResponse(
                LocalDateTime.now(),
                exception.getMessage(),
                "Erro de negócio",
                HttpStatus.BAD_REQUEST.value()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> tratarRecursoNaoEncontrado(
            ResourceNotFoundException exception
    ) {
        ErrorResponse erro = new ErrorResponse(
                LocalDateTime.now(),
                exception.getMessage(),
                "Recurso não encontrado",
                HttpStatus.NOT_FOUND.value()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ErrorResponse> tratarServicoExternoIndisponivel(
            ExternalServiceException exception
    ) {
        ErrorResponse erro = new ErrorResponse(
                LocalDateTime.now(),
                exception.getMessage(),
                "Serviço externo indisponível",
                HttpStatus.SERVICE_UNAVAILABLE.value()
        );

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> tratarErroDeValidacao(
            MethodArgumentNotValidException exception
    ) {
        String mensagem = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Dados inválidos");

        ErrorResponse erro = new ErrorResponse(
                LocalDateTime.now(),
                mensagem,
                "Erro de validação",
                HttpStatus.BAD_REQUEST.value()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> tratarErroDeConversao(
            MethodArgumentTypeMismatchException exception
    ) {
        String mensagem;

        if ("dataInicio".equals(exception.getName())
                || "dataFim".equals(exception.getName())) {

            mensagem = "Formato de data inválido. Utilize o padrão yyyy-MM-dd'T'HH:mm:ss";

        } else {
            mensagem = "Valor inválido para o parâmetro: " + exception.getName();
        }

        ErrorResponse erro = new ErrorResponse(
                LocalDateTime.now(),
                mensagem,
                "Erro de conversão",
                HttpStatus.BAD_REQUEST.value()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    public record ErrorResponse(
            LocalDateTime timestamp,
            String message,
            String error,
            int status
    ) {
    }
}