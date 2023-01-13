package startervalley.backend.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import startervalley.backend.admin.dto.team.TeamRequest;
import startervalley.backend.admin.dto.user.UserSimpleDto;
import startervalley.backend.admin.service.TeamService;
import startervalley.backend.admin.service.UserService;
import startervalley.backend.dto.common.BasicResponse;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/teams")
public class TeamController {

    private final TeamService teamService;
    private final UserService userService;

    @GetMapping("/available-users")
    public ResponseEntity<List<UserSimpleDto>> listAvailable(@RequestParam("generation") Long generationId) {
        return ResponseEntity.ok(userService.listAvailableForTeamByGenerationId(generationId));
    }

    @PostMapping
    public ResponseEntity<BasicResponse> create(@Valid @RequestBody TeamRequest request) {
        return ResponseEntity.ok(teamService.createOne(request));
    }

}
