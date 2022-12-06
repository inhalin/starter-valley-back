package startervalley.backend.dto.user;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UserCardListDto {
    private List<UserCardDto> me;
    private List<UserCardDto> list;
}
