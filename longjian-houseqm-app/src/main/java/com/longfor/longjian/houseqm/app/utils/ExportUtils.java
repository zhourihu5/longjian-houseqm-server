package com.longfor.longjian.houseqm.app.utils;

import com.longfor.longjian.houseqm.app.vo.ExportReplyDetail;
import com.longfor.longjian.houseqm.app.vo.export.NodeVo;
import com.longfor.longjian.houseqm.app.vo.houseqmstat.InspectionHouseStatusInfoVo;
import com.longfor.longjian.houseqm.app.vo.issuelist.ExcelIssueData;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlToken;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.apache.poi.ss.usermodel.VerticalAlignment.CENTER;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.utils
 * @ClassName: ExportUtils
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/2/16 13:35
 */
@Slf4j
@Component
public class ExportUtils {

    private static final String WORKBOOK = "workbook";
    private static final String SHEET = "sheet";
    private static final String CELL_STYLE = "cellStyle";
    private static final String UTF_8 = "utf-8";
    @Value("${export_path}")
    private static String exportPath;
    private static List<String> colNameList = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
            "AA", "AB", "AC", "AD", "AE", "AF", "AG", "AH", "AI", "AJ", "AK", "AL", "AM", "AN", "AO", "AP", "AQ", "AR", "AS", "AT", "AU", "AV", "AW", "AX", "AY", "AZ",
            "BA", "BB", "BC", "BD", "BE", "BF", "BG", "BH", "BI", "BJ", "BK", "BL", "BM", "BN", "BO", "BP", "BQ", "BR", "BS", "BT", "BU", "BV", "BW", "BX", "BY", "BZ");

    private ExportUtils() {

    }

    private static Map<String, Object> initWorkbook() {
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        SXSSFSheet sheet = workbook.createSheet();

        CellStyle cellStyle = workbook.createCellStyle();
        Font baseFont = workbook.createFont();

        baseFont.setFontName("宋体");//字体
        baseFont.setBold(true);//加粗
        baseFont.setFontHeightInPoints((short) 14);
        cellStyle.setFont(baseFont);
        cellStyle.setVerticalAlignment(CENTER);//垂直居中
        cellStyle.setWrapText(true);//文字换行
        cellStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        Map<String, Object> result = new HashMap<>();
        result.put(WORKBOOK, workbook);
        result.put(SHEET, sheet);
        result.put(CELL_STYLE, cellStyle);
        return result;
    }

    // 导出excel 不带图片 问题列表
    @SuppressWarnings("squid:S3776")
    public static SXSSFWorkbook exportExcel(List<ExcelIssueData> data, boolean conditionOpen) {

        Map<String, Object> initParam = initWorkbook();
        SXSSFWorkbook workbook = (SXSSFWorkbook) initParam.get(WORKBOOK);
        SXSSFSheet sheet = (SXSSFSheet) initParam.get(SHEET);
        CellStyle cellStyle = (CellStyle) initParam.get(CELL_STYLE);
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
        title.addAll(Arrays.asList("问题ID", "任务名称'", "问题状态", "位置", "楼栋", "楼层", "户",
                "房间", "任务类型"));
        if (conditionOpen) {
            title.add("严重程度");
        }
        title.addAll(Arrays.asList("是否超期", "检查人", "检查时间", "分配人", "分配时间", "整改人",
                "整改截止时间", "整改完成时间", "销项人", "销项时间", "问题描述", "检查项"));

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

            if (conditionOpen) {
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


        return workbook;
    }

    //导出 整改回复单
    @SuppressWarnings("squid:S3776")
    public static XWPFDocument exportRepairReply(ExportReplyDetail data) throws IOException, InvalidFormatException {
        String templateNotify = "/templates/reply_template.docx";
        InputStream fis = ExportUtils.class.getResourceAsStream(templateNotify);
        XWPFDocument doc = new XWPFDocument(fis);
        List<XWPFTable> tables = doc.getTables();
        XWPFTable table = tables.get(0);

        table.getRow(0).getCell(1).setText(data.getTask_name());
        table.getRow(0).getCell(3).setText(data.getCheck_item_name());
        //
        table.getRow(2).getCell(0).setText("上次检查问题整改复查情况：");
        for (int index = 0; index < data.getIssue_detail().size(); index++) {
            ExportReplyDetail.ExportIssueDetail issue = data.getIssue_detail().get(index);

            XWPFTableCell cell = table.getRow(2).getCell(0);
            XWPFParagraph pIO = cell.addParagraph();
            XWPFRun rIO = pIO.createRun();
            rIO.setText(String.format("%d、问题：%s", index + 1, issue.getQues_content().replace("\n", " ")));
            if (issue.getAnsw_content().length() > 0) {
                XWPFParagraph pI2 = cell.addParagraph();
                XWPFRun rI2 = pI2.createRun();
                rI2.setText(String.format("%s回复：%s", (index + 1 < 10 ? "   " : "    "), issue.getAnsw_content().replace("\n", " ")));
                if (CollectionUtils.isNotEmpty(issue.getAnsw_attachment_path())) {
                    XWPFParagraph pI3 = cell.addParagraph();
                    XWPFRun rI3 = pI3.createRun();
                    rI3.setText((index + 1 < 10 ? "   " : "    "));
                    for (String attachment : issue.getAnsw_attachment_path()) {
                        //图片 attachment: pictures/3a98d018fb666e1fd3c41575fa20659b.png
                        // 读取图片 加载到doc中
                        File file = new File(attachment);
                        FileInputStream is = new FileInputStream(file);
                        String pictureData = doc.addPictureData(is, XWPFDocument.PICTURE_TYPE_PNG);
                        createPicture(doc, pictureData, doc.getNextPicNameNumber(XWPFDocument.PICTURE_TYPE_PNG), 96, 96,pI3);
                        FileOutputStream fos = new FileOutputStream(file);
                        doc.write(fos);
                        fos.close();
                    }
                }
            } else {
                if (CollectionUtils.isNotEmpty(issue.getAnsw_attachment_path()) && issue.getAnsw_attachment_path().size() > 0) {
                    XWPFParagraph pI2 = cell.addParagraph();
                    XWPFRun rI2 = pI2.createRun();
                    rI2.setText(String.format("%s回复：", (index + 1 < 10 ? "   " : "    ")));
                    for (String attachment : issue.getAnsw_attachment_path()) {
                        //图片
                        // 读取图片 加载到doc中
                        File file = new File(attachment);
                        FileInputStream is = new FileInputStream(file);
                        String pictureData = doc.addPictureData(is, XWPFDocument.PICTURE_TYPE_PNG);
                        createPicture(doc, pictureData, doc.getNextPicNameNumber(XWPFDocument.PICTURE_TYPE_PNG), 96, 96,pI2);
                        FileOutputStream fos = new FileOutputStream(file);
                        doc.write(fos);
                        fos.close();
                    }
                } else {
                    XWPFParagraph pI2 = cell.addParagraph();
                    XWPFRun rI2 = pI2.createRun();
                    rI2.setText(String.format("%s回复：", (index + 1 < 10 ? "   " : "    ")));
                }

            }
        }

        return doc;
    }

    // 导出 统计报告 -任务概况 -验房详情 导出excel 使用freemaker
    public static void exportStatExcel(String templateName, Map<String, Object> data, HttpServletResponse response) throws IOException, TemplateException {
        // 加载模板
        File file = new File("temp.xlsx");// 临时名称
        Configuration configuration = new Configuration();
        configuration.setDefaultEncoding(UTF_8);
        configuration.setClassForTemplateLoading(ExportUtils.class, "/templates");
        Template template = configuration.getTemplate(templateName, UTF_8);
        // 填充数据至Excel
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), UTF_8);
        template.process(data, writer);
        // 关闭文件流
        writer.close();
        // 导出文件
        FileUtil.load(file.getAbsolutePath(), response);
        Files.delete(Paths.get(file.toURI()));
    }

    public static SXSSFWorkbook exportInspectionSituationExcel(List<InspectionHouseStatusInfoVo> data) {
        Map<String, Object> initParam = initWorkbook();
        SXSSFWorkbook workbook = (SXSSFWorkbook) initParam.get(WORKBOOK);
        SXSSFSheet sheet = (SXSSFSheet) initParam.get(SHEET);
        CellStyle titilecellStyle = (CellStyle) initParam.get(CELL_STYLE);

        CellStyle cellStyle = workbook.createCellStyle();
        Font baseFont1 = workbook.createFont();
        baseFont1.setFontName("宋体");
        baseFont1.setBold(false);
        baseFont1.setFontHeightInPoints((short) 10.5);
        cellStyle.setFont(baseFont1);
        cellStyle.setVerticalAlignment(CENTER);//垂直居中
        cellStyle.setWrapText(true);//文字换行
        cellStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);


        sheet.setColumnWidth(getColumnIndexByName("A"), 9 * 256);
        sheet.setColumnWidth(getColumnIndexByName("B"), 9 * 256);
        sheet.setColumnWidth(getColumnIndexByName("C"), 9 * 256);
        sheet.setColumnWidth(getColumnIndexByName("D"), 9 * 256);
        sheet.setColumnWidth(getColumnIndexByName("E"), 7 * 256);
        sheet.setColumnWidth(getColumnIndexByName("F"), 7 * 256);
        sheet.setColumnWidth(getColumnIndexByName("G"), 7 * 256);

        //表头
        List<String> title = new ArrayList<>();
        title.addAll(Arrays.asList("楼栋", "楼层", "户名称", "户状态", "问题数", "整改数", "销项数"));

        SXSSFRow row0 = sheet.createRow(0);
        row0.setHeightInPoints((float) 31.2);

        for (int i = 0; i < title.size(); i++) {
            SXSSFCell cell = row0.createCell(i);
            cell.setCellStyle(titilecellStyle);
            cell.setCellValue(title.get(i));
        }

        //表中数据
        for (int rowInx = 0; rowInx < data.size(); rowInx++) {
            InspectionHouseStatusInfoVo issue = data.get(rowInx);
            SXSSFRow row = sheet.createRow(rowInx + 1);//第二行
            row.setHeightInPoints((float) 14.4);
            int curColumn = 0;//单元格 0

            List<String> areaPathName = issue.getAreaPathName();
            SXSSFCell cell = row.createCell(curColumn++);
            cell.setCellStyle(cellStyle);
            if (CollectionUtils.isNotEmpty(areaPathName)) {
                cell.setCellValue(areaPathName.get(0));
                cell = row.createCell(curColumn++);
                cell.setCellStyle(cellStyle);
                if (areaPathName.size() > 1) cell.setCellValue(areaPathName.get(1));
                else cell.setCellValue(areaPathName.get(0));
            } else {
                cell.setCellValue("");
                cell = row.createCell(curColumn++);
                cell.setCellStyle(cellStyle);
                cell.setCellValue("");
            }

            cell = row.createCell(curColumn++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(issue.getAreaName());

            cell = row.createCell(curColumn++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(issue.getStatusName());

            cell = row.createCell(curColumn++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(issue.getIssueCount());

            cell = row.createCell(curColumn++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(issue.getIssueRepairedCount());

            cell = row.createCell(curColumn);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(issue.getIssueApprovededCount());

        }
        return workbook;

    }


    private static int getColumnIndexByName(String name) {
        return colNameList.indexOf(name);
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

    public static void createPicture(XWPFDocument doc, String blipId, int id, int width, int height,XWPFParagraph paragraph) {
        final int EMU = 9525;
        width *= EMU;
        height *= EMU;
        CTInline inline = paragraph.createRun().getCTR().addNewDrawing().addNewInline();

        String picXml = "" +
                "<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">" +
                "   <a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">" +
                "      <pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">" +
                "         <pic:nvPicPr>" +
                "            <pic:cNvPr id=\"" + id + "\" name=\"Generated\"/>" +
                "            <pic:cNvPicPr/>" +
                "         </pic:nvPicPr>" +
                "         <pic:blipFill>" +
                "            <a:blip r:embed=\"" + blipId + "\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"/>" +
                "            <a:stretch>" +
                "               <a:fillRect/>" +
                "            </a:stretch>" +
                "         </pic:blipFill>" +
                "         <pic:spPr>" +
                "            <a:xfrm>" +
                "               <a:off x=\"0\" y=\"0\"/>" +
                "               <a:ext cx=\"" + width + "\" cy=\"" + height + "\"/>" +
                "            </a:xfrm>" +
                "            <a:prstGeom prst=\"rect\">" +
                "               <a:avLst/>" +
                "            </a:prstGeom>" +
                "         </pic:spPr>" +
                "      </pic:pic>" +
                "   </a:graphicData>" +
                "</a:graphic>";

        XmlToken xmlToken = null;
        try {
            xmlToken = XmlToken.Factory.parse(picXml);
        } catch (XmlException xe) {
            log.error("error:" + xe);
        }
        inline.set(xmlToken);
        inline.setDistT(0);
        inline.setDistB(0);
        inline.setDistL(0);
        inline.setDistR(0);

        CTPositiveSize2D extent = inline.addNewExtent();
        extent.setCx(width);
        extent.setCy(height);

        CTNonVisualDrawingProps docPr = inline.addNewDocPr();
        docPr.setId(id);
        docPr.setName("Picture " + id);
        docPr.setDescr("Generated");
    }


    public static SXSSFWorkbook exportIssueStatisticExcel(List<NodeVo> nodeTree, int maxCol) {
        Map<String, Object> initParam = initWorkbook();
        SXSSFWorkbook workbook = (SXSSFWorkbook) initParam.get(WORKBOOK);
        SXSSFSheet sheet = (SXSSFSheet) initParam.get(SHEET);
        int curRow = 0;
        int curColumn = 0;
        //创建行
        SXSSFRow row = sheet.createRow(curRow);
        for (int i = 0; i < maxCol; i++) {
            //创捷列
            SXSSFCell cell = row.createCell(curColumn + i);
            //内容
            if (i == 0) {
                cell.setCellValue("问题项");
            } else {
                cell.setCellValue("细项");
            }
            SXSSFCell cell2 = row.createCell(curColumn + i + 1);
            cell2.setCellValue("问题数");
            curColumn += 1;

        }
        curRow = 1;
        int curCol = 0;
        exportTree(workbook, sheet, nodeTree, curRow, curCol);

        return workbook;

    }

    private static void exportTree(SXSSFWorkbook workbook, SXSSFSheet sheet, List<NodeVo> tree, int row, int col) {
        for (NodeVo node : tree) {
            int endRow = row + node.getData().getChild_count() - 1;
            int endColumn = col;
            log.info("row={},endRow={},col={},endColumn={}", row, endRow, col, endColumn);
            //合并单元格
            //1：开始行 2：结束行  3：开始列 4：结束列
            if (endRow > row) {
                CellRangeAddress region = new CellRangeAddress(row, endRow, col, endColumn);
                sheet.addMergedRegion(region);
            }
            SXSSFRow row1 = sheet.getRow(row);
            if (row1 == null) {
                //创建行
                row1 = sheet.createRow(row);
            }
            //创捷列
            SXSSFCell cell = row1.createCell(col);
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setVerticalAlignment(CENTER);//垂直居中
            cell.setCellValue(node.getData().getName());
            log.info("cell.getStringCellValue={}", cell.getStringCellValue());
            //合并
            if (endRow > row) {
                sheet.addMergedRegion(new CellRangeAddress(row, endRow, col + 1, endColumn + 1));
            }

            //创捷列
            SXSSFCell cell1 = row1.createCell(col + 1);
            CellStyle cellStyles = workbook.createCellStyle();
            cellStyles.setVerticalAlignment(CENTER);//垂直居中
            cell1.setCellValue(String.valueOf(node.getData().getIssue_count()));
            if (CollectionUtils.isNotEmpty(node.getChild_list())) {
                exportTree(workbook, sheet, node.getChild_list(), row, col + 2);
            }
            row += node.getData().getChild_count();

        }

    }
}
