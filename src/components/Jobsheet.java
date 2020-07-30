package components;


import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author ali
 * @created_on 7/17/20
 */
public class Jobsheet {
    public static ArrayList<Job>jobsheet = new ArrayList<>();

    String id, vehicle_reg, customer_id, mech_assigned, status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVehicle_reg() {
        return vehicle_reg;
    }

    public void setVehicle_reg(String vehicle_reg) {
        this.vehicle_reg = vehicle_reg;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getMech_assigned() {
        return mech_assigned;
    }

    public void setMech_assigned(String mech_assigned) {
        this.mech_assigned = mech_assigned;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static enum JobSheetStatsus{
        PENDING,
        UPDATED
    }

    public static class Job{

        String jobSummmery, jobDesc,jobStatus;

        public Job(){};

        public Job(String job_summery, String jobDesc, String jobStatus){
            this.jobDesc = jobDesc;
            this.jobSummmery =job_summery;
            this.jobStatus =jobStatus;
        }

        public String getJobSummmery() {return jobSummmery; }

        public void setJobSummmery(String jobSummmery) {
            this.jobSummmery = jobSummmery;
        }

        public String getJobDesc() {
            return jobDesc;
        }

        public void setJobDesc(String jobDesc) {
            this.jobDesc = jobDesc;
        }

        public Job(String jobDesc){this.jobDesc = jobDesc;}

        public String getJobStatus() { return jobStatus; }

        public void setJobStatus(String jobStatus) {this.jobStatus = jobStatus; }
    }

    public static ArrayList<Job> getJobs(String jobSheetId){
        Connection conn = Helpers.getConnection();
        ArrayList<Job> jobs = new ArrayList<>();
        String sql = "SELECT * FROM VMS.JOBS WHERE jobsheetId =?";
        try {
            PreparedStatement p = conn.prepareStatement(sql);
            p.setString(1, jobSheetId);
            ResultSet resultSet = p.executeQuery();
            Job job = new Job();
            while(resultSet.next()){
                job.setJobSummmery(resultSet.getString(2));
                job.setJobDesc(resultSet.getString(3));
                job.setJobStatus(resultSet.getString(4));
                jobs.add(job);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

return jobs;
    }
}
