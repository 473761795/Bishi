package com.example.mianshi.Controller;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


@RestController
public class SKUSplitController {
    @PostMapping("/skuSplit")
    public Object skuSplit(MultipartFile file) {
        try {
            // 将上传的文件保存到临时文件
            File tempFile = File.createTempFile("temp", ".xlsx");
            file.transferTo(tempFile);

            // 执行SKU分裂操作
            String outputFile = "path/to/output_file.xlsx"; // 指定输出文件路径
            SKUSplitter.split(tempFile.getAbsolutePath(), outputFile);

            // 读取分裂后的Excel文件内容
            byte[] fileContent = Files.readAllBytes(Paths.get(outputFile));

            // 返回分裂后的Excel文件内容
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(ContentDisposition.attachment().filename("split_output.xlsx").build());
            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("SKU split failed: " + e.getMessage());
        }
    }

    public static class SKUSplitter {
        public static void split(String inputFile, String outputFile) {
            try {
                // 读取原始数据Excel文件
                FileInputStream fis = new FileInputStream(inputFile);
                Workbook workbook = new XSSFWorkbook(fis);
                Sheet sheet = workbook.getSheetAt(0);

                // 创建新的Excel文件
                Workbook newWorkbook = new XSSFWorkbook();
                Sheet newSheet = newWorkbook.createSheet();

                // 将原始数据的表头复制到新文件
                Row headerRow = sheet.getRow(0);
                Row newHeaderRow = newSheet.createRow(0);
                for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                    Cell cell = headerRow.getCell(i);
                    Cell newCell = newHeaderRow.createCell(i);
                    newCell.setCellValue(cell.getStringCellValue());
                }

                // 分裂SKU数据
                int rowIndex = 1;
                Gson gson = new Gson();
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    String skuDetails = row.getCell(2).getStringCellValue();
                    JsonArray skuArray = gson.fromJson(skuDetails, JsonArray.class);

                    for (JsonElement skuElement : skuArray) {
                        JsonObject skuObject = skuElement.getAsJsonObject();

                        // 创建新行并填充分裂后的SKU数据
                        Row newRow = newSheet.createRow(rowIndex++);
                        newRow.createCell(0).setCellValue(skuObject.get("sku_id").getAsString());
                        newRow.createCell(1).setCellValue(skuObject.get("sku_name").getAsString());
                        newRow.createCell(2).setCellValue(skuObject.get("sku_current_price").getAsDouble());
                        newRow.createCell(3).setCellValue(skuObject.get("sku_original_price").getAsDouble());
                        newRow.createCell(4).setCellValue(skuObject.get("sku_stock").getAsInt());
                    }
                }

                // 保存新的Excel文件
                FileOutputStream fos = new FileOutputStream(outputFile);
                newWorkbook.write(fos);
                fos.close();

                System.out.println("SKU splitting completed successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
