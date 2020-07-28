package components;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DashedBorder;
import com.itextpdf.layout.borders.DottedBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.layout.property.FontKerning;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import javax.swing.*;
import java.util.ArrayList;

/**
 * @author ali
 * @created_on 7/24/20
 */
public class InvoiceGenerator{
    public static class Row{
        Cell cell1;
        Cell cell2;
        Cell cell3;
        public Row(Product product){
            cell1= new Cell().add(new Paragraph(product.getId()));
            cell2= new Cell().add(new Paragraph(product.getProductCode()));
            cell3= new Cell().add(new Paragraph(product.getPrice()));
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

    public static void createInvoice(ArrayList<Product> products, Customer customer, Vehicle vehicle, Invoice invoice){
        Document doc=null;
        Table table = new Table(3);
        PageSize pageSize = null;
//        table.setHorizontalAlignment(HorizontalAlignment.CENTER);

//        table.setFixedLayout();



        try {
            PdfDocument pdfDoc = new PdfDocument(new PdfWriter("invoice.pdf"));
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




            Paragraph heading = new Paragraph("INVOICE").setUnderline();
            heading.setFontSize(12).setFixedPosition(180,550,200);
            String var = String.format("Invoice #: %s\n Customer ID: %s\nDate: %s",invoice.getId(),customer.getId(),invoice.getCreatedDate() );
            Paragraph invoiceDetails = new Paragraph(var).setFontSize(8).setFixedPosition(200,505,200);

            Paragraph info = new Paragraph("Bill To: ").setItalic().setFontSize(8)
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


            doc.add(invoiceDetails);
            doc.add(heading);
//            doc.add(image1);
            PdfFont font = PdfFontFactory.createFont();



             // 3 columns.
            Cell cell1 = new Cell().add(new Paragraph("Product Id").setFontColor(ColorConstants.WHITE).setFontSize(8));
            Cell cell2 = new Cell().add(new Paragraph("Product Name").setFontColor(ColorConstants.WHITE).setFontSize(8));
            Cell cell3 = new Cell().add(new Paragraph("Price").setFontColor(ColorConstants.WHITE).setFontSize(8));


            cell1.setBackgroundColor(ColorConstants.BLACK);
            cell2.setBackgroundColor(ColorConstants.BLACK);
            cell3.setBackgroundColor(ColorConstants.BLACK);



            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);

        } catch (Exception e) {
            e.printStackTrace();
        }

        for(Product product:products){
            Row row = new Row(product);
            Row.addRow(table,row);
        }
        Row footer = new Row();
        float total = calculateTotal(products);
        float tax = calTax(total,6);
        float netTotal = total+tax;
        footer.getCell3().add(new Paragraph(String.format("" +
            "Total:  MVR %.2f\n" +
            "Tax:  MVR %.2f\n" +
            "Total Due:  MVR %.2f\n\n" +
            "Payment Method: %s\n" +
            "Amount given: MVR %.2f\n" +
            "Balance: MVR %.2f\n" +
            "Cashier: %s",
            total,
            tax,
            netTotal,
            invoice.getPaymentMethod(),
            Float.parseFloat(invoice.getAmountGiven()),
            Float.parseFloat(invoice.getAmountGiven())-netTotal,
            invoice.createdUser
        )));
        table.addCell(footer.getCell1());
        table.addCell(footer.getCell2());
        table.addCell(footer.getCell3());
        System.out.println("Total "+total);
        System.out.println("Tax "+tax);
        System.out.println("Net Due "+netTotal);

//        paragraph.add(table);

//        doc.add(table);
//        table.
//        table.setFixedPosition(pageSize.getLeft() + 30, pageSize.getTop()-400, pageSize.getWidth() - 60);
        table.setMarginTop(10);
        table.setMarginLeft(20);

        table.setWidth(288);
        doc.add(table);
        doc.close();

    }

    public static float calculateTotal(ArrayList<Product> products){
        int total = 0;
        for(Product product: products){
            total+=Integer.parseInt(product.getPrice());
        }
        return total;
    }

    public static float calTax(float total, float tax){

        return (tax/100)*total;

    }


}
