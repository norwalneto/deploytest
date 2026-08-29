package nwl.projetos.deploytest.domain.dto;

import nwl.projetos.deploytest.domain.enums.TaskStatus;
import nwl.projetos.deploytest.domain.model.Task;

import java.time.LocalDateTime;

public record TaskResponseDTO(
        Long id,
        String titulo,
        String descricao,
        LocalDateTime dataLimite,
        TaskStatus status
) {

    public TaskResponseDTO(Task task) {
        this(
                task.getId(),
                task.getTitulo(),
                task.getDescricao(),
                task.getDataLimite(),
                task.getStatus()
        );
    }
}
