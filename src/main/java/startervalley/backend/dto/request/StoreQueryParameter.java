package startervalley.backend.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Min;

@Getter
@Setter
public class StoreQueryParameter {

    @Min(value = 1, message = "페이지는 1부터 시작입니다.")
    private int page;

    @Range(min = 5, max = 20, message = "사이즈는 5~20으로 지정해주세요.")
    private int size;

    private String category;
    private String orderBy = "asc";

    public StoreQueryParameter() {
        this.page = 1;
        this.size = 10;
    }

    public Pageable getPageable() {
        return PageRequest.of(page - 1, size);
    }

    public Pageable getPageable(Sort sort) {
        return PageRequest.of(page - 1, size, sort);
    }
}
