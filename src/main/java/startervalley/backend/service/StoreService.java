package startervalley.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;
import startervalley.backend.dto.request.CommentRequestDto;
import startervalley.backend.dto.request.PageRequestDto;
import startervalley.backend.dto.request.StoreRequestDto;
import startervalley.backend.dto.request.StoreUpdateDto;
import startervalley.backend.dto.response.*;
import startervalley.backend.entity.*;
import startervalley.backend.exception.NotOwnerException;
import startervalley.backend.exception.ResourceNotFoundException;
import startervalley.backend.repository.*;

import java.io.IOException;
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

    public StoreDetailDto findStore(Long userId, Long storeId) {
        User user = getUserElseThrow(userId);
        Store store = getStoreOrElseThrow(storeId);
        long likeCount = userLikeStoreRepository.countByStore(store);
        boolean myLikeStatus = userLikeStoreRepository.existsByUserAndStore(user, store);
        boolean own = Objects.equals(store.getUser().getId(), userId);
        StoreResponseDto storeDto = StoreResponseDto.builder()
                .id(storeId)
                .name(store.getName())
                .address(store.getAddress())
                .description(store.getDescription())
                .url(store.getUrl())
                .likeCount(likeCount)
                .myLikeStatus(myLikeStatus)
                .category(store.getCategory().getName())
                .own(own)
                .build();

        List<StoreImageDto> imageDto = store.getStoreImageList().stream()
                .map(image -> new StoreImageDto(image.getId(), image.getUuid(), image.getImgName(), image.getPath()))
                .toList();

        List<TagDto> tagDto = store.getStoreTagList().stream().map(storeTag -> {
            Tag tag = storeTag.getTag();
            return TagDto.builder().id(tag.getId()).content(tag.getContent()).build();
        }).toList();

        return new StoreDetailDto(storeDto, imageDto, tagDto);
    }

    public PageResultDTO<Store, StoreResponseDto> findAllStores(Long userId, @ModelAttribute PageRequestDto pageRequestDto) {
        User user = getUserElseThrow(userId);
        Pageable pageable = pageRequestDto.getPageable();
        Page<Store> storePage;
        Long queryCategory = pageRequestDto.getCategoryId();
        if (queryCategory == null) {
            storePage = storeRepository.findAll(pageable);
        } else {
            Category category = getCategoryElseThrow(queryCategory);
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

    @Transactional
    public Long createStore(Long userId, StoreRequestDto storeRequestDto, List<MultipartFile> uploadFiles) {
        User user = getUserElseThrow(userId);
        Category category = getCategoryElseThrow(storeRequestDto.getCategoryId());
        Store store = Store.builder()
                .name(storeRequestDto.getName())
                .address(storeRequestDto.getAddress())
                .description(storeRequestDto.getDescription())
                .url(storeRequestDto.getUrl())
                .category(category)
                .user(user)
                .build();
        storeRepository.save(store);
        try {
            storeImageService.uploadAndSave(store, uploadFiles);
        } catch (IOException e) {
            throw new RuntimeException();
        }
        tagService.createTag(store, storeRequestDto.getTagList());
        return store.getId();
    }

    @Transactional
    public void updateStore(Long userId, Long storeId, StoreUpdateDto storeUpdateDto, List<MultipartFile> uploadFiles) throws IOException {
        Store store = getStoreOrElseThrow(storeId);
        Category category = getCategoryElseThrow(storeUpdateDto.getCategoryId());

        if (!Objects.equals(store.getUser().getId(), userId)) {
            throw new NotOwnerException();
        }

        store.getStoreTagList().clear();
        tagService.createTag(store, storeUpdateDto.getTagList());
        storeImageService.deleteFilesAndUpdate(store, storeUpdateDto.getDeleteImgIdList(), uploadFiles);
        store.update(storeUpdateDto, category);
    }

    @Transactional
    public void deleteStore(Long userId, Long storeId) {
        Store store = getStoreOrElseThrow(storeId);

        if (!Objects.equals(store.getUser().getId(), userId)) {
            throw new NotOwnerException();
        }

        storeRepository.deleteById(storeId);
    }

    public List<CategoryResponseDto> findAllCategory() {
        List<Category> categoryList = categoryRepository.findAll();
        return categoryList.stream()
                .map(category -> new CategoryResponseDto(category.getId(), category.getName()))
                .toList();
    }

    public List<StoreResponseDto> findMyStoreLikeList(Long userId) {
        User user = getUserElseThrow(userId);
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
        User user = getUserElseThrow(userId);
        Store store = getStoreOrElseThrow(storeId);
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
        User user = getUserElseThrow(userId);
        Store store = getStoreOrElseThrow(storeId);
        Pageable pageable = pageRequestDto.getPageable();
        Page<Comment> commentPage = commentRepository.findAllByStore(store, pageable);


        List<CommentResponseDto> result = commentPage.getContent().stream().map(comment -> {
            User commentUser = comment.getUser();
            boolean isOwn = Objects.equals(commentUser.getId(), user.getId());
            List<TagDto> tagDtoList = comment.getCommentTagList().stream().map(commentTag -> {
                Tag tag = commentTag.getTag();
                return TagDto.builder().id(tag.getId()).content(tag.getContent()).build();
            }).toList();

            return CommentResponseDto.builder()
                    .id(comment.getId())
                    .description(comment.getDescription())
                    .author(commentUser.getName())
                    .isOwn(isOwn)
                    .tagList(tagDtoList)
                    .createdDate(comment.getCreatedDate())
                    .modifiedDate(comment.getModifiedDate())
                    .build();
        }).toList();
        return new PageResultDTO<>(commentPage, result);
    }

    @Transactional
    public Long createComment(Long userId, Long storeId, CommentRequestDto commentRequestDto) {
        User user = getUserElseThrow(userId);
        Store store = getStoreOrElseThrow(storeId);

        String content = commentRequestDto.getContent();
        Comment comment = Comment.builder().user(user).store(store).description(content).build();
        commentRepository.save(comment);

        List<String> tagList = commentRequestDto.getTagList();
        tagService.createTag(comment, tagList);

        return comment.getId();
    }

    @Transactional
    public void updateComment(Long userId, Long commentId, CommentRequestDto commentRequestDto) {
        Comment comment = getCommentOrElseThrow(commentId);

        if (!Objects.equals(comment.getUser().getId(), userId)) {
            throw new NotOwnerException();
        }

        comment.getCommentTagList().clear();
        tagService.createTag(comment, commentRequestDto.getTagList());
        comment.update(commentRequestDto);
    }

    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = getCommentOrElseThrow(commentId);

        if (!Objects.equals(comment.getUser().getId(), userId)) {
            throw new NotOwnerException();
        }

        commentRepository.deleteById(commentId);
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

    private Store getStoreOrElseThrow(Long id) {
        return storeRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Store", "id", id.toString()));
    }

    private Comment getCommentOrElseThrow(Long id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Comment", "id", id.toString()));
    }

    private Category getCategoryElseThrow(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() ->
                new ResourceNotFoundException("Category", "categoryId", categoryId.toString()));
    }

    private User getUserElseThrow(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User", "id", String.valueOf(userId)));
    }
}