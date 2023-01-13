package startervalley.backend.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import startervalley.backend.admin.dto.team.TeamRequest;
import startervalley.backend.dto.common.BasicResponse;
import startervalley.backend.entity.Team;
import startervalley.backend.entity.TeamUser;
import startervalley.backend.entity.TeamUserId;
import startervalley.backend.repository.team.TeamRepository;
import startervalley.backend.repository.team.TeamUserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamUserRepository teamUserRepository;

    public BasicResponse createOne(TeamRequest request) {

        Team team = Team.builder()
                .name(request.getName())
                .description(request.getDescription())
                .notionUrl(request.getNotionUrl())
                .releaseUrl(request.getReleaseUrl())
                .build();
        teamRepository.save(team);

        teamUserRepository.save(TeamUser.of(TeamUserId.of(team.getId(), request.getLeader()), true));

        request.getTeammates()
                .forEach(teammate -> teamUserRepository.save(TeamUser.of(TeamUserId.of(team.getId(), teammate), false)));

        return BasicResponse.of(team.getId(), "팀이 생성되었습니다.");
    }
}
