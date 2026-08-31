package nwl.projetos.deploytest.domain.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import nwl.projetos.deploytest.domain.enums.TaskStatus;

import java.time.LocalDateTime;

public record TaskRequestDTO(

        @NotBlank(message = "O título é obrigatório")
        @Size(max = 150, message = "O título deve ter no máximo 150 caracteres")
        String titulo,

        @NotBlank(message = "A descrição é obrigatória")
        @Size(max = 1000, message = "A descrição deve ter no máximo 1000 caracteres")
        String descricao,

        @FutureOrPresent(message = "A data limite não pode estar no passado")
        LocalDateTime dataLimite,

        TaskStatus status
) {
    public TaskRequestDTO {
        if (status == null) {
            status = TaskStatus.PENDENTE;
        }
    }
}
