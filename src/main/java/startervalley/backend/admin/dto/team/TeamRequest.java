package startervalley.backend.admin.dto.team;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class TeamRequest {

    @NotNull(message = "기수는 필수입니다.")
    private Long generation;

    @NotEmpty(message = "팀명을 입력해주세요.")
    private String name;

    private String notionUrl;
    private String releaseUrl;
    private String description;

    @NotNull(message = "리더를 선택해주세요.")
    private Long leader;

    @NotNull(message = "팀원을 선택해주세요.")
    private List<Long> teammates;
}
