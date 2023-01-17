package startervalley.backend.admin.dto.generation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import startervalley.backend.entity.Devpart;
import startervalley.backend.exception.ResourceNotValidException;

@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class DevpartDto {
    private String name;
    private String koname;

    public static DevpartDto validate(DevpartDto devpart) {
        if (devpart.getName() == null) {
            throw new ResourceNotValidException("파트의 영문명을 입력해주세요.");
        }

        if (!devpart.getName().matches("^\\p{Alpha}[a-zA-Z_]+$")) {
            throw new ResourceNotValidException("파트의 영문명은 영문자 및 언어바(_)만 입력 가능합니다. 첫글잔는 영문자만 가능합니다.");
        }

        if (devpart.getKoname() == null) {
            throw new ResourceNotValidException("파트의 한글명을 입력해주세요.");
        }

        return devpart;
    }

    public static DevpartDto mapToDto(Devpart devpart) {
        return DevpartDto.of(devpart.getName(), devpart.getKoname());
    }
}
