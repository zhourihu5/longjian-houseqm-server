package com.longfor.longjian.houseqm.app.utils;

import com.longfor.longjian.houseqm.app.vo.ExportReplyDetail;
import com.longfor.longjian.houseqm.app.vo.issuelist.ExcelIssueData;
import com.longfor.longjian.houseqm.util.DateUtil;
import com.longfor.longjian.houseqm.utils.ExampleUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
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

import javax.annotation.Resource;
import java.io.*;
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
        String dt = DateUtil.getNowTimeStr("MMddHHmmss");
        String r = new Random().ints(0, 65536).toString();
        String pathname = String.format("%s/export_issue_excel_%s_%s.xlsx", EXPORT_PATH, dt, r);

        return workbook;
    }

    //导出 整改回复单
    public static XWPFDocument exportRepairReply(ExportReplyDetail data) throws Exception {
        String template_notify = "templates/reply_template.docx";
        String dt = DateUtil.getNowTimeStr("MMddHHmmss");
        String r = new Random().ints(0, 65536).toString();
        String file_path = String.format("%s/buildingqm_report/reply_report_%s_%s", EXPORT_PATH, dt, r);

        XWPFDocument doc = new XWPFDocument(POIXMLDocument.openPackage(template_notify));
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
                if (issue.getAnsw_attachment_path().size() > 0) {
                    XWPFParagraph pI3 = cell.addParagraph();
                    XWPFRun rI3 = pI3.createRun();
                    rI3.setText((index + 1 < 10 ? "   " : "    "));
                    for (String attachment : issue.getAnsw_attachment_path()) {
                        //图片 attachment: pictures/3a98d018fb666e1fd3c41575fa20659b.png
                        // 读取图片 加载到doc中
                        File file = new File(attachment);
                        FileInputStream is = new FileInputStream(file);
                        String pictureData = doc.addPictureData(is, XWPFDocument.PICTURE_TYPE_PNG);
                        CTInline ctInline = pI3.createRun().getCTR().addNewDrawing().addNewInline();
                        createPicture(doc,pictureData, doc.getNextPicNameNumber(XWPFDocument.PICTURE_TYPE_PNG), 254, 254);
                        FileOutputStream fos = new FileOutputStream(file);
                        doc.write(fos);
                        fos.close();
                    }
                }
            } else {
                if (issue.getAnsw_attachment_path().size() > 0) {
                    XWPFParagraph pI2 = cell.addParagraph();
                    XWPFRun rI2 = pI2.createRun();
                    rI2.setText(String.format("%s回复：", (index + 1 < 10 ? "   " : "    ")));
                    for (String attachment : issue.getAnsw_attachment_path()) {
                        //图片
                        // 读取图片 加载到doc中
                        File file = new File(attachment);
                        FileInputStream is = new FileInputStream(file);
                        String pictureData = doc.addPictureData(is, XWPFDocument.PICTURE_TYPE_PNG);
                        CTInline ctInline = pI2.createRun().getCTR().addNewDrawing().addNewInline();
                        createPicture(doc,pictureData, doc.getNextPicNameNumber(XWPFDocument.PICTURE_TYPE_PNG), 254, 254);
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

        String dt1 = DateUtil.getNowTimeStr("MMddHHmmss");
        String r1 = new Random().ints(0, 65536).toString();
        String pathname = String.format("%s/repair_reply_%s_%s_report.docx", file_path, dt1, r1);

        return doc;
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

    private static void insertPicture(String blipId, XWPFDocument document, String filePath,
                                      CTInline inline, int width,
                                      int height) throws Exception {
        document.addPictureData(new FileInputStream(filePath), XWPFDocument.PICTURE_TYPE_PNG);
        int id = document.getAllPictures().size() - 1;
        final int EMU = 9525;
        width *= EMU;
        height *= EMU;
        //String blipId = document.getAllPictures().get(id).getPackageRelationship().getId();
        String picXml = getPicXml(blipId, width, height);
        XmlToken xmlToken = null;
        try {
            xmlToken = XmlToken.Factory.parse(picXml);
        } catch (XmlException xe) {
            xe.printStackTrace();
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
        docPr.setName("IMG_" + id);
        docPr.setDescr("IMG_" + id);
    }

    private static String getPicXml(String blipId, int width, int height) {
        String picXml =
                "" + "<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">" +
                        "   <a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">" +
                        "      <pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">" +
                        "         <pic:nvPicPr>" + "            <pic:cNvPr id=\"" + 0 +
                        "\" name=\"Generated\"/>" + "            <pic:cNvPicPr/>" +
                        "         </pic:nvPicPr>" + "         <pic:blipFill>" +
                        "            <a:blip r:embed=\"" + blipId +
                        "\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"/>" +
                        "            <a:stretch>" + "               <a:fillRect/>" +
                        "            </a:stretch>" + "         </pic:blipFill>" +
                        "         <pic:spPr>" + "            <a:xfrm>" +
                        "               <a:off x=\"0\" y=\"0\"/>" +
                        "               <a:ext cx=\"" + width + "\" cy=\"" + height +
                        "\"/>" + "            </a:xfrm>" +
                        "            <a:prstGeom prst=\"rect\">" +
                        "               <a:avLst/>" + "            </a:prstGeom>" +
                        "         </pic:spPr>" + "      </pic:pic>" +
                        "   </a:graphicData>" + "</a:graphic>";
        return picXml;
    }


    public static void createPicture(XWPFDocument doc,String blipId,int id, int width, int height) {
        final int EMU = 9525;
        width *= EMU;
        height *= EMU;
        //String blipId = getAllPictures().get(id).getPackageRelationship().getId();

        CTInline inline = doc.createParagraph().createRun().getCTR().addNewDrawing().addNewInline();

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

        //CTGraphicalObjectData graphicData = inline.addNewGraphic().addNewGraphicData();
        XmlToken xmlToken = null;
        try {
            xmlToken = XmlToken.Factory.parse(picXml);
        } catch(XmlException xe) {
            xe.printStackTrace();
        }
        inline.set(xmlToken);
        //graphicData.set(xmlToken);

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

}
