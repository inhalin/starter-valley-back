package startervalley.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Embeddable
public class TeamUserId implements Serializable {
    private Long teamId;
    private Long userId;
}
