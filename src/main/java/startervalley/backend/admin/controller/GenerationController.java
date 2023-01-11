package startervalley.backend.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import startervalley.backend.admin.dto.generation.GenerationRequest;
import startervalley.backend.admin.dto.generation.GenerationResponse;
import startervalley.backend.admin.dto.generation.GenerationUpdateRequest;
import startervalley.backend.admin.service.GenerationService;
import startervalley.backend.dto.common.BasicResponse;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/generations")
public class GenerationController {

    private final GenerationService generationService;

    @PostMapping
    public ResponseEntity<BasicResponse> create(@Valid @RequestBody GenerationRequest request) {
        return ResponseEntity.ok(generationService.createOne(request));
    }

    @GetMapping
    public ResponseEntity<List<GenerationResponse>> list() {
        return ResponseEntity.ok(generationService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenerationResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(generationService.getOne(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BasicResponse> update(@PathVariable Long id,
                                                @Valid @RequestBody GenerationUpdateRequest request) {
        return ResponseEntity.ok(generationService.updateOne(id, request));
    }
}
