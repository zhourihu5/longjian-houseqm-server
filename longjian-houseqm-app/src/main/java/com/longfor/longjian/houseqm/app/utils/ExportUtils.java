package com.longfor.longjian.houseqm.app.utils;

import com.longfor.longjian.houseqm.app.vo.issuelist.ExcelIssueData;
import com.longfor.longjian.houseqm.util.DateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.apache.poi.ss.usermodel.VerticalAlignment.CENTER;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.utils
 * @ClassName: ExportUtils
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/2/16 13:35
 */
@Component
public class ExportUtils {

    @Value("${export_path}")
    private static String EXPORT_PATH;

    private static List<String> COL_NAME_LIST = Arrays.asList(new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
            "AA", "AB", "AC", "AD", "AE", "AF", "AG", "AH", "AI", "AJ", "AK", "AL", "AM", "AN", "AO", "AP", "AQ", "AR", "AS", "AT", "AU", "AV", "AW", "AX", "AY", "AZ",
            "BA", "BB", "BC", "BD", "BE", "BF", "BG", "BH", "BI", "BJ", "BK", "BL", "BM", "BN", "BO", "BP", "BQ", "BR", "BS", "BT", "BU", "BV", "BW", "BX", "BY", "BZ"});


    public static SXSSFWorkbook exportExcel(List<ExcelIssueData> data, boolean condition_open) {
        //File file = FileUtil.createFile(path);
        //OutputStream out = new FileOutputStream(file);
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        SXSSFSheet sheet = workbook.createSheet();

        CellStyle cellStyle = workbook.createCellStyle();
        Font base_font = workbook.createFont();

        base_font.setFontName("宋体");//字体
        base_font.setBold(true);//加粗
        base_font.setFontHeightInPoints((short) 14);
        cellStyle.setFont(base_font);
        cellStyle.setVerticalAlignment(CENTER);//垂直居中
        cellStyle.setWrapText(true);//文字换行
        cellStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);

        sheet.setColumnWidth(getColumnIndexByName("B"), 25 * 256);
        sheet.setColumnWidth(getColumnIndexByName("D"), 30 * 256);
        sheet.setColumnWidth(getColumnIndexByName("I"), 40 * 256);
        sheet.setColumnWidth(getColumnIndexByName("L"), 20 * 256);
        sheet.setColumnWidth(getColumnIndexByName("M"), 25 * 256);
        sheet.setColumnWidth(getColumnIndexByName("N"), 20 * 256);
        sheet.setColumnWidth(getColumnIndexByName("O"), 25 * 256);
        sheet.setColumnWidth(getColumnIndexByName("P"), 20 * 256);
        sheet.setColumnWidth(getColumnIndexByName("Q"), 25 * 256);
        sheet.setColumnWidth(getColumnIndexByName("R"), 25 * 256);
        sheet.setColumnWidth(getColumnIndexByName("U"), 100 * 256);
        sheet.setColumnWidth(getColumnIndexByName("S"), 20 * 256);
        sheet.setColumnWidth(getColumnIndexByName("T"), 25 * 256);

        //表头
        List<String> title = new ArrayList<>();
        title.addAll(Arrays.asList(new String[]{"问题ID", "任务名称'", "问题状态", "位置", "楼栋", "楼层", "户",
                "房间", "任务类型"}));
        if (condition_open) {
            title.add("严重程度");
        }
        title.addAll(Arrays.asList(new String[]{"是否超期", "检查人", "检查时间", "分配人", "分配时间", "整改人",
                "整改截止时间", "整改完成时间", "销项人", "销项时间", "问题描述", "检查项"}));

        SXSSFRow row0 = sheet.createRow(0);
        for (int i = 0; i < title.size(); i++) {
            SXSSFCell cell = row0.createCell(i);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(title.get(i));
        }
        //表中数据
        for (int rowInx = 1; rowInx < data.size(); rowInx++) {
            ExcelIssueData issue = data.get(rowInx - 1);
            SXSSFRow row = sheet.createRow(rowInx);
            int curColumn = 0;//单元格 0
            SXSSFCell cell = row.createCell(curColumn++);
            cell.setCellValue(issue.getIssue_id());

            cell = row.createCell(curColumn++);
            cell.setCellValue(issue.getTask_name());

            cell = row.createCell(curColumn++);
            cell.setCellValue(issue.getStatus_name());

            cell = row.createCell(curColumn++);
            cell.setCellValue(issue.getArea_path().size() > 0 ? String.join("/", issue.getArea_path()) : "");

            cell = row.createCell(curColumn++);
            cell.setCellValue(issue.getArea_path().size() > 0 ? issue.getArea_path().get(0) : "");

            cell = row.createCell(curColumn++);
            cell.setCellValue(issue.getArea_path().size() > 1 ? issue.getArea_path().get(1) : "");

            cell = row.createCell(curColumn++);
            cell.setCellValue(issue.getArea_path().size() > 2 ? issue.getArea_path().get(2) : "");

            cell = row.createCell(curColumn++);
            cell.setCellValue(issue.getArea_path().size() > 3 ? issue.getArea_path().get(3) : "");

            cell = row.createCell(curColumn++);
            cell.setCellValue(issue.getCategory_name());

            if (condition_open) {
                cell = row.createCell(curColumn++);
                cell.setCellValue(issue.getCondition_name());
            }

            cell = row.createCell(curColumn++);
            cell.setCellValue(issue.getIs_overdue() ? "超期" : "");

            cell = row.createCell(curColumn++);
            cell.setCellValue(issue.getChecker());

            cell = row.createCell(curColumn++);
            cell.setCellValue(issue.getCheck_at());

            cell = row.createCell(curColumn++);
            cell.setCellValue(issue.getAssigner());

            cell = row.createCell(curColumn++);
            cell.setCellValue(issue.getAssign_at());

            cell = row.createCell(curColumn++);
            cell.setCellValue(issue.getRepairer());

            cell = row.createCell(curColumn++);
            cell.setCellValue(issue.getRepair_plan_end_on());

            cell = row.createCell(curColumn++);
            cell.setCellValue(issue.getRepair_end_on());

            cell = row.createCell(curColumn++);
            cell.setCellValue(issue.getDestroy_user());

            cell = row.createCell(curColumn++);
            cell.setCellValue(issue.getDestroy_at());

            cell = row.createCell(curColumn++);
            cell.setCellValue(issue.getContent());

            for (String checkItem : issue.getCheck_item()) {
                cell = row.createCell(curColumn++);
                cell.setCellValue(checkItem);
            }

        }
        // dt = datetime.datetime.strftime(datetime.datetime.now(), '%m%d%H%M%S')
        //    r = '%04x' % random.randint(0, 65536)
        //    pathname = '%s/export_issue_excel_%s_%s.xlsx' % (config.EXPORT_PATH, dt, r)
        //    wb.save(pathname)
        String dt = DateUtil.getNowTimeStr("yyyyMMddHHmmss");
        String r = new Random().ints(0, 65536).toString();
        String pathname = String.format("%s/export_issue_excel_%s_%s.xlsx", EXPORT_PATH, dt, r);

        return workbook;
    }

    private static int getColumnIndexByName(String name) {
        return COL_NAME_LIST.indexOf(name);
    }

    private static XSSFCell getCell(String address, XSSFSheet sheet) {//需先创建单元格才能获取
        CellAddress ca = new CellAddress(address);
        XSSFRow row = sheet.getRow(ca.getRow());
        XSSFCell cell = row.getCell(ca.getColumn());
        if (cell == null) {
            cell = row.createCell(ca.getColumn());
        }
        return cell;
    }

}
