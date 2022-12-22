package startervalley.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import startervalley.backend.entity.*;
import startervalley.backend.repository.CommentTagRepository;
import startervalley.backend.repository.StoreTagRepository;
import startervalley.backend.repository.TagRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TagService {

    private final TagRepository tagRepository;
    private final StoreTagRepository storeTagRepository;
    private final CommentTagRepository commentTagRepository;

    @Transactional
    public void createTag(Object object, List<String> tagList) {

        if (tagList == null) return;

        for (String tag : tagList) {
            Optional<Tag> optional = tagRepository.findByContent(tag);
            Tag findTag;
            if (optional.isPresent()) {
                findTag = optional.get();
            } else {
                findTag = Tag.builder().content(tag).build();
                tagRepository.save(findTag);
            }

            if (object instanceof Store store) {
                StoreTag storeTag = StoreTag.builder().store(store).tag(findTag).build();
                storeTagRepository.save(storeTag);
            } else if (object instanceof Comment comment) {
                CommentTag commentTag = CommentTag.builder().comment(comment).tag(findTag).build();
                commentTagRepository.save(commentTag);
            }
        }
    }
}
