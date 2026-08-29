package nwl.projetos.deploytest.service.impl;

import jakarta.persistence.EntityNotFoundException;
import nwl.projetos.deploytest.domain.dto.TaskRequestDTO;
import nwl.projetos.deploytest.domain.dto.TaskResponseDTO;
import nwl.projetos.deploytest.domain.model.Task;
import nwl.projetos.deploytest.domain.repository.TaskRepository;
import nwl.projetos.deploytest.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    @Transactional
    public TaskResponseDTO criar(TaskRequestDTO request) {
        Task task = new Task();
        atualizarDados(task, request);
        return new TaskResponseDTO(taskRepository.save(task));
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponseDTO buscarPorId(Long id) {
        return new TaskResponseDTO(buscarEntidadePorId(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponseDTO> listarTodos() {
        return taskRepository.findAll()
                .stream()
                .map(TaskResponseDTO::new)
                .toList();
    }

    @Override
    @Transactional
    public TaskResponseDTO atualizar(Long id, TaskRequestDTO request) {
        Task task = buscarEntidadePorId(id);
        atualizarDados(task, request);
        return new TaskResponseDTO(taskRepository.save(task));
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        taskRepository.delete(buscarEntidadePorId(id));
    }

    private Task buscarEntidadePorId(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa não encontrada com o id: " + id));
    }

    private void atualizarDados(Task task, TaskRequestDTO request) {
        task.setTitulo(request.titulo());
        task.setDescricao(request.descricao());
        task.setDataLimite(request.dataLimite());
        task.setStatus(request.status());
    }
}
