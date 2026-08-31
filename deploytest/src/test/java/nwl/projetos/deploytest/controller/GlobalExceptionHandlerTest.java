package nwl.projetos.deploytest.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import nwl.projetos.deploytest.domain.dto.ApiErrorDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private HttpServletRequest request;

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        when(request.getRequestURI()).thenReturn("/api/v1/tasks/99");
    }

    @Test
    void deveRetornarNotFoundParaTarefaInexistente() {
        EntityNotFoundException exception =
                new EntityNotFoundException("Tarefa não encontrada com o id: 99");

        ResponseEntity<ApiErrorDTO> resposta =
                handler.tratarEntidadeNaoEncontrada(exception, request);

        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
        assertNotNull(resposta.getBody());
        assertEquals(404, resposta.getBody().status());
        assertEquals(exception.getMessage(), resposta.getBody().message());
        assertEquals("/api/v1/tasks/99", resposta.getBody().path());
    }

    @Test
    void deveRetornarOsCamposComErroDeValidacao() {
        BindingResult bindingResult = mock(BindingResult.class);
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        FieldError fieldError = new FieldError(
                "taskRequestDTO",
                "titulo",
                "O título é obrigatório"
        );

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<ApiErrorDTO> resposta = handler.tratarErroDeValidacao(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
        assertNotNull(resposta.getBody());
        assertEquals("O título é obrigatório", resposta.getBody().fields().get("titulo"));
    }
}
