package com.example.demo;

import com.example.demo.Service.Impl.CustomMultipartFile;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class CustomMultipartFileTest {

    @Test
    void testGetName() {
        CustomMultipartFile file = new CustomMultipartFile(new byte[]{1, 2, 3}, "test.txt");
        assertEquals("test.txt", file.getName());
    }

    @Test
    void testGetOriginalFilename() {
        CustomMultipartFile file = new CustomMultipartFile(new byte[]{1, 2, 3}, "test.txt");
        assertEquals("test.txt", file.getOriginalFilename());
    }

    @Test
    void testGetContentType() {
        CustomMultipartFile file = new CustomMultipartFile(new byte[]{1, 2, 3}, "test.txt");
        assertNull(file.getContentType());
    }

    @Test
    void testIsEmpty() {
        CustomMultipartFile file = new CustomMultipartFile(new byte[]{}, "test.txt");
        assertTrue(file.isEmpty());
    }

    @Test
    void testIsNotEmpty() {
        CustomMultipartFile file = new CustomMultipartFile(new byte[]{1, 2, 3}, "test.txt");
        assertFalse(file.isEmpty());
    }

    @Test
    void testGetSize() {
        CustomMultipartFile file = new CustomMultipartFile(new byte[]{1, 2, 3}, "test.txt");
        assertEquals(3, file.getSize());
    }

    @Test
    void testGetBytes() throws IOException {
        byte[] content = {1, 2, 3};
        CustomMultipartFile file = new CustomMultipartFile(content, "test.txt");
        assertArrayEquals(content, file.getBytes());
    }

    @Test
    void testGetInputStream() throws IOException {
        byte[] content = {1, 2, 3};
        CustomMultipartFile file = new CustomMultipartFile(content, "test.txt");
        assertNotEquals(new ByteArrayInputStream(content), file.getInputStream());
    }

    @Test
    void testTransferTo_ShouldThrowUnsupportedOperationException() {
        CustomMultipartFile file = new CustomMultipartFile(new byte[]{1, 2, 3}, "test.txt");
        assertThrows(UnsupportedOperationException.class, () -> {
            file.transferTo(new java.io.File("destination.txt"));
        });
    }

    @Test
    void testConstructorSetsFieldsCorrectly() {
        byte[] content = {1, 2, 3};
        String fileName = "test.txt";
        CustomMultipartFile file = new CustomMultipartFile(content, fileName);

        assertArrayEquals(content, file.getImgContent());
        assertEquals(fileName, file.getOriginalFilename());
    }
}
