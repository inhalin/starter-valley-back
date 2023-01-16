package startervalley.backend.repository.generation;

import startervalley.backend.entity.Generation;

import java.util.List;

public interface GenerationRepositoryCustom {
    List<Generation> findAllGenerations();
}
