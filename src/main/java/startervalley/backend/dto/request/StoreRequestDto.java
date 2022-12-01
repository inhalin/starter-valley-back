package startervalley.backend.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StoreRequestDto {

    private String name;

    private String address;

    private String description;

    private String category;
}
