package startervalley.backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import startervalley.backend.dto.request.CommentRequestDto;
import startervalley.backend.dto.request.PageRequestDto;
import startervalley.backend.dto.store.StoreRequestDto;
import startervalley.backend.dto.store.StoreUpdateDto;
import startervalley.backend.dto.response.*;
import startervalley.backend.dto.store.StoreDetailDto;
import startervalley.backend.dto.store.StoreResponseDto;
import startervalley.backend.entity.Comment;
import startervalley.backend.entity.Store;
import startervalley.backend.security.auth.CustomUserDetails;
import startervalley.backend.service.StoreService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/schelins")
public class StoreController {

    private final StoreService storeService;

    @GetMapping
    public ResponseEntity<PageResultDTO<Store, StoreResponseDto>> getStoreList(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                               @Valid @ModelAttribute PageRequestDto pageRequestDto) {
        Long userId = userDetails.getId();
        PageResultDTO<Store, StoreResponseDto> result = storeService.findAllStores(userId, pageRequestDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/like")
    public ResponseEntity<List<StoreResponseDto>> getMyStoreLikeList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        List<StoreResponseDto> result = storeService.findMyStoreLikeList(userId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponseDto>> getCategoryList() {
        List<CategoryResponseDto> result = storeService.findAllCategory();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreDetailDto> getStore(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                   @PathVariable Long id) {
        Long userId = userDetails.getId();
        StoreDetailDto dto = storeService.findStore(userId, id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
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

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateStore(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @PathVariable("id") Long commentId,
                                            @RequestPart(value = "content") StoreUpdateDto storeUpdateDto,
                                            @RequestPart(value = "uploadFiles", required = false) List<MultipartFile> uploadFiles) throws IOException {
        Long userId = userDetails.getId();
        storeService.updateStore(userId, commentId, storeUpdateDto, uploadFiles);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteStore(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable("id") Long storeId) {
        Long userId = userDetails.getId();
        storeService.deleteStore(userId, storeId);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<BaseResponseDto> likeStore(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                     @PathVariable Long id) {
        Long userId = userDetails.getId();
        String result = storeService.likeStore(userId, id);
        BaseResponseDto dto = new BaseResponseDto(result);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<PageResultDTO<Comment, CommentResponseDto>> getStoreComments(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                       @PathVariable Long id,
                                                                                       @Valid @ModelAttribute PageRequestDto pageRequestDto) {
        Long userId = userDetails.getId();
        PageResultDTO<Comment, CommentResponseDto> result = storeService.findStoreComments(userId, id, pageRequestDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Long> createComment(@AuthenticationPrincipal CustomUserDetails userDetails,
                                              @PathVariable("id") Long storeId,
                                              @Valid @RequestBody CommentRequestDto commentRequestDto) {
        Long userId = userDetails.getId();
        Long result = storeService.createComment(userId, storeId, commentRequestDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<Void> updateComment(@AuthenticationPrincipal CustomUserDetails userDetails,
                                              @PathVariable("id") Long storeId,
                                              @Valid @RequestBody CommentRequestDto commentRequestDto) {
        Long userId = userDetails.getId();
        storeService.updateComment(userId, storeId, commentRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(@AuthenticationPrincipal CustomUserDetails userDetails,
                                              @PathVariable("id") Long commentId) {
        Long userId = userDetails.getId();
        storeService.deleteComment(userId, commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/recommend")
    public List<StoreResponseDto> getRecommendedStoreList() {
        return storeService.findRecommendStoreList();
    }

}