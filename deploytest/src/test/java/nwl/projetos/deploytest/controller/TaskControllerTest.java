package nwl.projetos.deploytest.controller;

import nwl.projetos.deploytest.domain.dto.TaskRequestDTO;
import nwl.projetos.deploytest.domain.dto.TaskResponseDTO;
import nwl.projetos.deploytest.domain.enums.TaskStatus;
import nwl.projetos.deploytest.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    private TaskController taskController;
    private TaskRequestDTO request;
    private TaskResponseDTO response;

    @BeforeEach
    void setUp() {
        taskController = new TaskController(taskService);
        LocalDateTime dataLimite = LocalDateTime.now().plusDays(1);
        request = new TaskRequestDTO("Estudar", "Estudar JUnit", dataLimite, TaskStatus.PENDENTE);
        response = new TaskResponseDTO(1L, "Estudar", "Estudar JUnit", dataLimite, TaskStatus.PENDENTE);
    }

    @Test
    void deveCriarUmaTarefa() {
        when(taskService.criar(request)).thenReturn(response);

        ResponseEntity<TaskResponseDTO> resultado = taskController.criar(request);

        assertEquals(HttpStatus.CREATED, resultado.getStatusCode());
        assertEquals(response, resultado.getBody());
        verify(taskService).criar(request);
    }

    @Test
    void deveListarTodasAsTarefas() {
        when(taskService.listarTodos()).thenReturn(List.of(response));

        ResponseEntity<List<TaskResponseDTO>> resultado = taskController.listarTodos();

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(List.of(response), resultado.getBody());
        verify(taskService).listarTodos();
    }

    @Test
    void deveBuscarUmaTarefaPorId() {
        when(taskService.buscarPorId(1L)).thenReturn(response);

        ResponseEntity<TaskResponseDTO> resultado = taskController.buscarPorId(1L);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(response, resultado.getBody());
        verify(taskService).buscarPorId(1L);
    }

    @Test
    void deveAtualizarUmaTarefa() {
        when(taskService.atualizar(1L, request)).thenReturn(response);

        ResponseEntity<TaskResponseDTO> resultado = taskController.atualizar(1L, request);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(response, resultado.getBody());
        verify(taskService).atualizar(1L, request);
    }

    @Test
    void deveExcluirUmaTarefa() {
        ResponseEntity<Void> resultado = taskController.excluir(1L);

        assertEquals(HttpStatus.NO_CONTENT, resultado.getStatusCode());
        assertNull(resultado.getBody());
        verify(taskService).excluir(1L);
    }
}
