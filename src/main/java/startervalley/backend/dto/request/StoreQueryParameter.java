package startervalley.backend.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StoreQueryParameter {

    private String category;

    private String orderBy = "asc";
}
