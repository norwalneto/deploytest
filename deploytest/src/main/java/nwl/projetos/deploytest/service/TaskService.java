package nwl.projetos.deploytest.service;

import nwl.projetos.deploytest.domain.dto.TaskRequestDTO;
import nwl.projetos.deploytest.domain.dto.TaskResponseDTO;

import java.util.List;

public interface TaskService {

    TaskResponseDTO criar(TaskRequestDTO request);

    TaskResponseDTO buscarPorId(Long id);

    List<TaskResponseDTO> listarTodos();

    TaskResponseDTO atualizar(Long id, TaskRequestDTO request);

    void excluir(Long id);
}
