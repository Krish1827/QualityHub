package com.qualityhub.controller;

import com.qualityhub.model.Task;
import com.qualityhub.repository.TaskRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "http://localhost:5173")
public class TaskController {

    private final TaskRepository repository;

    public TaskController(TaskRepository repository) {
        this.repository = repository;
    }

    public record CreateTaskRequest(
            @NotBlank @Size(max = 120) String title,
            @Size(max = 1000) String description,
            String priority
    ) {}

    public record UpdateStatusRequest(
            @NotBlank String status
    ) {}

    @GetMapping
    public List<Task> getTasks(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "") String status) {
        return repository.findAll(search, status);
    }

    @GetMapping("/{id}")
    public Task getTask(@PathVariable long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Task not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(@Valid @RequestBody CreateTaskRequest request) {
        String priority = request.priority() == null
                ? "MEDIUM" : request.priority().toUpperCase();

        if (!List.of("LOW", "MEDIUM", "HIGH").contains(priority)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid priority");
        }

        return repository.create(
                request.title().trim(),
                request.description() == null ? "" : request.description(),
                priority
        );
    }

    @PatchMapping("/{id}/status")
    public Task updateStatus(
            @PathVariable long id,
            @Valid @RequestBody UpdateStatusRequest request) {

        String status = request.status().toUpperCase();

        if (!List.of("TODO", "IN_PROGRESS", "DONE").contains(status)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid status");
        }

        return repository.updateStatus(id, status)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Task not found"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable long id) {
        if (!repository.delete(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Task not found");
        }
    }
}
