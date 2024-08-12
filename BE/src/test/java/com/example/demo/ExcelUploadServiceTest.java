package com.example.demo;

import com.example.demo.Dto.SubMaterialDTO.SubMaterialDTO;
import com.example.demo.Service.Impl.ExcelError;
import com.example.demo.Service.Impl.ExcelUploadService;
import org.apache.poi.EmptyFileException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

public class ExcelUploadServiceTest {

    // Helper method to create a valid Excel file in memory
    //chứa dữ liệu Excel giả ,withData ở đây để thể hiện nó chưa data mẫu hay không
    private MockMultipartFile createMockExcelFile(String sheetName, boolean withData) throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);

        // Create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Sub_material_name");
        header.createCell(1).setCellValue("Material_name");
        header.createCell(2).setCellValue("Description");
        header.createCell(3).setCellValue("Quantity");
        header.createCell(4).setCellValue("Unit_price");
        header.createCell(5).setCellValue("Input_price");
        header.createCell(6).setCellValue("Date_ware_house");

        if (withData) {
            // Create data rows
            Row row1 = sheet.createRow(1);
            row1.createCell(0).setCellValue("Sub1");
            row1.createCell(1).setCellValue("Material1");
            row1.createCell(2).setCellValue("Desc1");
            row1.createCell(3).setCellValue(10);
            row1.createCell(4).setCellValue(100.0);
            row1.createCell(5).setCellValue(50.0);
            // Chuyển đổi chuỗi thành đối tượng Date
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date warehouseDate = dateFormat.parse("12/01/2004");
            row1.createCell(6).setCellValue(warehouseDate);
        }
        //Tạo một đối tượng ByteArrayOutputStream để lưu trữ nội dung file Excel dưới dạng byte array.
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();

        InputStream is = new ByteArrayInputStream(bos.toByteArray());
        return new MockMultipartFile("file", "test.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", is);
    }

    //kiểm tra chức năng đọc dữ liệu từ file Excel và chuyển đổi thành danh sách SubMaterialDTO của lớp ExcelUploadService
    @Test
    void testGetSubMaterialDataFromExcel_ShouldReturnCorrectData() throws Exception {
        // Tạo 1 file excel giả
        MockMultipartFile file = createMockExcelFile("SubMaterial", true);
        List<ExcelError> errors = new ArrayList<>();

        // Act
        List<SubMaterialDTO> result = ExcelUploadService.getSubMaterialDataFromExcel(file.getInputStream(), errors);

        // Assert
        assertNotNull(result); //kiểm tra danh sách kết quả ko null
        assertEquals(1, result.size()); //Kiểm tra danh sách có một phần tử.
        assertTrue(errors.isEmpty()); // Kiểm tra danh sách lỗi rỗng (không có lỗi)
        SubMaterialDTO dto = result.get(0);//lấy phần tử đầu tiên và so sánh kêt quả với dữ liệu mẫu trong file excel
        assertEquals("Sub1", dto.getSub_material_name());
        assertEquals("Material1", dto.getMaterial_name());
        assertEquals("Desc1", dto.getDescription());
        assertEquals(10, dto.getQuantity());
        assertEquals(BigDecimal.valueOf(50.0), dto.getUnit_price());
        assertEquals(BigDecimal.valueOf(100.0), dto.getInput_price());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String expectedDateString = "12/01/2004";
        String actualDateString = dateFormat.format(dto.getDate_ware_house());
        assertEquals(expectedDateString,actualDateString);
    }

    @Test
    void testGetSubMaterialDataFromExcel_ShouldHandleInvalidData() throws Exception {
        // Arrange
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("SubMaterial");

        // Create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Sub_material_name");
        header.createCell(1).setCellValue("Material_name");
        header.createCell(2).setCellValue("Description");
        header.createCell(3).setCellValue("Quantity");
        header.createCell(4).setCellValue("Unit_price");
        header.createCell(5).setCellValue("Input_price");

        // Create invalid data row
        Row row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue("Sub1");
        row1.createCell(1).setCellValue("Material1");
        row1.createCell(2).setCellValue("Desc1");
        row1.createCell(3).setCellValue("InvalidNumber"); // Invalid quantity
        row1.createCell(4).setCellValue("InvalidNumber"); // Invalid unit price
        row1.createCell(5).setCellValue("InvalidNumber"); // Invalid input price

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();

        InputStream is = new ByteArrayInputStream(bos.toByteArray());
        MockMultipartFile file = new MockMultipartFile("file", "test.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", is);

        List<ExcelError> errors = new ArrayList<>();

        // Act
        List<SubMaterialDTO> result = ExcelUploadService.getSubMaterialDataFromExcel(file.getInputStream(), errors);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        ExcelError error = errors.get(0);
        assertTrue(error.getErrorMessage().contains("Lỗi định dạng"));
    }

    @Test
    void testGetSubMaterialDataFromExcel_ShouldHandleEmptyFile() throws Exception {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream(new byte[0]); // Create an empty input stream
        MockMultipartFile file = new MockMultipartFile("file", "test.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", inputStream);

        List<ExcelError> errors = new ArrayList<>();

        // Act & Assert
        assertThrows(EmptyFileException.class, () -> {
            ExcelUploadService.getSubMaterialDataFromExcel(file.getInputStream(), errors);
        }, "Expected EmptyFileException to be thrown for an empty file.");
    }
    @Test
    void testIsValidExcelFile_ShouldReturnFalseForIncorrectContentType() {
        // Arrange
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", new byte[0]); //tạo 1 file với dạng là text/plain

        // Act
        boolean result = ExcelUploadService.isValidExcelFile(file);

        // Assert
        assertFalse(result, "Expected isValidExcelFile to return false for non-Excel content type.");
    }


    @Test
    void testIsValidExcelFile_ShouldReturnTrueForValidFile() throws Exception {
        // Arrange
        MockMultipartFile file = createMockExcelFile("SubMaterial", true);

        // Act
        boolean isValid = ExcelUploadService.isValidExcelFile(file);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void testIsValidExcelFile_ShouldReturnFalseForInvalidFile() {
        // Arrange
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", new byte[0]);

        // Act
        boolean isValid = ExcelUploadService.isValidExcelFile(file);

        // Assert
        assertFalse(isValid);
    }


}

