package info634;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mwangia on 11/30/18.
 */
public class ProjectMain {
    public static final String ORIGINATED = "Loan originated";
    public static final String APPROVED = "Application Approved";
    public static final String APPROVED_NOT_ACCEPTED = "Application approved but not accepted";
    public static final String DENIED = "Application Denied";
    public static final String DENIED_APP = "Application denied by financial institution";
    public static final String PRE_APPROVAL = "Preapproval request denied by financial institution";
    public static void main(String[] args) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("/Users/mwangia/Documents/DrexelGrad/INFO634/hmda_philly.csv"))) {
            String line;
            boolean delete;
            int count = 0;
            Pattern originatedPattern = Pattern.compile('^' +ORIGINATED, Pattern.CASE_INSENSITIVE);
            Pattern approvedNotAcceptedPattern = Pattern.compile('^' +APPROVED_NOT_ACCEPTED, Pattern.CASE_INSENSITIVE);
            Pattern deniedAppPattern = Pattern.compile('^' +DENIED_APP, Pattern.CASE_INSENSITIVE);
            Pattern preApprovalPattern = Pattern.compile('^' +PRE_APPROVAL, Pattern.CASE_INSENSITIVE);

            BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/mwangia/Documents/DrexelGrad/INFO634/hmda_philly_cleaned_classes.csv"));
            BufferedWriter deletedWriter = new BufferedWriter(new FileWriter("/Users/mwangia/Documents/DrexelGrad/INFO634/hmda_philly_deleted_classes.csv"));
            while((line = reader.readLine()) != null) {
                delete = false;
                count = count + 1;
                //System.out.println(line);
                Matcher originatedMatcher = originatedPattern.matcher(line);
                Matcher approvedMatcher = approvedNotAcceptedPattern.matcher(line);
                Matcher deniedMatcher = deniedAppPattern.matcher(line);
                Matcher preapprovedMatcher = preApprovalPattern.matcher(line);
                String editedLine = null;
                if (originatedMatcher.find()){
                    editedLine = line.replace(ORIGINATED, APPROVED);
                } else if (approvedMatcher.find()) {
                    editedLine = line.replace(APPROVED_NOT_ACCEPTED, APPROVED);
                } else if (deniedMatcher.find()) {
                    editedLine = line.replace(DENIED_APP, DENIED);
                } else if (preapprovedMatcher.find()) {
                    editedLine = line.replace(PRE_APPROVAL, DENIED);
                } else if (count != 1){
                    delete = true;
                } else if (count == 1) {
                    //it's the header row. Write as-is
                    writer.write(line);
                    writer.newLine();
                    deletedWriter.write(line);
                    deletedWriter.newLine();
                    writer.flush();
                    deletedWriter.flush();
                    continue;
                }
                if (!delete) {
                    writer.write(editedLine);
                    writer.newLine();
                    writer.flush();


                } else {
                    deletedWriter.write(line);
                    deletedWriter.newLine();
                    deletedWriter.flush();
                }
            }
            System.out.println("copied " + count + " rows.");
        }
    }
}
