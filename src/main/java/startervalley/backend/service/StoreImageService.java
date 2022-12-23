package startervalley.backend.service;

import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;
import startervalley.backend.entity.Store;
import startervalley.backend.exception.StoreImageUploadFailedException;

import java.io.IOException;
import java.util.List;

import static startervalley.backend.constant.ExceptionMessage.FILE_TYPE_NOT_IMAGE;

public interface StoreImageService {

    void uploadAndSave(Store store, List<MultipartFile> uploadFiles) throws IOException;

    default void deleteFiles(List<Long> deleteFiles) {}

    default void deleteFilesAndUpdate(Store store, List<Long> deleteImgList, List<MultipartFile> uploadFiles) throws IOException {
        deleteFiles(deleteImgList);
        uploadAndSave(store, uploadFiles);
    }

    default void checkIfImageType(String originalName) {
        String mimeType = new Tika().detect(originalName);
        if (!mimeType.startsWith("image")) {
            throw new StoreImageUploadFailedException(FILE_TYPE_NOT_IMAGE.toString());
        }
    }
}
