package startervalley.backend.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import startervalley.backend.admin.dto.generation.*;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<BasicResponse> delete(@PathVariable Long id) {
        generationService.deleteOne(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{generationId}/devparts")
    public ResponseEntity<BasicResponse> updateDevpart(@PathVariable Long generationId,
                                                       @RequestBody DevpartDto devpart) {
        return ResponseEntity.ok(generationService.updateDevpart(generationId, devpart));
    }

    @DeleteMapping("/{generationId}/devparts")
    public ResponseEntity<BasicResponse> deleteDevpart(@PathVariable Long generationId,
                                                       @RequestBody DevpartDto devpart) {
        generationService.deleteDevpart(generationId, devpart);
        return ResponseEntity.noContent().build();
    }
}
