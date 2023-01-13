package startervalley.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class TeamUser extends BaseTimeEntity {

    @EmbeddedId
    private TeamUserId id;

    private boolean leader;
}
