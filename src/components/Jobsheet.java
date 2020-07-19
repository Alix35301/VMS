package components;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * @author ali
 * @created_on 7/17/20
 */
public class Jobsheet {
    public static ArrayList<Job>jobsheet = new ArrayList<>();

    public static class Job{
        public String getJob_summmery() {
            return job_summmery;
        }

        public void setJob_summmery(String job_summmery) {
            this.job_summmery = job_summmery;
        }

        public String getJob_desc() {
            return job_desc;
        }

        public void setJob_desc(String job_desc) {
            this.job_desc = job_desc;
        }

        String job_summmery, job_desc,mechanic_id;

        public String getMechanic_id() {
            return mechanic_id;
        }

        public void setMechanic_id(String mechanic_id) {
            this.mechanic_id = mechanic_id;
        }

        public Job(String job_summery, String job_desc, String mechanic_id){
            this.job_desc =job_desc;
            this.job_summmery =job_summery;
            this.mechanic_id =mechanic_id;
        }

        public Job(String job_desc){
            this.job_desc =job_desc;
        }

    }

    public static void toPDF(){
        //TODO implement print jobsheet function

        Document document = new Document();
        try
        {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("AddImageExample.pdf"));

            document.open();
            document.add(new Paragraph("Image Example"));

            //Add Image
            Image image1 = Image.getInstance("resources/company_logo.png");
            //Fixed Positioning
            image1.setAbsolutePosition(100f, 550f);
            //Scale to new height and new width of image
            image1.scaleAbsolute(200, 200);
            //Add to document
            document.add(image1);

//            String imageUrl = "/resources/company_logo.png";
//            Image image2 = Image.getInstance(new URL(imageUrl));

            document.close();
            writer.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
