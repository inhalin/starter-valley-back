package startervalley.backend.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import startervalley.backend.entity.Store;
import startervalley.backend.entity.StoreImage;
import startervalley.backend.repository.StoreImageRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Primary
@Service
public class StoreImageS3Service implements StoreImageService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final StoreImageRepository storeImageRepository;
    private final AmazonS3Client amazonS3Client;

    @Override
    public void uploadAndSave(Store store, List<MultipartFile> uploadFiles) throws IOException {

        if (uploadFiles == null) return;

        for (MultipartFile uploadFile : uploadFiles) {
            String originalName = uploadFile.getOriginalFilename();
            checkIfImageType(originalName);

            //실제 파일 이름 IE나 Edge는 전체 경로가 들어오므로
            String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);

            //UUID
            String uuid = UUID.randomUUID().toString();

            String saveName = uuid + "_" + fileName;

            ObjectMetadata objMeta = new ObjectMetadata();
            objMeta.setContentLength(uploadFile.getInputStream().available());

            amazonS3Client.putObject(bucket, saveName, uploadFile.getInputStream(), objMeta);

//            File file = convert(uploadFile)
//                    .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));
//
//            putS3(file, saveName);

            StoreImage storeImage = StoreImage.builder()
                    .imgName(fileName)
                    .uuid(uuid)
                    .store(store)
                    .build();
            storeImageRepository.save(storeImage);
        }
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }
}
