package com.otienochris.filemanipulator;

import com.otienochris.filemanipulator.Models.Document;
import com.otienochris.filemanipulator.repositories.DocumentRepo;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FileUploaderAndDownloaderApplicationTests {

    @Autowired
    private DocumentRepo documentRepo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @Rollback(value = false)
    void testInsertDocument() throws IOException {
        File file = new File("C:\\Users\\admin\\Downloads\\ORDER 1485835.edited.odt");
        Document document = Document.builder()
                .name(file.getName())
                .content(Files.readAllBytes(file.toPath()))
                .size(file.length())
                .uploadTime(new Date())
                .build();
        long fileSize = file.length();

        Document savedDocument = documentRepo.save(document);
        Document existDoc = entityManager.find(Document.class, savedDocument.getId());

        assertThat(existDoc.getSize()).isEqualTo(fileSize);
    }

}
