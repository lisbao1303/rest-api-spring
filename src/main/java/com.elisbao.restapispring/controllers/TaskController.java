package com.elisbao.restapispring.controllers;

import com.elisbao.restapispring.models.Task;
import com.elisbao.restapispring.models.projection.TaskProjection;
import com.elisbao.restapispring.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/task")
@Validated
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Operation(
            summary = "Find task by ID",
            description = "Retrieve a task by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task found", content = @Content(schema = @Schema(implementation = Task.class))),
                    @ApiResponse(responseCode = "404", description = "Task not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(@Parameter(description = "ID of the task to be found") @PathVariable Long id) {
        Task obj = this.taskService.findById(id);
        return ResponseEntity.ok(obj);
    }

    @Operation(
            summary = "Find all tasks by user",
            description = "Retrieve all tasks associated with the current user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tasks found", content = @Content(schema = @Schema(implementation = TaskProjection.class))),
                    @ApiResponse(responseCode = "404", description = "Tasks not found")
            }
    )
    @GetMapping("/user")
    public ResponseEntity<List<TaskProjection>> findAllByUser() {
        List<TaskProjection> objs = this.taskService.findAllByUser();
        return ResponseEntity.ok().body(objs);
    }

    @Operation(
            summary = "Create a new task",
            description = "Create a new task for the current user.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Task created", content = @Content(schema = @Schema(implementation = Task.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    @PostMapping
    public ResponseEntity<Void> create(
            @Parameter(description = "Task object to be created") @Valid @RequestBody Task obj) {
        this.taskService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @Operation(
            summary = "Update an existing task",
            description = "Update the details of an existing task by its ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Task updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Task not found")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @Parameter(description = "Updated task object") @Valid @RequestBody Task obj,
            @Parameter(description = "ID of the task to be updated") @PathVariable Long id) {
        obj.setId(id);
        this.taskService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Delete a task",
            description = "Delete a task by its ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Task not found")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "ID of the task to be deleted") @PathVariable Long id) {
        this.taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
