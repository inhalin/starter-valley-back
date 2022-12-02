package startervalley.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import startervalley.backend.dto.request.StoreQueryParameter;
import startervalley.backend.dto.request.StoreRequestDto;
import startervalley.backend.dto.response.BaseResponseDto;
import startervalley.backend.dto.response.CommentResponseDto;
import startervalley.backend.dto.response.StoreResponseDto;
import startervalley.backend.entity.*;
import startervalley.backend.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static startervalley.backend.constant.ResponseMessage.*;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StoreService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;
    private final UserLikeStoreRepository userLikeStoreRepository;
    private final CommentRepository commentRepository;
    private final static List<StoreResponseDto> recommendStoreList = new ArrayList<>();

    public BaseResponseDto<List<StoreResponseDto>> findAllStore(StoreQueryParameter queryParameter) {
        log.info("queryParameter {}", queryParameter);
        User user = userRepository.findById(1L).orElseThrow();
        List<Store> storeList = storeRepository.findAll();
        List<StoreResponseDto> result = storeList.stream().map(store -> {
            long likeCount = userLikeStoreRepository.countByStore(store);
            boolean myLikeStatus = userLikeStoreRepository.existsByUserAndStore(user, store);
            return StoreResponseDto.builder()
                    .id(store.getId())
                    .name(store.getName())
                    .description(store.getDescription())
                    .address(store.getAddress())
                    .likeCount(likeCount)
                    .myLikeStatus(myLikeStatus)
                    .category(store.getCategory().getName())
                    .build();
        }).toList();

        return new BaseResponseDto<>(STORE_LIST.toString(), result);
    }

    @Transactional
    public BaseResponseDto<Long> createStore(StoreRequestDto storeRequestDto) {
        Category category = getCategory(storeRequestDto.getCategory());
        Store store = Store.builder()
                .name(storeRequestDto.getName())
                .address(storeRequestDto.getAddress())
                .description(storeRequestDto.getDescription())
                .category(category)
                .build();
        storeRepository.save(store);
        return new BaseResponseDto<>(STORE_CREATE.toString(), store.getId());
    }

    public BaseResponseDto<StoreResponseDto> findStore(Long id) {
        User user = userRepository.findById(1L).orElseThrow();
        Store store = getStore(id);
        long likeCount = userLikeStoreRepository.countByStore(store);
        boolean myLikeStatus = userLikeStoreRepository.existsByUserAndStore(user, store);
        StoreResponseDto dto = StoreResponseDto.builder()
                .id(id)
                .name(store.getName())
                .address(store.getAddress())
                .description(store.getDescription())
                .likeCount(likeCount)
                .myLikeStatus(myLikeStatus)
                .category(store.getCategory().getName())
                .build();
        return new BaseResponseDto<>(STORE.toString(), dto);
    }

    @Transactional
    public BaseResponseDto<Void> modify(Long id, StoreRequestDto storeRequestDto) {
        Store store = getStore(id);
        store.update(storeRequestDto);
        return new BaseResponseDto<>(STORE_UPDATE.toString(), null);
    }

    public BaseResponseDto<List<String>> findAllCategory() {
        List<Category> categoryList = categoryRepository.findAll();
        List<String> result = categoryList.stream()
                .map(Category::getName)
                .toList();
        return new BaseResponseDto<>(CATEGORY_LIST.toString(), result);
    }

    public BaseResponseDto<List<StoreResponseDto>> findMyStoreLikeList() {
        User user = userRepository.findById(1L).orElseThrow();
        List<UserLikeStore> likeStoreList = userLikeStoreRepository.findAllByUser(user);
        List<StoreResponseDto> result = likeStoreList.stream().map(likeStore -> {
            Store store = likeStore.getStore();
            long likeCount = userLikeStoreRepository.countByStore(store);
            boolean myLikeStatus = userLikeStoreRepository.existsByUserAndStore(user, store);
            return StoreResponseDto.builder()
                    .id(store.getId())
                    .name(store.getName())
                    .description(store.getDescription())
                    .address(store.getAddress())
                    .likeCount(likeCount)
                    .myLikeStatus(myLikeStatus)
                    .category(store.getCategory().getName())
                    .build();
        }).toList();
        return new BaseResponseDto<>(STORE_LIST.toString(), result);
    }

    @Transactional
    public BaseResponseDto<Void> likeStore(Long id) {
        User user = userRepository.findById(1L).orElseThrow();
        Store store = getStore(id);
        Optional<UserLikeStore> optional = userLikeStoreRepository.findByUserAndStore(user, store);

        String message = null;

        if (optional.isPresent()) {
            UserLikeStore likeStore = optional.get();
            userLikeStoreRepository.delete(likeStore);
            message = STORE_DISLIKE.toString();
        } else {
            UserLikeStore userLikeStore = UserLikeStore.builder().user(user).store(store).build();
            userLikeStoreRepository.save(userLikeStore);
            message = STORE_LIKE.toString();
        }
        return new BaseResponseDto<>(message, null);
    }

    public BaseResponseDto<List<CommentResponseDto>> findStoreComments(Long id) {
        User user = userRepository.findById(1L).orElseThrow();
        Store store = getStore(id);
        List<Comment> comments = commentRepository.findAllByStore(store);

        List<CommentResponseDto> result = comments.stream().map(comment -> {
            boolean isOwn = Objects.equals(comment.getUser().getId(), user.getId());
            return CommentResponseDto   .builder()
                    .id(comment.getId())
                    .description(comment.getDescription())
                    .isOwn(isOwn)
                    .createdDate(comment.getCreatedDate())
                    .modifiedDate(comment.getModifiedDate())
                    .build();
        }).toList();
        return new BaseResponseDto<>(COMMENT_LIST.toString(), result);
    }

    public BaseResponseDto<List<StoreResponseDto>> findRecommendStoreList() {
        return new BaseResponseDto<>(STORE_RECOMMEND.toString(), recommendStoreList);
    }

    @Transactional
    public void updateRecommendStoreList() {
        recommendStoreList.clear();
        List<Store> randomList = storeRepository.findAllRandom();
        List<StoreResponseDto> result = randomList.stream().map(store -> StoreResponseDto.builder()
                .id(store.getId())
                .name(store.getName())
                .address(store.getAddress())
                .description(store.getDescription())
                .category(store.getCategory().getName())
                .build()).toList();
        recommendStoreList.addAll(result);
    }

    private Store getStore(Long id) {
        Optional<Store> optional = storeRepository.findById(id);
        if (optional.isEmpty()) {
            throw new RuntimeException();
        }
        return optional.get();
    }

    private Category getCategory(String name) {
        Optional<Category> optional = categoryRepository.findByName(name);
        if (optional.isEmpty()) {
            throw new RuntimeException();
        }
        return optional.get();
    }
}
