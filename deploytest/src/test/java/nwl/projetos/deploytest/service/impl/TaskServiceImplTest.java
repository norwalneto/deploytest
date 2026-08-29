package nwl.projetos.deploytest.service.impl;

import jakarta.persistence.EntityNotFoundException;
import nwl.projetos.deploytest.domain.dto.TaskRequestDTO;
import nwl.projetos.deploytest.domain.dto.TaskResponseDTO;
import nwl.projetos.deploytest.domain.enums.TaskStatus;
import nwl.projetos.deploytest.domain.model.Task;
import nwl.projetos.deploytest.domain.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    private TaskServiceImpl taskService;
    private TaskRequestDTO request;
    private Task task;

    @BeforeEach
    void setUp() {
        taskService = new TaskServiceImpl(taskRepository);
        LocalDateTime dataLimite = LocalDateTime.now().plusDays(1);
        request = new TaskRequestDTO("Estudar", "Estudar JUnit", dataLimite, TaskStatus.PENDENTE);
        task = new Task(1L, "Estudar", "Estudar JUnit", dataLimite, TaskStatus.PENDENTE);
    }

    @Test
    void deveCriarUmaTarefa() {
        when(taskRepository.save(org.mockito.ArgumentMatchers.any(Task.class))).thenReturn(task);

        TaskResponseDTO resultado = taskService.criar(request);

        assertEquals(1L, resultado.id());
        assertEquals(request.titulo(), resultado.titulo());
        assertEquals(request.status(), resultado.status());
        verify(taskRepository).save(org.mockito.ArgumentMatchers.any(Task.class));
    }

    @Test
    void deveBuscarUmaTarefaPorId() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskResponseDTO resultado = taskService.buscarPorId(1L);

        assertEquals(1L, resultado.id());
        assertEquals(task.getTitulo(), resultado.titulo());
    }

    @Test
    void deveLancarExcecaoQuandoATarefaNaoExistir() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException excecao = assertThrows(
                EntityNotFoundException.class,
                () -> taskService.buscarPorId(99L)
        );

        assertEquals("Tarefa não encontrada com o id: 99", excecao.getMessage());
    }

    @Test
    void deveListarTodasAsTarefas() {
        when(taskRepository.findAll()).thenReturn(List.of(task));

        List<TaskResponseDTO> resultado = taskService.listarTodos();

        assertEquals(1, resultado.size());
        assertEquals(task.getId(), resultado.getFirst().id());
    }

    @Test
    void deveAtualizarUmaTarefa() {
        TaskRequestDTO atualizacao = new TaskRequestDTO(
                "Projeto",
                "Finalizar o projeto",
                LocalDateTime.now().plusDays(2),
                TaskStatus.EM_ANDAMENTO
        );
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        TaskResponseDTO resultado = taskService.atualizar(1L, atualizacao);

        assertEquals(atualizacao.titulo(), resultado.titulo());
        assertEquals(atualizacao.descricao(), resultado.descricao());
        assertEquals(TaskStatus.EM_ANDAMENTO, resultado.status());
        verify(taskRepository).save(task);
    }

    @Test
    void deveExcluirUmaTarefa() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.excluir(1L);

        verify(taskRepository).delete(task);
    }

    @Test
    void naoDeveExcluirQuandoATarefaNaoExistir() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.excluir(99L));

        verify(taskRepository, never()).delete(org.mockito.ArgumentMatchers.any(Task.class));
    }
}
