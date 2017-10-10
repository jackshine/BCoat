package com.elastica.beatle.tests.securlets;

import com.universal.common.DropboxBusinessAccActivities;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

class Task1 implements Runnable {

    private String name;
    DropboxStressTest javaMultiThread = DropboxStressTest.getInstance();
    DropboxBusinessAccActivities dropboxBusinessAccActivities;
    Map<String, String> run;

    public Task1(String name) {
        this.name = name;
        this.dropboxBusinessAccActivities = javaMultiThread.getDropboxBusinessAccActivities();
        String refList = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "securlets" + File.separator + "securletsData" + File.separator + "ReferenceList";
        String memList = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "securlets" + File.separator + "securletsData" + File.separator + "memberList_Updated.csv";
        this.run = javaMultiThread.run(memList);
    }

    public String getName() {
        return name;
    }

    @Override
    public void run() {
        System.out.println("Doing a task during : " + name);
        try {
            while (DropboxStressTest.startUser <= DropboxStressTest.endUser) {
                int counterStatus = DropboxStressTest.startUser++;

                System.out.println("Worker Name :" + name + " ## Counter Status :" + counterStatus);
                String userEmailId = "mitthan.meena+" + counterStatus + "@elasticaqa.net";
                String memberId = run.get(userEmailId);
                System.out.println(userEmailId + "####" + memberId); 
                getRevision(memberId, userEmailId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getRevision(String memberId, String userEmailId) throws IOException {   
        String revision = this.dropboxBusinessAccActivities.getRevision(memberId, "/DropboxUploadedFiles/Dropbox-Risk-Files-0/C-Image.jpg");
        if (revision.contains("not found")) {
            String content="Folder Missing : User Mail ID :" + userEmailId + "## Member ID :" + memberId;
            DropboxStressTest.al.add(content);
            System.out.println(content);
    
        }  
        System.out.println("User Mail Id :"+userEmailId +" ## Revision :"+revision);
       // return revision;
    }

    public void performStressOperation(String memberId, String userEmailId) throws IOException, InterruptedException {
        ////======= Perform Operations...//
        String performPublicSharing = null;
        for (int i = 0; i <= 20; i++) {
            String destinationFolder = "/DropboxUploadedFiles/Dropbox-Risk-Files-" + i;
            performPublicSharing = dropboxBusinessAccActivities.performPublicSharing(memberId, destinationFolder);
            System.out.println("User Name :" + userEmailId + " ## Folder Shared :" + destinationFolder + " ## Public Share Response :" + performPublicSharing);
        }
        if (performPublicSharing.contains("not found")) {
            System.out.println("Folders Missing... Copying Again....");
            String fileUploadCopyRef = javaMultiThread.fileUploadCopyRef(userEmailId, memberId, "OyTe0mFvNWExOWhrdXNzNA", "DropboxUploadedFiles");
            System.out.println("Email ID :" + userEmailId + " Copy Ref. Response :" + fileUploadCopyRef + " ## Copy Refrence :" + "OyTe0mFvNWExOWhrdXNzNA");
            System.out.println("### Folder Copy Response :" + fileUploadCopyRef);
            performStressOperation(memberId, userEmailId);
            Thread.sleep(90000);
        }
    }

}

public class DropboxStressTest {

    public static Map<String, String> myHashMap = new HashMap<>();
    public static List<String> al=new ArrayList<>();
    public static int startUser = 1;
    public static int endUser = 19995;
    public static int copyRefIndex = 0;
    String accessToken = "ostBxPcGQHoAAAAAAAACTSFYFi65koNks6kTSLwAPwmruVscYExTKG3AsbT5S1kU";
    String dropboxTeamMemberManagementToken = "A9QbK9tPvOoAAAAAAAGaQciY1H5ThXxnMURAux0KsRhmCipUjyLVSBPOywuKosX7";
    String dropboxTeamMemberFileAccessToken = "A9QbK9tPvOoAAAAAAAGaQyHwijmuadsp-WW3JhW_DWLejOmk-sMR87V0Uith0Df3";
    com.universal.common.DropboxBusinessAccActivities dropboxBusinessAccActivities;

    public DropboxBusinessAccActivities getDropboxBusinessAccActivities() {
        return dropboxBusinessAccActivities;
    }
    private static DropboxStressTest instance = null;

    public Map<String, String> run(String fileLocation) {
        String csvFile = fileLocation;
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        Map<String, String> maps = new HashMap<String, String>();
        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] strArray = line.split(cvsSplitBy);
                maps.put(strArray[0], strArray[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Done");
        return maps;

    }

    public List<String> readFile(String fileLocation) {
        String csvFile = fileLocation;
        BufferedReader br = null;
        String line = "";
        List<String> al = new ArrayList();
        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                al.add(line.trim());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("File Scanning Done .....");
        return al;
    }

    private DropboxStressTest() {
        this.dropboxBusinessAccActivities = new DropboxBusinessAccActivities(dropboxTeamMemberManagementToken, dropboxTeamMemberFileAccessToken);
    }

    public String fileUploadCopyRef(String mailId, String memberID, String copyReference, String cloudLocation) throws IOException, InterruptedException {
        String error = "502 Bad Gateway";
        String error1 = "Failed to grab";
        System.out.println("=== UPLOAD IN PROGRESS =====> Member ID :" + memberID + "mailID :" + mailId + "Copy Reference :" + copyReference);
        String folderCopyFromReference = dropboxBusinessAccActivities.folderCopyFromReference(memberID, copyReference, cloudLocation);
        System.out.println("========>" + folderCopyFromReference + " Member ID :" + memberID + "mailID :" + mailId + "Copy Reference :" + copyReference);
        Thread.sleep(5000);

        if (folderCopyFromReference.contains(error1)) {
            System.out.println("Retying Again..for..:" + memberID);
            String fileUploadCopyRef = fileUploadCopyRef(mailId, memberID, copyReference, cloudLocation);
            System.out.println("Retry Section :" + fileUploadCopyRef + " ## Mail ID :" + mailId);
            // Thread.sleep(300000);      
        }
        return folderCopyFromReference;
    }

    public String getCopyReference(String memberID, String cloudLocation) throws IOException {
        return dropboxBusinessAccActivities.getCopyReference(memberID, cloudLocation).split(":")[4].replaceAll("}", "").replaceAll("\"", "").trim();

    }

    public String getMemberId(String userEmailId) throws InterruptedException {
        String memberId = null;
        try {
            memberId = dropboxBusinessAccActivities.getMemberId(userEmailId);
            if (memberId == null) {
                System.out.println("####### Member id is null=========>" + userEmailId);
                getMemberId(userEmailId);
            }
            Thread.sleep(15000);
            System.out.println("Retrying for " + userEmailId);
        } catch (Exception e) {
            Thread.sleep(10000);
            System.out.println("Retrying for " + userEmailId);
            getMemberId(userEmailId);
        }

        return memberId;
    }

    public static DropboxStressTest getInstance() {
        if (instance == null) {
            instance = new DropboxStressTest();
        }
        return instance;
    }

    @Test(priority = 1)
    public void performBulkFileUpload() throws InterruptedException, Exception {
        System.out.println("Starting Bulk File Public Sharing in Dropbox....");
        int noOfWorkers = 10;
        long startTime = System.currentTimeMillis();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        for (int i = 1; i <= noOfWorkers; i++) {
            Task1 task = new Task1("Worker=>" + i);
            System.out.println("A new task has been added : " + task.getName());
            executor.execute(task);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("Finished all threads");
        long endTime = System.currentTimeMillis();
        System.out.println(" ## Total Time Taken :" + (endTime - startTime) + " ms");
    }

    public static void main(String[] args) throws InterruptedException {
        int noOfWorkers = 10;
        long startTime = System.currentTimeMillis();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        for (int i = 1; i <= noOfWorkers; i++) {
            Task1 task = new Task1("Worker=>" + i);
            System.out.println("A new task has been added : " + task.getName());
            executor.execute(task);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("Finished all threads");
        long endTime = System.currentTimeMillis();
        System.out.println(" ## Total Time Taken :" + (endTime - startTime) + " ms"); 
        for (String str : DropboxStressTest.al) {
            System.out.println("===>"+str);
        }
    }

//    public static void main(String[] args) throws IOException, InterruptedException {
//
//        JavaMultiThread1 javaMultiThread1 = new JavaMultiThread1();
//        Map<String, String> run = javaMultiThread1.run("/Users/rahulkumar/Desktop/Elastica/StressTesting/memberList_Updated.csv");
//        DropboxBusinessAccActivities dropboxBusinessAccActivities1 = javaMultiThread1.getDropboxBusinessAccActivities();
//        for (int i = 900; i <= 1000; i++) {
//            String userEmailId = "mitthan.meena+" + i + "@elasticaqa.net";
//            String memberId = run.get(userEmailId);
//            System.out.println(userEmailId + "####" + memberId);
//            javaMultiThread1.fileUploadCopyRef(userEmailId,memberId, "OyTe0mFvNWExOWhrdXNzNA", "DropboxUploadedFiles");
//            //String folderCopyFromPath = dropboxBusinessAccActivities1.folderCopyFromPath(memberId, "Myteam_01/DropboxUploadedFiles", "DropboxUploadedFiles");
//           // System.out.println("folderCopyFromPath===>" + folderCopyFromPath);
//        }
//
//    }
}
