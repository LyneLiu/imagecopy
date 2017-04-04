package com.image;

import com.google.common.base.Joiner;
import com.opencsv.CSVReader;

import java.io.*;
import java.nio.charset.Charset;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lyne on 2017/4/2.
 */
public class CustomCSVReader {

    private String versionLine;

    private String titleLine;

    private String currNameLine;

    public String getCurrNameLine() {
        return currNameLine;
    }

    public void setCurrNameLine(String currNameLine) {
        this.currNameLine = currNameLine;
    }

    private String[] metaElement;

    public String[] getMetaElement() {
        return metaElement;
    }

    public void setMetaElement(String[] metaElement) {
        this.metaElement = metaElement;
    }

    public String getVersionLine() {
        return versionLine;
    }

    public void setVersionLine(String versionLine) {
        this.versionLine = versionLine;
    }

    public String getTitleLine() {
        return titleLine;
    }

    public void setTitleLine(String titleLine) {
        this.titleLine = titleLine;
    }

    public static String[] readCsvFile(String fileName) {

        String[] metaElement = new String[]{};
        try {

            CSVReader csvReader = new CSVReader(new InputStreamReader(new FileInputStream(fileName), "UTF-16LE"));

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-16LE"));

            String[] nextElement;
            int index = 0;
            while ((nextElement = csvReader.readNext()) != null) {
                if (index <= 2) {
                    index++;
                    continue;
                }

                if (index >= 4) {
                    break;
                }

                String elementStr1 = nextElement[0].replaceAll("\t\"\t", "\t\" \"\t");
                String[] tempElementList = elementStr1.split("\t");

                for (int i = 0; i < tempElementList.length; i++) {

                    String tempElement = tempElementList[i];

                    if ("\"".equalsIgnoreCase(tempElement) || "\"\"".equalsIgnoreCase(tempElement)) {
                        if ("\"".equalsIgnoreCase(tempElement)) {
                            tempElementList[i] += "\"";
                        }
                    } else if (tempElement.startsWith("\"") && !tempElement.endsWith("\"")) {
                        tempElementList[i] += "\"";
                    } else if (!tempElement.startsWith("\"") && tempElement.endsWith("\"")) {
                        tempElementList[i] = "\"" + tempElementList[i];
                    } else if ("\" \"".equalsIgnoreCase(tempElement)) {
                        tempElementList[i] = "\"\"";
                    }

                }

                metaElement = tempElementList;
                index++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return metaElement;
    }

    public static String bufferReadCsvFile(String fileName) {

        String metaElement = new String();
        try {

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-16LE"));

            String nextElement;
            int index = 0;
            while ((nextElement = bufferedReader.readLine()) != null) {
                if (index <= 2) {
                    index++;
                    continue;
                }

                if (index >= 4) {
                    break;
                }

                metaElement = nextElement;
                index++;
            }

            bufferedReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(metaElement);
        String firstSubStr = null;
        while (m.find()) {
            firstSubStr = m.group(1);
            if (firstSubStr != null) break;
        }
        int len = firstSubStr.length() + 2;

        String newMetaElement =  metaElement.substring(len,metaElement.length());

        return newMetaElement;
    }

    /**
     * 获取version和title
     *
     * @param fileName
     * @return
     */
    public static List<String> readVersionAndTitle(String fileName) {
        List<String> versionAndTitle = new ArrayList<>();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-16"));
            String currVersionLine = bufferedReader.readLine();
            String currTitleLine = bufferedReader.readLine();
            String currNameLine = bufferedReader.readLine();
            versionAndTitle.add(currVersionLine);
            versionAndTitle.add(currTitleLine);
            versionAndTitle.add(currNameLine);
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return versionAndTitle;
    }

    public static void main(String[] args) {
        String csvFile = "E:\\image\\image\\分裂后的数据.csv";
        String[] metaElement = CustomCSVReader.readCsvFile(csvFile);
        List<String> versionAndTitle = CustomCSVReader.readVersionAndTitle(csvFile);

        CustomCSVReader customCSVReader = new CustomCSVReader();
        customCSVReader.setVersionLine(versionAndTitle.get(0));
        customCSVReader.setVersionLine(versionAndTitle.get(1));
        customCSVReader.setMetaElement(metaElement);

        Joiner joiner = Joiner.on("\t").skipNulls();
        String convertMetaElement = joiner.join(metaElement);

        System.out.println(metaElement);
        System.out.println(versionAndTitle.size());
        System.out.println(convertMetaElement);
    }
}
