package components;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author ali
 * @created_on 7/24/20
 */
public class JobSheetGenerator {
    public static class Row{
        Cell cell1;
        Cell cell2;
        Cell cell3;
        public Row(Jobsheet.Job job){
            cell1= new Cell().add(new Paragraph(job.getJobSummmery()));
            cell2= new Cell().add(new Paragraph(job.getJobDesc()));
            cell3= new Cell().add(new Paragraph(job.getJobStatus()));
        }
        public Row(){
            cell1= new Cell();
            cell2= new Cell();
            cell3= new Cell();
            cell1.setBorder(Border.NO_BORDER).setFontSize(8);
            cell2.setBorder(Border.NO_BORDER).setFontSize(8);
            cell3.setBorder(Border.NO_BORDER).setFontSize(8);

        }

        public Cell getCell1() {
            return cell1;
        }

        public void setCell1(Cell cell1) {
            this.cell1 = cell1;
        }

        public Cell getCell2() {
            return cell2;
        }

        public void setCell2(Cell cell2) {
            this.cell2 = cell2;
        }

        public Cell getCell3() {
            return cell3;
        }

        public void setCell3(Cell cell3) {
            this.cell3 = cell3;
        }

        public static void addRow(Table table, Row row){
            table.addCell(row.getCell1().setFontSize(8));
            table.addCell(row.getCell2().setFontSize(8));
            table.addCell(row.getCell3().setFontSize(8));

        }
    }
    public static void main(String[] args) {

    }

    public static void creatJobSheet(String jobSheetId, Customer customer, Vehicle vehicle) throws IOException {
        Document doc=null;
        Table table = new Table(3);
        PageSize pageSize = null;
        String jobsheetName = null;
//        table.setHorizontalAlignment(HorizontalAlignment.CENTER);

//        table.setFixedLayout();



        try {
            jobsheetName= String.format("src/doc/jobsheet-%s-%s.pdf", jobSheetId,customer.getId());
            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(jobsheetName));
            doc = new Document(pdfDoc,PageSize.A5);
            pageSize = doc.getPdfDocument().getDefaultPageSize();

            ImageData data = ImageDataFactory.create("src/resources/company_logo.png");
            Image image1 = new Image(data);
            image1.scaleAbsolute(30, 30);
            image1.setFixedPosition(13,540);

            Paragraph companyName = new Paragraph("[Company Name]").setFontSize(12).setItalic();
            Paragraph companyDetails = new Paragraph("[Street Address]\n[Phone]\n[Email]\n[Website]");
            companyName.setMarginLeft(20).setFontSize(8);
            companyDetails.setMarginLeft(20).setFontSize(8);




            Paragraph heading = new Paragraph("Jobsheet").setUnderline();
            heading.setFontSize(12).setFixedPosition(180,550,200);
            String var = String.format("Jobsheet #: %s\n Customer ID: %s\n",jobSheetId,customer.getId() );
            Paragraph jobSheetDetails = new Paragraph(var).setFontSize(8).setFixedPosition(200,505,200);

            Paragraph info = new Paragraph("Customer Info: ").setItalic().setFontSize(8)
                .setMarginLeft(20);
            String var2 = String.format("Name: %s\n Email: %s\nAddress: %s\nContact: %s\n", customer.getName(),
                customer.getEmail(),customer.getAddress(),customer.getPhone());
            Paragraph details = new Paragraph(var2).setFontSize(8)
                .setMarginLeft(20);
            String var4 = String.format("Vehicle Registration No.: %s\n Model: %s\n Chassis: %s\n Registration Date: %s",
                vehicle.getVehicle_reg(),vehicle.getModel(),vehicle.getChassis_num(),vehicle.getReg_date());
            Paragraph vehicleHeading = new Paragraph("Vehicle Details").setItalic().setFontSize(8).setFixedPosition(200,470,200);
            Paragraph vehicleDetails = new Paragraph(var4).setFontSize(8).setFixedPosition(200,410,200);;
            doc.add(vehicleHeading);
            doc.add(vehicleDetails);
            doc.add(companyName);
            doc.add(companyDetails);
            doc.add(info);
            doc.add(details);


            doc.add(jobSheetDetails);
            doc.add(heading);
//            doc.add(image1);
            PdfFont font = PdfFontFactory.createFont();



             // 3 columns.
            Cell cell1 = new Cell().add(new Paragraph("Job Summery").setFontColor(ColorConstants.WHITE).setFontSize(8));
            Cell cell2 = new Cell().add(new Paragraph("Job Description").setFontColor(ColorConstants.WHITE).setFontSize(8));
            Cell cell3 = new Cell().add(new Paragraph("Status").setFontColor(ColorConstants.WHITE).setFontSize(8));


            cell1.setBackgroundColor(ColorConstants.BLACK);
            cell2.setBackgroundColor(ColorConstants.BLACK);
            cell3.setBackgroundColor(ColorConstants.BLACK);



            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);

        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<Jobsheet.Job>jobs = Jobsheet.getJobs(jobSheetId);
        for(Jobsheet.Job job: jobs){
            Row row = new Row(job);
            Row.addRow(table,row);
        }


//        paragraph.add(table);

//        doc.add(table);
//        table.
//        table.setFixedPosition(pageSize.getLeft() + 30, pageSize.getTop()-400, pageSize.getWidth() - 60);
        table.setMarginTop(10);
        table.setMarginLeft(20);

        table.setWidth(288);

        Paragraph paragraph = new Paragraph("Note: Price of products/services will be informed once service is done").setItalic().setFontSize(10);
        doc.add(table);
        doc.add(paragraph);
        doc.close();

        //TODO open the generated pdf file

    }




}
