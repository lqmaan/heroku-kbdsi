package icstar.kbdsi.apps.util;

import icstar.kbdsi.apps.models.Category;
import icstar.kbdsi.apps.models.Transaction;
import icstar.kbdsi.apps.models.User;
import icstar.kbdsi.apps.repository.CategoryRepository;
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
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.number.*;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class TransactionExcelGenerator {
    private List<Transaction> transactionList;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    private static final String PATTERN_FORMAT = "dd/MM/yyyy";


    @Autowired
    private CategoryService categoryService;

    public TransactionExcelGenerator(List<Transaction> transactionList){
        this.transactionList = transactionList;
        workbook = new XSSFWorkbook();
    }


    private void writeHeader(){
        sheet = workbook.createSheet("Transaction");
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
        createCell(row, 7, "CreatedBy", style);
        createCell(row, 8, "UpdatedBy", style);

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT)
                .withZone(ZoneId.systemDefault());

        int num = 0;
        for(Transaction item:transactionList){
            Row row = sheet.createRow(rowCount++);
            int columnCount= 0;
            createCell(row, ++columnCount, ++num, style);
            createCell(row, columnCount++, item.getTransactionId(), style);
            createCell(row, columnCount++, item.getDescription(), style);
//            Optional<Category> tempCategory = categoryService.getCategoryById(item.getCategory());
//            Category category = tempCategory.get();
            createCell(row, columnCount++, item.getCategory(), style);
            createCell(row, columnCount++, item.getType(), style);
            String rupiahFormat = new CurrencyStyleFormatter().print(item.getAmount(), new Locale("id", "ID"));
            createCell(row, columnCount++, rupiahFormat, style);
            String formattedInstant = formatter.format(item.getCreatedAt().toInstant());
            createCell(row, columnCount++, formattedInstant, style);
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
