package com.example.demo;

import com.example.demo.Service.Impl.MultipartFileConverter;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class MultipartFileConverterTest {

    private final MultipartFileConverter converter = new MultipartFileConverter();

    @Test
    void testConvertBase64ToMultipartFile_ValidData() {
        // Arrange
        String base64 = "data:image/png;base64," + Base64.getEncoder().encodeToString("dummyImageData".getBytes());
        String originalFileName = "image.png";

        // Act
        MultipartFile file = converter.convertBase64ToMultipartFile(base64, originalFileName);

        // Assert
        assertNotNull(file);
        assertEquals(originalFileName, file.getOriginalFilename());
    }



    @Test
    void testConvertBase64ToMultipartFile_InvalidBase64Format() {
        // Arrange
        String base64 = "invalidBase64String";

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            converter.convertBase64ToMultipartFile(base64, "file.png");
        });
        assertEquals("Invalid Base64 string: missing comma or data", thrown.getMessage());
    }

    @Test
    void testConvertBase64ToMultipartFile_InvalidContentType() {
        // Arrange
        String base64 = "data:text/plain;base64," + Base64.getEncoder().encodeToString("dummyTextData".getBytes());

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            converter.convertBase64ToMultipartFile(base64, "file.txt");
        });
        assertEquals("Invalid Base64 string: not an image", thrown.getMessage());
    }

    @Test
    void testConvertBase64ToMultipartFile_EmptyBase64String() {
        // Arrange
        String base64 = "";

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            converter.convertBase64ToMultipartFile(base64, "file.png");
        });
        assertEquals("Invalid Base64 string: missing comma or data", thrown.getMessage());
    }

    @Test
    void testConvertBase64ToMultipartFile_EmtyBase64String() {
        // Arrange
        String base64 = "";

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            converter.convertBase64ToMultipartFile(base64, "file.png");
        });
        assertEquals("Invalid Base64 string: missing comma or data", thrown.getMessage());
    }

    @Test
    void testConvertBase64ToMultipartFile_NoCommaInBase64() {
        // Arrange
        String base64 = "data:image/jpeg;base64dummyImageData";

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            converter.convertBase64ToMultipartFile(base64, "file.jpg");
        });
        assertEquals("Invalid Base64 string: missing comma or data", thrown.getMessage());
    }

    @Test
    void testConvertBase64ToMultipartFile_EmptyOriginalFileName() {
        // Arrange
        String base64 = "";

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            converter.convertBase64ToMultipartFile(base64, "file.png");
        });
        assertEquals("Invalid Base64 string: missing comma or data", thrown.getMessage());
    }

    @Test
    void testConvertBase64ToMultipartFile_DifferentFileExtension() {
        // Arrange
        String base64 = "data:image/bmp;base64," + Base64.getEncoder().encodeToString("dummyImageData".getBytes());

        // Act
        MultipartFile file = converter.convertBase64ToMultipartFile(base64, "image.bmp");

        // Assert
        assertNotNull(file);
        assertEquals("image.bmp", file.getOriginalFilename());
    }

    @Test
    void testConvertBase64ToMultipartFile_ValidBase64Image() {
        // Arrange
        String base64 = "data:image/png;base64," + Base64.getEncoder().encodeToString("dummyImageData".getBytes());

        // Act
        MultipartFile file = converter.convertBase64ToMultipartFile(base64, "image.png");

        // Assert
        assertNotNull(file);
        assertEquals("image.png", file.getOriginalFilename());
    }
}
