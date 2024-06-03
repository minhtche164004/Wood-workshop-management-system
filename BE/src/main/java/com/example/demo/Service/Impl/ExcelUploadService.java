package com.example.demo.Service.Impl;

import com.example.demo.Dto.SubMaterialDTO.SubMaterialDTO;
import com.example.demo.Entity.SubMaterials;
import com.example.demo.Repository.SubMaterialsRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class ExcelUploadService {

    public static boolean isValidExcelFile(MultipartFile file){
        return Objects.equals(file.getContentType(),"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }
    public static List<SubMaterialDTO> getSubMaterialDataFromExcel(InputStream inputStream){
        List<SubMaterialDTO> subMaterialDTOS = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheet("SubMaterial");

            int rowIndex=0;
            for(Row row :sheet){
                if(rowIndex ==0){
                    rowIndex ++;
                    continue;
                }
                Iterator<Cell> cellIterator = row.iterator();
                int cellIndex=0;
                SubMaterialDTO subMaterialDTO = new SubMaterialDTO();
                while(cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    switch (cellIndex){
                        case 0 -> subMaterialDTO.setSub_material_name(cell.getStringCellValue());
                        case 1 -> subMaterialDTO.setMaterial_id((int) cell.getNumericCellValue());
                        case 2 -> subMaterialDTO.setDescription(cell.getStringCellValue());
                        case 3 -> subMaterialDTO.setQuantity((int) cell.getNumericCellValue());
                        case 4 -> subMaterialDTO.setUnit_price(BigDecimal.valueOf(cell.getNumericCellValue()));
                        default ->  {

                        }
                    }
                    cellIndex++;
                }
                subMaterialDTOS.add(subMaterialDTO);

            }
        } catch (IOException e) {
            e.getStackTrace();
        }
        return subMaterialDTOS;
    }
}
