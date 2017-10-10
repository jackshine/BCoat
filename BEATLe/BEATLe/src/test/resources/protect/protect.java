/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elastica.beatle.tests.securlets;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.Scanner;

public class Hello {

    public static void main(String[] args) throws IOException {
        Hello obj = new Hello();
        System.out.println(obj.getFile("configs/suiteConfigs/suiteConfigurations.xml"));
        Properties symbolmap;

    }

    private String getFile(String fileName) throws FileNotFoundException, IOException {

        StringBuilder result = new StringBuilder("");

        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        classLoader.getResource(fileName).getFile();
        File file = new File(classLoader.getResource(fileName).getFile());

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }

            scanner.close();
            InputStream inputStream = new ByteArrayInputStream(result.toString().getBytes(Charset.forName("UTF-8")));
            Properties symbolmap = new Properties();
            symbolmap.loadFromXML(inputStream);
            System.out.println("Value :" + symbolmap.get("eoe.loginURL"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();

    }
}
