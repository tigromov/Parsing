import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ExelMid {

    public ExelMid() throws FileNotFoundException {
        Workbook wbMid = new XSSFWorkbook();
        Sheet midSheet = wbMid.createSheet();
        FileOutputStream fos = new FileOutputStream("C:/Rest/140322.xls");
    }
}
