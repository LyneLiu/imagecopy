package com.image;


import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lyne on 2017/4/1.
 */
public class CustomCSVContainer extends Component {

    public static final Logger logger = LoggerFactory.getLogger(CustomCSVContainer.class);

    private static final String NEWLINE = System.getProperty("line.separator");

    /*容器*/
    private JPanel firstPanel;
    private JPanel secondPanel;
    private JFrame mainFrame;
    /*title标签*/
    private JLabel titleLabel;
    /*csv标签*/
    private JLabel csvLabel;

    private JButton titleButton;
    private JButton csvButton;
    private JButton multiButton;

    private String targetDir;

    private String sourceCSVFile;

    private List<String> titles;

    private String csvVersion;

    private String csvTitle;

    private String csvName;

    public String getCsvName() {
        return csvName;
    }

    public void setCsvName(String csvName) {
        this.csvName = csvName;
    }

    private String csvMetaElement;

    public String getCsvMetaElement() {
        return csvMetaElement;
    }

    public void setCsvMetaElement(String csvMetaElement) {
        this.csvMetaElement = csvMetaElement;
    }

    private String[] csvMetaElementArray;

    private static final String csvTackledFile = "分裂后的数据.csv";

    public String[] getCsvMetaElementArray() {
        return csvMetaElementArray;
    }

    public void setCsvMetaElementArray(String[] csvMetaElementArray) {
        this.csvMetaElementArray = csvMetaElementArray;
    }

    public String getCsvVersion() {
        return csvVersion;
    }

    public void setCsvVersion(String csvVersion) {
        this.csvVersion = csvVersion;
    }

    public String getCsvTitle() {
        return csvTitle;
    }

    public void setCsvTitle(String csvTitle) {
        this.csvTitle = csvTitle;
    }

    public String getTargetDir() {
        return targetDir;
    }

    public void setTargetDir(String targetDir) {
        this.targetDir = targetDir;
    }

    public String getSourceCSVFile() {
        return sourceCSVFile;
    }

    public void setSourceCSVFile(String sourceCSVFile) {
        this.sourceCSVFile = sourceCSVFile;
    }

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public CustomCSVContainer() {
        prepareGUI();
    }

    public static void main(String[] args) {
        CustomCSVContainer container = new CustomCSVContainer();

    }

    private void prepareGUI() {
        mainFrame = new JFrame("图片分裂器");
        mainFrame.setSize(400, 400);
        mainFrame.setLayout(new GridLayout(4, 2));
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        titleLabel = new JLabel("标题文件（xls）:", JLabel.CENTER);
        csvLabel = new JLabel("csv文件:", JLabel.CENTER);

        titleButton = new JButton(" 选择标题文件 ");
        titleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                setTitles(titleButtonAction());
            }
        });
        csvButton = new JButton(" 选择csv文件 ");
        csvButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                csvButtonAction();
            }
        });
        multiButton = new JButton(" 多个分裂 ");
        multiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                multiButtonAction();
            }
        });

        firstPanel = new JPanel();
        firstPanel.setLayout(new FlowLayout());
        firstPanel.add(titleLabel);
        firstPanel.add(titleButton);
        firstPanel.add(csvLabel);
        firstPanel.add(csvButton);
        firstPanel.setVisible(true);

        secondPanel = new JPanel();
        secondPanel.setLayout(new FlowLayout());
        secondPanel.add(multiButton);
        secondPanel.setVisible(true);

        mainFrame.add(firstPanel);
        mainFrame.add(secondPanel);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    /**
     * 多个复制
     */
    private void multiButtonAction() {

        try {

            String targetFileName = getTargetDir() + "\\" + csvTackledFile;
            FileOutputStream fileOutputStream = new FileOutputStream(targetFileName);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream, "UTF-8"));

            String version = getCsvVersion();
            // add bom to csv file
            bufferedWriter.write(version);
            bufferedWriter.newLine();
            bufferedWriter.write(getCsvTitle());
            bufferedWriter.newLine();
            bufferedWriter.write(getCsvName());
            bufferedWriter.newLine();

            String csvMetaElement = getCsvMetaElement();

            List<String> titles = getTitles();
            if (titles != null) {

                for (int i = 0; i < titles.size(); i++) {
                    bufferedWriter.write("\"" + titles.get(i) + "\"" + csvMetaElement);
                    bufferedWriter.newLine();
                }
            }
            bufferedWriter.flush();
            bufferedWriter.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /**
     * 获取excel文件标题
     */
    private List<String> titleButtonAction() {
        List<String> choosedTitles = new ArrayList<String>();
        JFileChooser chooser = new JFileChooser();
        // optionally set chooser options ...
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File f = chooser.getSelectedFile();

                // read  and/or display the file somehow. ....
                FileInputStream file = new FileInputStream(f);

                if (f.getName().endsWith(".xls")){

                    //Get the workbook instance for XLS file
                    HSSFWorkbook workbook = new HSSFWorkbook(file);

                    //Get first sheet from the workbook
                    HSSFSheet sheet = workbook.getSheetAt(0);

                    //Iterate through each rows from first sheet
                    Iterator<Row> rowIterator = sheet.iterator();
                    while (rowIterator.hasNext()) {
                        Row row = rowIterator.next();

                        //For each row, iterate through each columns
                        Iterator<Cell> cellIterator = row.cellIterator();
                        while (cellIterator.hasNext()) {

                            Cell cell = cellIterator.next();

                            switch (cell.getCellType()) {
                                case Cell.CELL_TYPE_BOOLEAN:
                                    //System.out.println(cell.getBooleanCellValue() + "\t\t");
                                    break;
                                case Cell.CELL_TYPE_NUMERIC:
                                    //System.out.println(cell.getNumericCellValue() + "\t\t");
                                    break;
                                case Cell.CELL_TYPE_STRING:
                                    // System.out.println(cell.getStringCellValue() + "\t\t");
                                    choosedTitles.add(cell.getStringCellValue());
                                    break;
                            }

                            break;
                        }

                    }

                }else if (f.getName().endsWith(".xlsx")){
                    XSSFWorkbook workbook = new XSSFWorkbook(file);
                    XSSFSheet sheet = workbook.getSheetAt(0);
                    Iterator<Row> rowIterator = sheet.iterator();
                    while (rowIterator.hasNext()) {
                        Row row = rowIterator.next();

                        //For each row, iterate through each columns
                        Iterator<Cell> cellIterator = row.cellIterator();
                        while (cellIterator.hasNext()) {

                            Cell cell = cellIterator.next();

                            switch (cell.getCellType()) {
                                case Cell.CELL_TYPE_BOOLEAN:
                                    //System.out.println(cell.getBooleanCellValue() + "\t\t");
                                    break;
                                case Cell.CELL_TYPE_NUMERIC:
                                    //System.out.println(cell.getNumericCellValue() + "\t\t");
                                    break;
                                case Cell.CELL_TYPE_STRING:
                                    // System.out.println(cell.getStringCellValue() + "\t\t");
                                    choosedTitles.add(cell.getStringCellValue());
                                    break;
                            }
                            break;
                        }

                    }
                }

                file.close();
                return choosedTitles;
            } catch (Exception e) {
                // do nothing
            } finally {

            }
        }
        return choosedTitles;
    }

    /**
     * 读取csv文件
     */
    private void csvButtonAction() {

        JFileChooser chooser = new JFileChooser();
        FileInputStream fileInputStream = null;
        // optionally set chooser options ...
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File oldfile = chooser.getSelectedFile();

                // read  and/or display the file somehow. ....
                fileInputStream = new FileInputStream(oldfile);


                setSourceCSVFile(oldfile.getName());
                setTargetDir(oldfile.getParent());

                //System.out.println("dir:" + getTargetDir());
                //System.out.println("file name:" + getSourceImage());
            } catch (Exception e) {
                // do nothing
            } finally {
                try {
                    if (fileInputStream != null)
                        fileInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        String sourceCSVFileName = getTargetDir() + "\\" + getSourceCSVFile();
        String metaElement = CustomCSVReader.bufferReadCsvFile(sourceCSVFileName);

        List<String> versionAndTitle = CustomCSVReader.readVersionAndTitle(sourceCSVFileName);

        setCsvVersion(versionAndTitle.get(0));
        setCsvTitle(versionAndTitle.get(1));
        setCsvName(versionAndTitle.get(2));
        setCsvMetaElement(metaElement);

    }

}
