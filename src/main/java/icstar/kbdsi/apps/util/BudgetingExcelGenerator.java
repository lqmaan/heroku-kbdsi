package icstar.kbdsi.apps.util;

import icstar.kbdsi.apps.models.Budgeting;
import icstar.kbdsi.apps.models.Transaction;
import icstar.kbdsi.apps.services.CategoryService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.number.CurrencyStyleFormatter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class BudgetingExcelGenerator {
    private List<Budgeting> budgetingList;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    private static final String PATTERN_FORMAT = "dd/MM/yyyy HH:mm";


    public BudgetingExcelGenerator(List<Budgeting> budgetingList){
        this.budgetingList = budgetingList;
        workbook = new XSSFWorkbook();
    }


    private void writeHeader(){
        sheet = workbook.createSheet("Budgeting");
        Row row = sheet.createRow(0);
        CellStyle style =  workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(18);
        style.setFont(font);
        style.setWrapText(true);
        createCell(row, 0, "No", style);
        createCell(row, 1, "ID", style);
        createCell(row, 2, "Description", style);
        createCell(row, 3, "Category", style);
        createCell(row, 4, "Type", style);
        createCell(row, 5, "Amount", style);
        createCell(row, 6, "CreatedAt", style);
        createCell(row, 7, "UpdatedAt", style);
        createCell(row, 8, "CreatedBy", style);
        createCell(row, 9, "UpdatedBy", style);

    }

    private void createCell(Row row, int columnCount, Object valueofCell, CellStyle style){
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if(valueofCell instanceof Integer){
            cell.setCellValue((Integer) valueofCell);
        } else if(valueofCell instanceof Long){
            cell.setCellValue((Long) valueofCell);
        }
        else if(valueofCell instanceof String){
            cell.setCellValue((String) valueofCell);
        }
        else if(valueofCell instanceof Boolean){
            cell.setCellValue((Boolean) valueofCell);
        }
        cell.setCellStyle(style);
    }

    private void write(){
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        style.setWrapText(true);
        SimpleDateFormat dateFormatGmt7 = new SimpleDateFormat("yyyy-MM-dd HH:mm");


        int num = 0;
        for(Budgeting item:budgetingList){
            Row row = sheet.createRow(rowCount++);
            int columnCount= 0;
            createCell(row, columnCount++, ++num , style);
            createCell(row, columnCount++, item.getBudgetId(), style);
            createCell(row, columnCount++, item.getDescription(), style);
            createCell(row, columnCount++, item.getCategory(), style);
            createCell(row, columnCount++, item.getType(), style);
            String rupiahFormat = new CurrencyStyleFormatter().print(item.getAmount(), new Locale("id", "ID"));
            createCell(row, columnCount++, rupiahFormat, style);
            String formatCreated = dateFormatGmt7.format(item.getCreatedAt());
            createCell(row, columnCount++, Convert.ConvertToLocalTime(formatCreated,"Asia/Jakarta"), style);
            String formatUpdated = dateFormatGmt7.format(item.getUpdatedAt());
            createCell(row, columnCount++, Convert.ConvertToLocalTime(formatUpdated,"Asia/Jakarta"), style);
            createCell(row, columnCount++, item.getCreatedBy(), style);
            createCell(row, columnCount++, item.getUpdatedBy(), style);

        }
    }

    public void generateExcelFile(HttpServletResponse response) throws IOException {
        writeHeader();
        write();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
