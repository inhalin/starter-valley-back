package startervalley.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;
import startervalley.backend.dto.request.StoreQueryParameter;
import startervalley.backend.dto.request.StoreRequestDto;
import startervalley.backend.dto.response.*;
import startervalley.backend.entity.*;
import startervalley.backend.exception.CategoryFoundException;
import startervalley.backend.exception.StoreNotFoundException;
import startervalley.backend.repository.*;

import java.util.*;

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

    private final StoreImageService storeImageService;

    private final static List<StoreResponseDto> recommendStoreList = new ArrayList<>();

    public BaseResponseDto<List<StoreResponseDto>> findAllStores(@ModelAttribute StoreQueryParameter queryParameter) {
        User user = userRepository.findById(1L).orElseThrow();
        Pageable pageable = queryParameter.getPageable();
        Page<Store> storePage;
        String queryCategory = queryParameter.getCategory();
        if (queryCategory == null) {
            storePage = storeRepository.findAll(pageable);
        } else {
            Category category = getCategory(queryCategory);
            storePage = storeRepository.findAllByCategory(pageable, category);
        }

        List<Store> storeList = storePage.getContent();
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
    public BaseResponseDto<Long> createStore(StoreRequestDto storeRequestDto, List<MultipartFile> uploadFiles) {
        Category category = getCategory(storeRequestDto.getCategory());
        Store store = Store.builder()
                .name(storeRequestDto.getName())
                .address(storeRequestDto.getAddress())
                .description(storeRequestDto.getDescription())
                .category(category)
                .build();
        storeRepository.save(store);
        storeImageService.createImage(store, uploadFiles);
        return new BaseResponseDto<>(STORE_CREATE.toString(), store.getId());
    }

    public StoreDetailDto findStore(Long id) {
        User user = userRepository.findById(1L).orElseThrow();
        Store store = getStore(id);
        long likeCount = userLikeStoreRepository.countByStore(store);
        boolean myLikeStatus = userLikeStoreRepository.existsByUserAndStore(user, store);

        StoreResponseDto storeDto = StoreResponseDto.builder()
                .id(id)
                .name(store.getName())
                .address(store.getAddress())
                .description(store.getDescription())
                .likeCount(likeCount)
                .myLikeStatus(myLikeStatus)
                .category(store.getCategory().getName())
                .build();

        List<StoreImageDto> imageDto = store.getStoreImageList().stream()
                .map(image -> new StoreImageDto(image.getUuid(), image.getImgName(), image.getPath()))
                .toList();

        List<TagDto> tagDto = store.getStoreTagList().stream().map(storeTag -> {
            Tag tag = storeTag.getTag();
            return TagDto.of(tag.getContent());
        }).toList();

        return new StoreDetailDto(storeDto, imageDto, tagDto);
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
            throw new StoreNotFoundException("가게를 찾을 수 없습니다.");
        }
        return optional.get();
    }

    private Category getCategory(String name) {
        Optional<Category> optional = categoryRepository.findByName(name);
        if (optional.isEmpty()) {
            throw new CategoryFoundException("카테고리를 찾을 수 없습니다.");
        }
        return optional.get();
    }
}
