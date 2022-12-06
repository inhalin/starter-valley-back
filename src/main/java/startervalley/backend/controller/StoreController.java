package startervalley.backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import startervalley.backend.dto.request.StoreQueryParameter;
import startervalley.backend.dto.request.StoreRequestDto;
import startervalley.backend.dto.response.*;
import startervalley.backend.service.StoreService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/schelins")
public class StoreController {

    private final StoreService storeService;

    @GetMapping
    public BaseResponseDto<List<StoreResponseDto>> getStoreList(@Valid @ModelAttribute StoreQueryParameter queryParameter) {
        return storeService.findAllStore(queryParameter);
    }

    @GetMapping("/like")
    public BaseResponseDto<List<StoreResponseDto>> getMyStoreLikeList() {
        return storeService.findMyStoreLikeList();
    }

    @GetMapping("/categories")
    public BaseResponseDto<List<String>> getCategoryList() {
        return storeService.findAllCategory();
    }

    @PostMapping
    public BaseResponseDto<Long> createStore(@RequestPart(value = "storeRequestDto") StoreRequestDto storeRequestDto,
                                             @RequestPart(value = "uploadFiles", required = false) List<MultipartFile> uploadFiles) {
        return storeService.createStore(storeRequestDto, uploadFiles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreDetailDto> getStore(@PathVariable Long id) {
        StoreDetailDto dto = storeService.findStore(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/{id}/comments")
    public BaseResponseDto<List<CommentResponseDto>> getStoreComments(@PathVariable Long id) {
        return storeService.findStoreComments(id);
    }

    @PutMapping("/{id}")
    public BaseResponseDto<Void> updateStore(@PathVariable Long id, @RequestBody StoreRequestDto storeRequestDto) {
        return storeService.modify(id, storeRequestDto);
    }

    @DeleteMapping("/{id}")
    public BaseResponseDto<Void> requestForRemoveStore(@PathVariable Long id) {
        return null;
    }

    @PostMapping("/{id}/like")
    public BaseResponseDto<Void> likeStore(@PathVariable Long id) {
        return storeService.likeStore(id);
    }

    @GetMapping("/recommend")
    public BaseResponseDto<List<StoreResponseDto>> getRecommendedStoreList() {
        return storeService.findRecommendStoreList();
    }

}
