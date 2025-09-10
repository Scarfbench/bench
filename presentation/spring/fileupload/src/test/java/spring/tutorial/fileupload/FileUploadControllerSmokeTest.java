package spring.tutorial.fileupload;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = FileUploadApplication.class,
                webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class FileUploadControllerSmokeTest {

    @Autowired
    private MockMvc mockMvc;

    @TempDir
    Path tempDir;

    @Test
    void rootResponds() throws Exception {
        // Spring's welcome-page handler forwards "/" to "index.html" (no body in MockMvc)
        mockMvc.perform(get("/"))
               .andExpect(status().isOk())
               .andExpect(forwardedUrl("index.html"));
    }

    @Test
    void indexHtmlServed() throws Exception {
        mockMvc.perform(get("/index.html"))
               .andExpect(status().isOk())
               .andExpect(content().string(containsString("<form")))
               .andExpect(content().string(containsString("enctype=\"multipart/form-data\"")));
    }

    @Test
    void uploadCreatesFileInDestination() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "hello.txt", "text/plain",
                "hello spring".getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(multipart("/upload")
                        .file(file)
                        .param("destination", tempDir.toString()))
               .andExpect(status().isOk())
               .andExpect(content().string(containsString("New file hello.txt created at")));

        assertTrue(Files.exists(tempDir.resolve("hello.txt")), "Uploaded file should exist in destination");
    }

    @Test
    void uploadWithEmptyFileReturnsHelpfulMessage() throws Exception {
        MockMultipartFile empty = new MockMultipartFile(
                "file", "empty.txt", "text/plain", new byte[0]
        );

        mockMvc.perform(multipart("/upload")
                        .file(empty)
                        .param("destination", tempDir.toString()))
               .andExpect(status().isOk())
               .andExpect(content().string(containsString("did not specify a file")));
    }

    @Test
    void uploadWithBlankDestinationReturnsHelpfulMessage() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "a.txt", "text/plain", "x".getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(multipart("/upload")
                        .file(file)
                        .param("destination", ""))
               .andExpect(status().isOk())
               .andExpect(content().string(containsString("Destination must be provided")));
    }
}
