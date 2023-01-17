package startervalley.backend.repository.devpart;

import startervalley.backend.entity.Devpart;

import java.util.List;

public interface DevpartRepositoryCustom {

    List<Devpart> findAllByGenerationId(Long generationId);
}
