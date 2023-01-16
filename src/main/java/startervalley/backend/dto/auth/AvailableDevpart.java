package startervalley.backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import startervalley.backend.entity.Devpart;

@Getter
@AllArgsConstructor
public class AvailableDevpart {
    private Long id;
    private String name;
    private String koname;

    public static AvailableDevpart mapToResponse(Devpart devpart) {
        return new AvailableDevpart(devpart.getId(), devpart.getName(), devpart.getKoname());
    }
}
