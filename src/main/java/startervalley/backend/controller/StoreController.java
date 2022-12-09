package startervalley.backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import startervalley.backend.dto.request.StoreQueryParameter;
import startervalley.backend.dto.request.StoreRequestDto;
import startervalley.backend.dto.response.*;
import startervalley.backend.security.auth.CustomUserDetails;
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
    public ResponseEntity<List<StoreResponseDto>> getStoreList(@AuthenticationPrincipal CustomUserDetails userDetails,
                                               @Valid @ModelAttribute StoreQueryParameter queryParameter) {
        Long userId = userDetails.getId();
        List<StoreResponseDto> result = storeService.findAllStores(userId, queryParameter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/like")
    public ResponseEntity<List<StoreResponseDto>> getMyStoreLikeList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        List<StoreResponseDto> result = storeService.findMyStoreLikeList(userId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategoryList() {
        List<String> result = storeService.findAllCategory();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BaseResponseDto> createStore(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                       @RequestPart(value = "content") StoreRequestDto storeRequestDto,
                                                       @RequestPart(value = "uploadFiles", required = false) List<MultipartFile> uploadFiles) {
        Long userId = userDetails.getId();
        Long result = storeService.createStore(userId, storeRequestDto, uploadFiles);
        BaseResponseDto dto = new BaseResponseDto(result);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreDetailDto> getStore(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                   @PathVariable Long id) {
        Long userId = userDetails.getId();
        StoreDetailDto dto = storeService.findStore(userId, id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentResponseDto>> getStoreComments(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                     @PathVariable Long id) {
        Long userId = userDetails.getId();
        List<CommentResponseDto> result = storeService.findStoreComments(userId, id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public void updateStore(@PathVariable Long id, @RequestBody StoreRequestDto storeRequestDto) {
        storeService.modify(id, storeRequestDto);
    }

    @DeleteMapping("/{id}")
    public void requestForRemoveStore(@PathVariable Long id) {

    }

    @PostMapping("/{id}/like")
    public ResponseEntity<BaseResponseDto> likeStore(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                     @PathVariable Long id) {
        Long userId = userDetails.getId();
        String result = storeService.likeStore(userId, id);
        BaseResponseDto dto = new BaseResponseDto(result);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/recommend")
    public List<StoreResponseDto> getRecommendedStoreList() {
        return storeService.findRecommendStoreList();
    }

}