package startervalley.backend.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class StoreRequestDto {

    private String name;

    private String address;

    private String description;

    private String url;

    private String category;

    private List<String> tagList;
}
