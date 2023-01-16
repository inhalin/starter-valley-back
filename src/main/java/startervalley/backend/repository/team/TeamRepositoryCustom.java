package startervalley.backend.repository.team;

public interface TeamRepositoryCustom {
    boolean existsByGenerationIdAndName(Long generationId, String name);
}
