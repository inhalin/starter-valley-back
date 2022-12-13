package startervalley.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import startervalley.backend.entity.Store;
import startervalley.backend.entity.StoreImage;
import startervalley.backend.exception.StoreImageUploadFailedException;
import startervalley.backend.repository.StoreImageRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static startervalley.backend.constant.ExceptionMessage.FILE_TYPE_NOT_IMAGE;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StoreImageService {

    private final StoreImageRepository storeImageRepository;

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @Transactional
    public void createImage(Store store, List<MultipartFile> uploadFiles) {

        if (uploadFiles == null) {
            return;
        }

        for (MultipartFile uploadFile : uploadFiles) {
            log.info("uploadFile {}, contentType: {}", uploadFile.getOriginalFilename(), uploadFile.getContentType());

            String mimeType = null;
            mimeType = new Tika().detect(uploadFile.getOriginalFilename());
            if (!mimeType.startsWith("image")) {
                log.warn("this file is not image type");
                throw new StoreImageUploadFailedException(FILE_TYPE_NOT_IMAGE.toString());
            }

            //실제 파일 이름 IE나 Edge는 전체 경로가 들어오므로
            String originalName = uploadFile.getOriginalFilename();
            String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);

            log.info("fileName: {}", fileName);
            //날짜 폴더 생성
            String folderPath = makeFolder();

            //UUID
            String uuid = UUID.randomUUID().toString();

            //저장할 파일 이름 중간에 "_"를 이용해서 구분
            String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;
            Path savePath = Paths.get(saveName);

            try {
                //원본 파일 저장
                uploadFile.transferTo(savePath);

                //섬네일 생성
                String thumbnailSaveName = uploadPath + File.separator + folderPath + File.separator
                        + "s_" + uuid + "_" + fileName;
                //섬네일 파일 이름은 중간에 s_로 시작하도록
                File thumbnailFile = new File(thumbnailSaveName);
                //섬네일 생성
                Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 100, 100);
                StoreImage storeImage = StoreImage.builder()
                        .imgName(fileName)
                        .path(folderPath)
                        .uuid(uuid)
                        .store(store)
                        .build();
                storeImageRepository.save(storeImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String makeFolder() {

        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        String folderPath = str.replace("//", File.separator);

        // make folder --------
        File uploadPathFolder = new File(uploadPath, folderPath);

        if (!uploadPathFolder.exists()) {
            uploadPathFolder.mkdirs();
        }
        return folderPath;
    }
}
