package com.example.mianshi.Controller;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**

 *这里是解答《按列拆分成多个Excel》题
 2. 如果不开发接口呢 运营人员不懂技术 让他们调用api去拆分excel 也不现实
 能否直接生成一个exe工具给到运营人员 让他们去使用呢 请说明怎么将Java代码转成exe 以及 运营人员怎么去使用这个exe
 答：
 将Java代码转成可执行的EXE工具：
 将Java代码转成可执行的EXE工具可以使用第三方工具，如Launch4j或JSmooth。这些工具可以将Java代码打包成可执行的EXE文件，并将Java运行时环境（JRE）嵌入到EXE文件中。
 下面我将说明用Launch4j工具的步骤：

 步骤1：下载和安装Launch4j。

 访问Launch4j的官方网站：https://launch4j.sourceforge.io/

 步骤2：创建Launch4j配置文件。

 在项目的根目录下创建一个新的XML文件，命名为"launch4j.xml"。
 编辑该XML文件，按照以下示例配置文件的格式，根据你的项目设置配置项：
 xml
 Copy code
 <launch4jConfig>
 <dontWrapJar>false</dontWrapJar>
 <headerType>gui</headerType>
 <jar>path/to/your/project.jar</jar>
 <outfile>path/to/output/exe/file.exe</outfile>
 <errTitle>Error</errTitle>
 <cmdLine></cmdLine>
 <chdir>.</chdir>
 <priority>normal</priority>
 <versionInfo>
 <fileVersion>1.0.0.0</fileVersion>
 <txtFileVersion>1.0.0</txtFileVersion>
 <fileDescription>Excel Splitter</fileDescription>
 <productVersion>1.0.0.0</productVersion>
 <txtProductVersion>1.0.0</txtProductVersion>
 <productName>Excel Splitter</productName>
 <internalName>ExcelSplitter</internalName>
 <originalFilename>file.exe</originalFilename>
 </versionInfo>
 </launch4jConfig>
 将path/to/your/project.jar替换为你的Java项目的JAR文件路径，将path/to/output/exe/file.exe替换为输出的EXE文件路径。你还可以根据需要调整其他配置项。

 步骤3：使用Launch4j创建EXE文件。

 打开Launch4j程序。
 点击"Load configuration"按钮，选择步骤2中创建的"launch4j.xml"文件。
 点击"Build wrapper"按钮，Launch4j将会根据配置文件创建EXE文件。
 运行生成的EXE文件将启动Java程序，并执行Java代码。
 */
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

//@RestController
//public class ExcelSplitterController {
//
//    @PostMapping(value = "/splitExcel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<byte[]> splitExcel(@RequestParam("excelFile") MultipartFile excelFile,
//                                             @RequestParam("columnName") String columnName) throws IOException {
//        // 读取Excel文件
//        try (InputStream inputStream = excelFile.getInputStream()) {
//            List<Map<String, String>> dataList = EasyExcel.read(inputStream).sheet().doReadSync(); // 使用EasyExcel读取excel文件中的数据
//            System.out.println("Received Excel file with " + dataList.size() + " rows.");
//
//            // 按指定列的值拆分数据
//            Map<String, List<Map<String, String>>> splitData = new HashMap<>(); // 创建一个Map用于存储按列拆分后的数据
//            for (Map<String, String> row : dataList) {
//                String columnValue = row.get(columnName);
//                System.out.println("columnValue: " + columnValue);
//                if (!splitData.containsKey(columnValue)) {
//                    splitData.put(columnValue, new ArrayList<>()); // 如果Map中没有该列名对应的数据，则创建一个空列表
//                }
//                splitData.get(columnValue).add(row); // 将该行数据添加到对应列名的列表中
//            }
//
//            // 生成拆分后的Excel文件并放入ZIP文件
//            try (ByteArrayOutputStream zipOutputStream = new ByteArrayOutputStream();
//                 ZipOutputStream zip = new ZipOutputStream(zipOutputStream)) {
//                System.out.println("Split data size: " + splitData.size());
//                for (String key : splitData.keySet()) { // 遍历按列拆分后的数据Map中的每个列名
//                    System.out.println("Processing column: " + columnName);
//                    List<Map<String, String>> splitRows = splitData.get(key); // 获取该列名对应的数据列表
//                    System.out.println("Number of rows: " + splitRows.size());
//                    ByteArrayOutputStream tempOutputStream = new ByteArrayOutputStream(); // 创建一个临时输出流用于存储Excel文件的内容
//                    ExcelWriter excelWriter = EasyExcel.write(tempOutputStream).build(); // 创建ExcelWriter对象并与临时输出流关联
//                    WriteSheet writeSheet = EasyExcel.writerSheet().build(); // 创建工作表对象WriteSheet
//                    excelWriter.write(splitRows, writeSheet); // 将数据写入Excel文件
//                    excelWriter.finish(); // 关闭并释放ExcelWriter
//
//                    tempOutputStream.flush(); // 刷新缓冲区，确保数据写入到tempOutputStream
//
//                    ByteArrayInputStream tempInputStream = new ByteArrayInputStream(tempOutputStream.toByteArray()); // 将临时输出流的内容转换为字节数组作为输入流
//                    zip.putNextEntry(new ZipEntry(columnName + "_" + key + ".xlsx")); // 在ZIP文件中添加一个新条目，命名为"列名_列值.xlsx"
//                    byte[] buffer = new byte[1024];
//                    int length;
//                    while ((length = tempInputStream.read(buffer)) > 0) {
//                        zip.write(buffer, 0, length); // 将拆分后的Excel文件内容写入到ZIP文件的相应条目中
//                    }
//                    zip.closeEntry(); // 关闭当前ZIP文件条目
//                    tempOutputStream.close(); // 关闭临时输出流
//                    tempInputStream.close(); // 关闭临时输入流
//                }
//
//                byte[] zipBytes = zipOutputStream.toByteArray(); // 将ZIP文件内容转换为字节数组
//
//                return ResponseEntity.ok()
//                        .header("Content-Disposition", "attachment; filename=split_excel.zip")
//                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                        .body(zipBytes);
//            }
//        }
//    }
//}
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
@RestController
public class ExcelSplitterController {

    @PostMapping("/split-excel")
    public ResponseEntity<byte[]> splitExcel(
            @RequestParam("excelFile") MultipartFile excelFile,
            @RequestParam("columnName") String columnName) throws IOException {

        // 读取上传的Excel文件
        InputStream inputStream = excelFile.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);

        // 获取第一个Sheet
        Sheet sheet = workbook.getSheetAt(0);

        // 获取列索引
        int columnIndex = getColumnIndex(sheet, columnName);
        if (columnIndex == -1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("列名不存在".getBytes());
        }

        // 创建一个Map，用于存储按列值拆分的数据
        Map<String, List<Row>> splitDataMap = new HashMap<>();

        // 遍历每一行（从第二行开始，跳过列名）
        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            Cell cell = row.getCell(columnIndex);
            String columnValue = cell.getStringCellValue().trim();

            // 根据列值将数据添加到对应的列表中
            List<Row> splitData = splitDataMap.getOrDefault(columnValue, new ArrayList<>());
            splitData.add(row);
            splitDataMap.put(columnValue, splitData);
        }

        // 创建一个临时文件夹来存储拆分后的Excel文件
        File tempFolder = createTempFolder();

        // 遍历拆分后的数据，将每个列值的数据保存为单独的Excel文件
        for (Map.Entry<String, List<Row>> entry : splitDataMap.entrySet()) {
            String columnValue = entry.getKey();
            List<Row> splitData = entry.getValue();

            // 创建新的Workbook和Sheet
            Workbook outputWorkbook = new XSSFWorkbook();
            Sheet outputSheet = outputWorkbook.createSheet();

            // 复制数据到新的Sheet
            int rowIndex = 0;
            for (Row row : splitData) {
                Row newRow = outputSheet.createRow(rowIndex++);
                copyRow(row, newRow);
            }

            // 保存为Excel文件
            String outputFilePath = tempFolder.getAbsolutePath() + "/" + columnValue + ".xlsx";
            FileOutputStream fos = new FileOutputStream(outputFilePath);
            outputWorkbook.write(fos);
            fos.close();
        }

        // 生成压缩包
//        System.out.println(columnName);
        String zipFileName = excelFile.getOriginalFilename().replace(".xlsx", "") + "-" + columnName + ".zip";
        String zipFilePath = tempFolder.getParentFile().getAbsolutePath() + "/" + zipFileName;
        zipFiles(tempFolder.getAbsolutePath(), zipFilePath);

        // 读取压缩包的字节数组
        byte[] zipFileBytes = readBytesFromFile(zipFilePath);

        // 设置HTTP头信息，以便浏览器可以下载压缩包
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", zipFileName);

        // 删除临时文件和文件夹
        deleteFile(zipFilePath);
        deleteFile(tempFolder.getAbsolutePath());

        // 返回包含压缩包字节数组的ResponseEntity
        return new ResponseEntity<>(zipFileBytes, headers, HttpStatus.OK);
    }

    // 获取列索引
    private int getColumnIndex(Sheet sheet, String columnName) {
        Row firstRow = sheet.getRow(0);
        for (int i = 0; i < firstRow.getLastCellNum(); i++) {
            Cell cell = firstRow.getCell(i);
            if (cell.getStringCellValue().trim().equals(columnName)) {
                return i;
            }
        }
        return -1;
    }

    // 复制行
    private void copyRow(Row sourceRow, Row newRow) {
        newRow.setHeight(sourceRow.getHeight());

        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
            Cell sourceCell = sourceRow.getCell(i);
            Cell newCell = newRow.createCell(i);

            if (sourceCell != null) {
                CellType cellType = sourceCell.getCellType();
                newCell.setCellType(cellType);

                switch (cellType) {
                    case NUMERIC:
                        newCell.setCellValue(sourceCell.getNumericCellValue());
                        break;
                    case STRING:
                        newCell.setCellValue(sourceCell.getStringCellValue());
                        break;
                    case BOOLEAN:
                        newCell.setCellValue(sourceCell.getBooleanCellValue());
                        break;
                    case FORMULA:
                        newCell.setCellFormula(sourceCell.getCellFormula());
                        break;
                    default:
                        break;
                }
            }
        }
    }

    // 创建临时文件夹
    private File createTempFolder() throws IOException {
        File tempFolder = File.createTempFile("excel-splitter", "");
        tempFolder.delete();
        tempFolder.mkdirs();
        return tempFolder;
    }

    // 压缩文件夹中的文件
    private void zipFiles(String sourceFolderPath, String zipFilePath) throws IOException {
        FileOutputStream fos = new FileOutputStream(zipFilePath);
        ZipOutputStream zos = new ZipOutputStream(fos);

        File sourceFolder = new File(sourceFolderPath);
        File[] files = sourceFolder.listFiles();

        for (File file : files) {
            if (file.isDirectory()) {
                zipSubFolder(zos, file, file.getName() + "/");
            } else {
                zipFile(zos, file, "");
            }
        }

        zos.close();
        fos.close();
    }

    // 压缩子文件夹
    private void zipSubFolder(ZipOutputStream zos, File folder, String basePath) throws IOException {
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                zipSubFolder(zos, file, basePath + folder.getName() + "/");
            } else {
                zipFile(zos, file, basePath + folder.getName() + "/");
            }
        }
    }

    // 压缩文件
    private void zipFile(ZipOutputStream zos, File file, String basePath) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        zos.putNextEntry(new ZipEntry(basePath + file.getName()));
        int length;
        while ((length = fis.read(buffer)) > 0) {
            zos.write(buffer, 0, length);
        }
        fis.close();
        zos.closeEntry();
    }

    // 从文件中读取字节数组
    private byte[] readBytesFromFile(String filePath) throws IOException {
        File file = new File(filePath);
        byte[] fileBytes = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(fileBytes);
        fis.close();
        return fileBytes;
    }

    // 删除文件或文件夹（递归删除）
    private void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File subFile : files) {
                    deleteFile(subFile.getAbsolutePath());
                }
            }
        }
        file.delete();
    }
}
