/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 id : 431341992345-vd287uefo4f9f2qcnu0pqk1gebt1d6qp.apps.googleusercontent.com
 secret : GKYatd8jmH3Qq3QsvJbI9owz
 refresh token : 1/obHqkIKOSeCi_IoytRKWug5roT0fmz0RC9q4ZDGa01Q
 qa-admin@elasticaqa.net / Elastica#123
 */
package com.elastica.beatle.tests.securlets.gdrive;

import com.universal.common.GExcelDataProvider;
import com.universal.common.GoogleMailServices;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.testng.Reporter;
import org.testng.annotations.Test;

/**
 *
 * @author rahulkumar
 */
public class GMailStress {

    GoogleMailServices googleMailServices;
    List<String> receipentList = new ArrayList();
    int sizeOfreceipentList;
    Map<String, Integer> result = new HashMap();
    String excelSheetId = "1nusXFneZCgQo4EkUkjmfMiO8k00JSKenAeJN0fog1O4";
    int durationOfStressInSecs;
    long intervalBetweenActivitiesInMillies;
    List<String> sendReportTo = new ArrayList();
    String inputParams;

    public GMailStress(String clientId, String clientSecret, String refreshToken) {
        this.googleMailServices = new GoogleMailServices(clientId, clientSecret, refreshToken);
        googleMailServices.printLabelsInUserAccount();

        GExcelDataProvider excelDataProvider = new GExcelDataProvider(excelSheetId);
        List<Map<String, Object>> YammerStressInput = excelDataProvider.getDataAsMapList("GMailStressInput", "Param Name", "Param Value");
        for (Map<String, Object> YammerInput : YammerStressInput) {
            String stressParam = YammerInput.get("Param Name").toString();
            if (stressParam.contains("Duration of Stress Test in Secs")) {
                durationOfStressInSecs = Integer.parseInt(YammerInput.get("Param Value").toString());
            } else if (stressParam.contains("Interval between Activities in MilliSecs")) {
                intervalBetweenActivitiesInMillies = Integer.parseInt(YammerInput.get("Param Value").toString());
            } else if (stressParam.contains("Recipient List (Internal & Enternal Users)")) {
                String[] split = YammerInput.get("Param Value").toString().split(",");
                receipentList = new ArrayList<String>(Arrays.asList(split));
            } else if (stressParam.contains("Recipient List (Whom to send the results)")) {
                String[] split = YammerInput.get("Param Value").toString().split(",");
                sendReportTo = new ArrayList<String>(Arrays.asList(split));
            } else {
                System.out.println("!!! Stress Param Not Found !!!!");
            }
        }

        this.inputParams = "\nDuration Of Stress In Secs :" + durationOfStressInSecs + "\nInterval Between Activities In ms:" + intervalBetweenActivitiesInMillies
                + "\nRecipient List For Stress :" + receipentList + "\nSend Report To :" + sendReportTo;

        Reporter.log("Input Params :" + inputParams, true);
        this.sizeOfreceipentList = receipentList.size();
        for (String receipentList1 : receipentList) {
            result.put(receipentList1, 0);
        }
    }

    private static int getRandomNumberInRange(int max) {
        Random r = new Random();
        return r.nextInt(max);
    }

    public String getResult() {
        int count = 1;
        String mapString = "";
        mapString = mapString + "$$$$ ============ Gmail Stress Test Result========<" + new Date().toString() + ">=============== $$$$\n\n";
        for (Map.Entry<String, Integer> entrySet : result.entrySet()) {
            String key = entrySet.getKey();
            Integer value = entrySet.getValue();
            mapString = mapString + count++ + ")  " + key + " : " + value + "\n";
        }
        mapString = mapString + "\n===============================!!! Stress Test END !!!!===============================================";
        return mapString;
    }

    public void startGmailStressTest() {
        int totalTime = this.durationOfStressInSecs * 1000; // in ms
        long startTime = System.currentTimeMillis();
        boolean toFinish = false;
        while (!toFinish) {
            try {
                String emailBody = "Hello , \n\nGreeting of the day !!! Today is " + new Date().toString() + "\nThis mail is meant for GMail Stress Testing for Connector.\n\nThanks Sajjad ...\nQA Automation Lead..";
                List<String> to = new ArrayList();
                String emailId = receipentList.get(GMailStress.getRandomNumberInRange(sizeOfreceipentList));
                to.add(emailId);
                String subject = "GMail Stress Test :" + new Date().toString();
                googleMailServices.sendMailWithBody(to, null, null, subject, emailBody);
                toFinish = (System.currentTimeMillis() - startTime >= totalTime);
                result.put(emailId, result.get(emailId) + 1);
                Thread.sleep(this.intervalBetweenActivitiesInMillies);
            } catch (Exception ex) {
                Reporter.log("==== Issue Found in Stress Script :" + ex.getLocalizedMessage(), true);
            }
        }

        sendReport();
    }

    public void sendReport() {

        String emailBody = "Hello , \n\nGreeting of the day !!! Today is " + new Date().toString() + "\n"
                + "\n----------------------------------------------------------------------------------------------------------\n"
                + this.inputParams
                + "\n----------------------------------------------------------------------------------------------------------\n"
                + getResult()
                + "\n\nThanks Sajjad ...\nQA Automation Lead..";

        String subject = "GMail Stress Test Result :" + new Date().toString();
        googleMailServices.sendMailWithBody(this.sendReportTo, null, null, subject, emailBody);

    }

    @Test
    public static void performGmailStress() {
        GMailStress gMailStress = new GMailStress(
                "431341992345-vd287uefo4f9f2qcnu0pqk1gebt1d6qp.apps.googleusercontent.com",
                "GKYatd8jmH3Qq3QsvJbI9owz",
                "1/obHqkIKOSeCi_IoytRKWug5roT0fmz0RC9q4ZDGa01Q");

        gMailStress.startGmailStressTest();
    }

}
