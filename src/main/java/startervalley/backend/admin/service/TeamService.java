package startervalley.backend.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import startervalley.backend.admin.dto.team.TeamListDto;
import startervalley.backend.admin.dto.team.TeamRequest;
import startervalley.backend.admin.dto.team.TeamResponse;
import startervalley.backend.admin.dto.team.TeamUserDto;
import startervalley.backend.dto.common.BasicResponse;
import startervalley.backend.entity.Generation;
import startervalley.backend.entity.Team;
import startervalley.backend.entity.User;
import startervalley.backend.exception.ResourceNotFoundException;
import startervalley.backend.repository.generation.GenerationRepository;
import startervalley.backend.repository.team.TeamRepository;
import startervalley.backend.repository.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final GenerationRepository generationRepository;

    public BasicResponse createOne(TeamRequest request) {

        Generation generation = getTeamOrElseThrow(request);

        Team team = Team.builder()
                .name(request.getName())
                .description(request.getDescription())
                .notionUrl(request.getNotionUrl())
                .releaseUrl(request.getReleaseUrl())
                .generation(generation)
                .build();
        teamRepository.save(team);

        generation.setTeam(team);

        User user = userRepository.findById(request.getLeader())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getLeader().toString()));
        user.setTeamInfo(team, true);

        request.getTeammates().stream()
                .map(userId -> userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString())))
                .forEach(teammate -> teammate.setTeamInfo(team, false));

        return BasicResponse.of(team.getId(), "팀이 생성되었습니다.");
    }

    @Transactional(readOnly = true)
    public TeamResponse getOne(Long id) {

        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", id.toString()));

        List<TeamUserDto> teamUserDtos = userRepository.findAllByTeamId(id).stream()
                .map(TeamUserDto::mapToDto)
                .toList();

        return TeamResponse.mapToResponse(team, teamUserDtos);
    }

    @Transactional(readOnly = true)
    public List<TeamListDto> list() {
        return generationRepository.findAllGenerations().stream()
                .map(TeamListDto::mapToDto)
                .toList();
    }

    private Generation getTeamOrElseThrow(TeamRequest request) {
        return generationRepository.findById(request.getGeneration())
                .orElseThrow(() -> new ResourceNotFoundException("Generation", "id", request.getGeneration().toString()));
    }
}
