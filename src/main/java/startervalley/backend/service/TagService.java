package startervalley.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import startervalley.backend.entity.Store;
import startervalley.backend.entity.StoreTag;
import startervalley.backend.entity.Tag;
import startervalley.backend.repository.StoreTagRepository;
import startervalley.backend.repository.TagRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TagService {

    private final TagRepository tagRepository;
    private final StoreTagRepository storeTagRepository;

    @Transactional
    public void createTag(Store store, List<String> tagList) {
        for (String tag : tagList) {
            Optional<Tag> optional = tagRepository.findByContent(tag);
            Tag findTag;
            if (optional.isPresent()) {
                findTag = optional.get();
            } else {
                findTag = Tag.builder().content(tag).build();
                tagRepository.save(findTag);
            }
            StoreTag storeTag = StoreTag.builder().store(store).tag(findTag).build();
            storeTagRepository.save(storeTag);
        }
    }
}
