package pro.bzy.boot.framework.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import lombok.Cleanup;
import lombok.NonNull;
import pro.bzy.boot.framework.utils.parents.MyUtil;


/**
 * 
 * excel工具
 * @author user
 *
 */
public class ExcelUtil implements MyUtil {
    
    
    /**
     * 导出excel
     * @param excelFile 导出的excel文件路径名
     * @param titles 标题列表
     * @param rows 行数据
     * @return
     * @throws IOException
     */
    public static File createExcel(@NonNull final String excelFile, 
            List<String> titles, List<List<String>> rows) throws IOException {
        // 1. 参数校验
        ExceptionCheckUtil.hasText(excelFile, "导出Excel文件的名称未指定");
        
        // 2. 初始化workbook和sheet
        @Cleanup HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("SHEET1");
        
        // 3. 填充数据
        // 3.1 填充数据头
        int rowIndex = 0;
        if (CollectionUtil.isNotEmpty(titles)) {
            HSSFRow rowhead = sheet.createRow(rowIndex++);
            int titleSize = titles.size();
            for (int colIndex = 0; colIndex < titleSize; colIndex++) {
                rowhead.createCell(colIndex).setCellValue(titles.get(colIndex));
            }
        }
        // 3.2 填充数据体
        if (CollectionUtil.isNotEmpty(rows)) {
            int rowEndIndex = rows.size() + 1;
            for (; rowIndex < rowEndIndex; rowIndex++) {
                HSSFRow rowBody = sheet.createRow(rowIndex);
                List<String> rowItem = rows.get(rowIndex - 1);
                int colEndIndex = rowItem.size();
                for (int colIndex = 0; colIndex < colEndIndex; colIndex++) {
                    rowBody.createCell(colIndex).setCellValue(rowItem.get(colIndex));
                }
            }
        }
        
        // 4. 列宽自适应
        HSSFRow row = workbook.getSheetAt(0).getRow(0);
        if (row != null) {
            for(int colNum=0; colNum < row.getLastCellNum(); colNum++){
                workbook.getSheetAt(0).autoSizeColumn(colNum);
            }
        }
        
        // 5. 导出excel
        File file = new File(excelFile);
        if (!file.exists())
            file.createNewFile();
        @Cleanup FileOutputStream fileOut = new FileOutputStream(file);
        workbook.write(fileOut);
        return file;
    }
    
}
