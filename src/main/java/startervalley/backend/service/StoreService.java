package startervalley.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;
import startervalley.backend.dto.request.PageRequestDto;
import startervalley.backend.dto.request.StoreRequestDto;
import startervalley.backend.dto.response.*;
import startervalley.backend.entity.*;
import startervalley.backend.exception.ResourceNotFoundException;
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
    private final TagService tagService;

    private final static List<StoreResponseDto> recommendStoreList = new ArrayList<>();

    public PageResultDTO<Store, StoreResponseDto> findAllStores(Long userId, @ModelAttribute PageRequestDto pageRequestDto) {
        User user = getUser(userId);
        Pageable pageable = pageRequestDto.getPageable();
        Page<Store> storePage;
        Long queryCategory = pageRequestDto.getCategoryId();
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

        return new PageResultDTO<>(storePage, result);
    }

    // TODO: 최초 등록자 관리해주기?
    @Transactional
    public Long createStore(Long userId, StoreRequestDto storeRequestDto, List<MultipartFile> uploadFiles) {
        User user = getUser(userId);
        Category category = getCategory(storeRequestDto.getCategoryId());
        Store store = Store.builder()
                .name(storeRequestDto.getName())
                .address(storeRequestDto.getAddress())
                .description(storeRequestDto.getDescription())
                .url(storeRequestDto.getUrl())
                .category(category)
                .build();
        storeRepository.save(store);
        storeImageService.createImage(store, uploadFiles);
        tagService.createTag(store, storeRequestDto.getTagList());
        return store.getId();
    }

    public StoreDetailDto findStore(Long userId, Long storeId) {
        User user = getUser(userId);
        Store store = getStore(storeId);
        long likeCount = userLikeStoreRepository.countByStore(store);
        boolean myLikeStatus = userLikeStoreRepository.existsByUserAndStore(user, store);

        StoreResponseDto storeDto = StoreResponseDto.builder()
                .id(storeId)
                .name(store.getName())
                .address(store.getAddress())
                .description(store.getDescription())
                .url(store.getUrl())
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
    public void modify(Long id, StoreRequestDto storeRequestDto) {
        Store store = getStore(id);
        store.update(storeRequestDto);
    }

    public List<CategoryResponseDto> findAllCategory() {
        List<Category> categoryList = categoryRepository.findAll();
        return categoryList.stream()
                .map(category -> new CategoryResponseDto(category.getId(), category.getName()))
                .toList();
    }

    public List<StoreResponseDto> findMyStoreLikeList(Long userId) {
        User user = getUser(userId);
        List<UserLikeStore> likeStoreList = userLikeStoreRepository.findAllByUser(user);
        return likeStoreList.stream().map(likeStore -> {
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
    }

    @Transactional
    public String likeStore(Long userId, Long storeId) {
        User user = getUser(userId);
        Store store = getStore(storeId);
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
        return message;
    }

    public PageResultDTO<Comment, CommentResponseDto> findStoreComments(Long userId, Long storeId, PageRequestDto pageRequestDto) {
        User user = getUser(userId);
        Store store = getStore(storeId);
        Pageable pageable = pageRequestDto.getPageable();
        Page<Comment> commentPage = commentRepository.findAllByStore(store, pageable);


        List<CommentResponseDto> result = commentPage.getContent().stream().map(comment -> {
            boolean isOwn = Objects.equals(comment.getUser().getId(), user.getId());
            return CommentResponseDto   .builder()
                    .id(comment.getId())
                    .description(comment.getDescription())
                    .isOwn(isOwn)
                    .createdDate(comment.getCreatedDate())
                    .modifiedDate(comment.getModifiedDate())
                    .build();
        }).toList();
        return new PageResultDTO<>(commentPage, result);
    }

    public List<StoreResponseDto> findRecommendStoreList() {
        return recommendStoreList;
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
            throw new ResourceNotFoundException("Store", "id", id.toString());
        }
        return optional.get();
    }

    private Category getCategory(Long categoryId) {
        Optional<Category> optional = categoryRepository.findById(categoryId);
        if (optional.isEmpty()) {
            throw new ResourceNotFoundException("Category", "categoryId", categoryId.toString());
        }
        return optional.get();
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", String.valueOf(userId)));
    }
}