
package com.universal.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.testng.Reporter;

/**
 *
 * @author rahulkumar
 */
public class GenericTestUtils {

    public static List<String> generateString(String emailId) {
        List<String> generatedStrings=new ArrayList();
        String[] split = emailId.split("@");
        String s1 = split[0] + "@" + split[1].toUpperCase();
        generatedStrings.add(s1);
        String s2 = split[0].toUpperCase() + "@" + split[1];
        generatedStrings.add(s2);
        String s3 = split[0].toUpperCase() + "@" + split[1].toUpperCase();
        generatedStrings.add(s3);
        int l = emailId.length();
        int s = 0;
        Random rand = new Random();
        s = rand.nextInt(l/2);
        String s4 = emailId.substring(0, s).toUpperCase() + emailId.substring(s);
        generatedStrings.add(s4);
        String s5 = emailId.substring(0, s) + emailId.substring(s).toUpperCase();
        generatedStrings.add(s5);
        String s6 = emailId.substring(0, s+1).toUpperCase() + emailId.substring(s+1);
        generatedStrings.add(s6);
        String s7 = emailId.substring(0, s+1) + emailId.substring(s+1).toUpperCase();
        generatedStrings.add(s7);
        String s8 = emailId.substring(0, s+2).toUpperCase() + emailId.substring(s+2);
        generatedStrings.add(s8);
        String s9 = emailId.substring(0, s+2) + emailId.substring(s+2).toUpperCase();
        generatedStrings.add(s9);
        String s10 = emailId.substring(0, s+3) + emailId.substring(s+3).toUpperCase();
        generatedStrings.add(s10); 
        int size = generatedStrings.size();
        System.out.println("Size :"+size);
        Reporter.log("====== RANDOM GENERATED EMAIL ID WITH DIFFERENT COMBINATION OF UPPER CASE ============",true);
        int i=1;
        for (String x : generatedStrings) {  
            Reporter.log(i++ +") "+x,true);
        }
        Reporter.log("======================================================================================",true);
        return generatedStrings;
    }
    

}
