package com.example.mianshi.Controller;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

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
@SpringBootApplication
@RestController
public class ExcelSplitterController {

    @RequestMapping(value = "/splitExcel", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public Object splitExcel(@RequestBody SplitRequest request) throws IOException {
        // 读取Excel文件
        Workbook workbook = WorkbookFactory.create(request.getExcelFile());
        Sheet sheet = workbook.getSheetAt(0);

        // 获取指定列的索引
        int columnIndex = getColumnIndex(sheet, request.getColumnName());
        if (columnIndex == -1) {
            return ResponseEntity.badRequest().body("Column not found.");
        }

        // 按指定列的值拆分数据
        Map<String, List<Row>> splitData = new HashMap<>();
        for (Row row : sheet) {
            Cell cell = row.getCell(columnIndex);
            String columnValue = cell.getStringCellValue();

            if (!splitData.containsKey(columnValue)) {
                splitData.put(columnValue, new ArrayList<>());
            }
            splitData.get(columnValue).add(row);
        }

        // 生成拆分后的Excel文件并放入ZIP文件
        ByteArrayOutputStream zipOutputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zip = new ZipOutputStream(zipOutputStream)) {
            for (String key : splitData.keySet()) {
                Workbook splitWorkbook = new XSSFWorkbook();
                Sheet splitSheet = splitWorkbook.createSheet();
                List<Row> splitRows = splitData.get(key);

                int rowIndex = 0;
                for (Row splitRow : splitRows) {
                    Row newRow = splitSheet.createRow(rowIndex++);
                    for (int cellIndex = 0; cellIndex < splitRow.getLastCellNum(); cellIndex++) {
                        Cell splitCell = splitRow.getCell(cellIndex);
                        Cell newCell = newRow.createCell(cellIndex, splitCell.getCellType());

                        switch (splitCell.getCellType()) {
                            case BLANK:
                                break;
                            case BOOLEAN:
                                newCell.setCellValue(splitCell.getBooleanCellValue());
                                break;
                            case ERROR:
                                newCell.setCellValue(splitCell.getErrorCellValue());
                                break;
                            case FORMULA:
                                newCell.setCellValue(splitCell.getCellFormula());
                                break;
                            case NUMERIC:
                                newCell.setCellValue(splitCell.getNumericCellValue());
                                break;
                            case STRING:
                                newCell.setCellValue(splitCell.getStringCellValue());
                                break;
                        }
                    }
                }

                // 将拆分后的Sheet写入临时文件
                File tempFile = File.createTempFile(key, ".xlsx");
                try (OutputStream tempFileOutputStream = new FileOutputStream(tempFile)) {
                    splitWorkbook.write(tempFileOutputStream);
                }

                // 将拆分后的Sheet文件添加到ZIP
                try (FileInputStream fileInputStream = new FileInputStream(tempFile)) {
                    zip.putNextEntry(new ZipEntry(key + ".xlsx"));
                    FileCopyUtils.copy(fileInputStream, zip);
                    zip.closeEntry();
                }

                tempFile.delete(); // 删除临时文件
            }
        }

        // 构建ZIP文件响应
        byte[] zipBytes = zipOutputStream.toByteArray();
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=split_excel.zip")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(zipBytes);
    }

    private int getColumnIndex(Sheet sheet, String columnName) {
        Row firstRow = sheet.getRow(0);
        for (Cell cell : firstRow) {
            if (cell.getStringCellValue().equals(columnName)) {
                return cell.getColumnIndex();
            }
        }
        return -1;
    }
    public class SplitRequest {
        private byte[] excelFile;
        private String columnName;

        public SplitRequest() {
        }

        public byte[] getExcelFile() {
            return excelFile;
        }

        public void setExcelFile(byte[] excelFile) {
            this.excelFile = excelFile;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }
    }

}
