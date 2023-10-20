package icstar.kbdsi.apps.util;

import icstar.kbdsi.apps.models.User;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import static org.apache.poi.ss.util.CellUtil.createCell;

public class UserExcelGenerator {
    private List<User> userList;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;


    private static final String PATTERN_FORMAT = "dd/MM/yyyy HH:mm";

    public UserExcelGenerator(List<User> userList){
        this.userList = userList;
        workbook = new XSSFWorkbook();
    }

    private void writeHeader(){
        sheet = workbook.createSheet("User");
        Row row = sheet.createRow(0);
        CellStyle style =  workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        style.setWrapText(true);
        createCell(row, 0, "No", style);
        createCell(row, 1, "ID", style);
        createCell(row, 2, "Name", style);
        createCell(row, 3, "Email", style);
        createCell(row, 4, "Phone", style);
        createCell(row, 5, "Role", style);
        createCell(row, 6, "CreatedAt", style);
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
        SimpleDateFormat dateFormatGmt7 = new SimpleDateFormat("yyyy-MM-dd HH:mm");


        font.setFontHeight(14);
        style.setFont(font);
        style.setWrapText(true);


        int num = 0;
        for(User person:userList){
            Row row = sheet.createRow(rowCount++);
            int columnCount= 0;
            createCell(row, columnCount++, ++num, style);
            createCell(row, columnCount++, person.getId(), style);
            createCell(row, columnCount++, person.getName(), style);
            createCell(row, columnCount++, person.getEmail(), style);
            createCell(row, columnCount++, person.getPhone(), style);
            createCell(row, columnCount++, person.getRoles(), style);
            String formatCreated = dateFormatGmt7.format(person.getCreatedAt());
            createCell(row, columnCount++, Convert.ConvertToLocalTime(formatCreated,"Asia/Jakarta"), style);

        }
    }

    public void generateExcelFile(HttpServletResponse response) throws IOException{
        writeHeader();
        write();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
