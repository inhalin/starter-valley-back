package startervalley.backend.repository.user;

import startervalley.backend.entity.User;

import java.util.List;

public interface UserRepositoryCustom {

    List<User> listAvailableForTeamByGenerationId(Long generationId);
}
