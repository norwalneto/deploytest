package nwl.projetos.deploytest.controller;

import jakarta.validation.Valid;
import nwl.projetos.deploytest.domain.dto.TaskRequestDTO;
import nwl.projetos.deploytest.domain.dto.TaskResponseDTO;
import nwl.projetos.deploytest.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> criar(
            @Valid @RequestBody TaskRequestDTO request
    ) {
        TaskResponseDTO response = taskService.criar(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> listarTodos() {
        return ResponseEntity.ok(taskService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(taskService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequestDTO request
    ) {
        return ResponseEntity.ok(taskService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        taskService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
