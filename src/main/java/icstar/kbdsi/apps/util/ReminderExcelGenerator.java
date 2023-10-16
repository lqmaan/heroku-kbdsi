package icstar.kbdsi.apps.util;

import icstar.kbdsi.apps.models.Budgeting;
import icstar.kbdsi.apps.models.Reminder;
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
import icstar.kbdsi.apps.util.Convert;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReminderExcelGenerator {

    private List<Reminder> reminderList;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet1, sheet2, sheet3;

    private static final String PATTERN_FORMAT = "dd/MM/yyyy";

    private Convert convert;

    @Autowired
    private CategoryService categoryService;

    public ReminderExcelGenerator(List<Reminder> reminderList){
        this.reminderList = reminderList;
        workbook = new XSSFWorkbook();
    }


    private void writeHeader(){
        sheet1 = workbook.createSheet("Reminder");
        Row row = sheet1.createRow(0);
        CellStyle style =  workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(18);
        style.setFont(font);
        style.setWrapText(true);
        createCell(row, 0, "No", style);
        createCell(row, 1, "ID", style);
        createCell(row, 2, "Description", style);
        createCell(row, 3, "Email", style);
        createCell(row, 4, "Amount", style);
        createCell(row, 5, "Status", style);
        createCell(row, 6, "Schedule Date", style);
        createCell(row, 7, "Payment Date", style);
        createCell(row, 8, "CreatedAt", style);
        createCell(row, 9, "CreatedBy", style);
//        createCell(row, 10, "UpdatedBy", style);

    }

    private void createCell(Row row, int columnCount, Object valueofCell, CellStyle style){
        sheet1.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if(valueofCell instanceof Integer){
            cell.setCellValue((Integer) valueofCell);
        } else if(valueofCell instanceof Long){
            cell.setCellValue((Long) valueofCell);
        }
        else if(valueofCell instanceof String){
            cell.setCellValue((String) valueofCell);
        }
        else if(valueofCell instanceof Date){
            cell.setCellValue((Date) valueofCell);
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
        DateFormat formatter = new SimpleDateFormat(PATTERN_FORMAT);

        int num = 0;
        for(Reminder item:reminderList){
            Row row = sheet1.createRow(rowCount++);
            int columnCount= 0;
            createCell(row, columnCount++, ++num , style);
            createCell(row, columnCount++, item.getReminderId(), style);
            createCell(row, columnCount++, item.getDescription(), style);
            createCell(row, columnCount++, item.getEmail(), style);

            String rupiahFormat = new CurrencyStyleFormatter().print(item.getAmount(), new Locale("id", "ID"));
            createCell(row, columnCount++, rupiahFormat, style);
            createCell(row, columnCount++, item.getStatus(), style);

            String formattedSchedule = formatter.format(item.getScheduleDate());
            createCell(row, columnCount++, formattedSchedule, style);

            String formattedPayment = formatter.format(item.getPaymentDate());
            createCell(row, columnCount++, formattedPayment, style);

            String formattedCreated = formatter.format(item.getCreatedAt());;
            createCell(row, columnCount++, formattedCreated, style);

            createCell(row, columnCount++, item.getCreatedBy(), style);
//            createCell(row, columnCount++, item.getUpdatedBy(), style);

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
