package startervalley.backend.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import startervalley.backend.admin.dto.team.*;
import startervalley.backend.dto.common.BasicResponse;
import startervalley.backend.entity.Generation;
import startervalley.backend.entity.Team;
import startervalley.backend.entity.User;
import startervalley.backend.exception.ResourceNotFoundException;
import startervalley.backend.exception.ResourceNotValidException;
import startervalley.backend.repository.generation.GenerationRepository;
import startervalley.backend.repository.team.TeamRepository;
import startervalley.backend.repository.user.UserRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final GenerationRepository generationRepository;

    public BasicResponse createOne(TeamRequest request) {

        Generation generation = getGenerationOrElseThrow(request);

        Team team = Team.builder()
                .name(request.getName())
                .description(request.getDescription())
                .notionUrl(request.getNotionUrl())
                .releaseUrl(request.getReleaseUrl())
                .generation(generation)
                .build();
        teamRepository.save(team);

        generation.setTeam(team);

        User user = getUserOrElseThrow(request.getLeader());
        user.setTeamInfo(team, true);

        request.getTeammates().stream()
                .map(this::getUserOrElseThrow)
                .forEach(teammate -> teammate.setTeamInfo(team, false));

        return BasicResponse.of(team.getId(), "팀이 생성되었습니다.");
    }

    @Transactional(readOnly = true)
    public TeamResponse getOne(Long id) {

        Team team = getTeamOrElseThrow(id);

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

    public BasicResponse updateOne(Long id, TeamUpdateRequest request) {

        Team team = getTeamOrElseThrow(id);

        team.update(request.getName() != null ? request.getName() : team.getName(),
                request.getDescription() != null ? request.getDescription() : team.getDescription(),
                request.getNotionUrl() != null ? request.getNotionUrl() : team.getNotionUrl(),
                request.getReleaseUrl() != null ? request.getReleaseUrl() : team.getReleaseUrl());

        return BasicResponse.of(null, "팀 정보가 수정되었습니다.");
    }

    public BasicResponse addUser(Long teamId, Long userId, boolean isLeader) {

        User user = getUserOrElseThrow(userId);
        Team team = getTeamOrElseThrow(teamId);

        Optional<User> leader = team.getUsers().stream().filter(User::getIsLeader).findFirst();

        if (isLeader && leader.isPresent()) {
            throw new ResourceNotValidException("팀장은 한명만 지정 가능합니다.");
        }

        user.setTeamInfo(team, isLeader);

        String message = user.getName() + "님을 " + team.getName() + " 팀의 " + (isLeader ? "팀장" : "팀원") + "으로 추가하였습니다.";

        log.info("add team user with id {}: message = {}", user.getId(), message);

        return BasicResponse.of(user.getId(), message);
    }

    public BasicResponse deleteUser(Long id, Long userId) {

        User user = getUserOrElseThrow(userId);
        Team team = getTeamOrElseThrow(id);

        user.removeTeamInfo();

        String message = user.getName() + "님을 " + team.getName() + " 팀에서 제거하였습니다.";

        log.info("delete team user with id {}: message = {}", user.getId(), message);

        return BasicResponse.of(user.getId(), message);
    }

    private Generation getGenerationOrElseThrow(TeamRequest request) {
        return generationRepository.findById(request.getGeneration())
                .orElseThrow(() -> new ResourceNotFoundException("Generation", "id", request.getGeneration().toString()));
    }

    private Team getTeamOrElseThrow(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", id.toString()));
    }

    private User getUserOrElseThrow(Long request) {
        return userRepository.findById(request)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.toString()));
    }
}
