package startervalley.backend.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import startervalley.backend.admin.dto.team.*;
import startervalley.backend.admin.dto.user.UserSimpleDto;
import startervalley.backend.admin.service.TeamService;
import startervalley.backend.admin.service.UserService;
import startervalley.backend.dto.common.BasicResponse;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/{id}")
    public ResponseEntity<TeamResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(teamService.getOne(id));
    }

    @GetMapping
    public ResponseEntity<List<TeamListDto>> list() {
        return ResponseEntity.ok(teamService.list());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BasicResponse> update(@PathVariable Long id,
                                                @RequestBody TeamUpdateRequest request) {
        return ResponseEntity.ok(teamService.updateOne(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        teamService.deleteOne(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/users/{userId}")
    public ResponseEntity<BasicResponse> addTeammate(@PathVariable Long id,
                                                     @PathVariable Long userId,
                                                     @RequestBody Map<String, Boolean> leader){
        return ResponseEntity.ok(teamService.addUser(id, userId, leader.get("leader")));
    }

    @DeleteMapping("/{id}/users/{userId}")
    public ResponseEntity<Void> deleteTeammate(@PathVariable Long id, @PathVariable Long userId){
        teamService.deleteUser(id, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/users")
    public ResponseEntity<Void> deleteAllTeammates(@PathVariable Long id) {
        teamService.deleteAllUsers(id);
        return ResponseEntity.noContent().build();
    }
}
