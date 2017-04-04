package com.image;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by lyne on 2017/4/1.
 */
public class CustomContainer extends Component {

    public static final Logger logger = LoggerFactory.getLogger(CustomContainer.class);

    /*容器*/
    private JPanel firstPanel;
    private JPanel secondPanel;
    private JFrame mainFrame;
    /*title标签*/
    private JLabel titleLabel;
    /*csv标签*/
    private JLabel csvLabel;

    private Button titleButton;
    private Button csvButton;
    private Button singleButton;
    private Button multiButton;

    private String targetDir;

    private String sourceImage;

    private java.util.List<String> titles;

    public String getTargetDir() {
        return targetDir;
    }

    public void setTargetDir(String targetDir) {
        this.targetDir = targetDir;
    }

    public String getSourceImage() {
        return sourceImage;
    }

    public void setSourceImage(String sourceImage) {
        this.sourceImage = sourceImage;
    }

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public CustomContainer() {
        prepareGUI();
    }

    public static void main(String[] args) {
        CustomContainer container = new CustomContainer();
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

        titleButton = new Button(" 选择标题文件 ");
        titleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                setTitles(titleButtonAction());
            }
        });
        csvButton = new Button(" 选择图片文件 ");
        csvButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                csvButtonAction();
            }
        });
        singleButton = new Button(" 单个分裂 ");
        singleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                singleButtonAction();
            }
        });
        multiButton = new Button(" 多个分裂 ");
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
        secondPanel.add(singleButton);
        secondPanel.add(multiButton);
        secondPanel.setVisible(true);

        mainFrame.add(firstPanel);
        mainFrame.add(secondPanel);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    /**
     * 单个复制
     */
    private void singleButtonAction() {

        try {
            String fileName = getTargetDir() + "\\" + getSourceImage();
            File oldfile = new File(fileName);

            // read  and/or display the file somehow. ....
            FileInputStream fileInputStream = new FileInputStream(oldfile);

            List<String> titles = getTitles();
            Random rand = new Random();
            int randomNum = rand.nextInt(titles.size());
            String targetName = titles.get(randomNum) + ".jpg";
            File newFile = new File(getTargetDir(), targetName);

            if (!newFile.exists()) {
                newFile.createNewFile();
            }

            //获取输出到目标文件的输出流
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);

            IOUtils.copy(fileInputStream, fileOutputStream);

            fileInputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 多个复制
     */
    private void multiButtonAction() {

        try {

            List<String> titles = getTitles();
            if (titles != null) {

                for (int i = 0;i<titles.size();i++){
                //for (int i = 0; i < 10; i++) {

                    String targetName = titles.get(i) + ".jpg";
                    copyImageFile(targetName);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void copyImageFile(String targetName) {

        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            String fileName = getTargetDir() + "\\" + getSourceImage();
            File oldfile = new File(fileName);

            // read  and/or display the file somehow. ....
            fileInputStream = new FileInputStream(oldfile);
            File newFile = new File(getTargetDir(), targetName);

            if (!newFile.exists()) {
                newFile.createNewFile();
            }

            //获取输出到目标文件的输出流
            fileOutputStream = new FileOutputStream(newFile);

            IOUtils.copy(fileInputStream, fileOutputStream);

        } catch (Exception e) {

        } finally {
            try {
                fileOutputStream.close();
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

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
                    }
                }
                return choosedTitles;
            } catch (Exception e) {
                // do nothing
            }
        }
        return choosedTitles;
    }

    /**
     * 读取csv文件
     */
    private void csvButtonAction() {

        JFileChooser chooser = new JFileChooser();
        // optionally set chooser options ...
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File oldfile = chooser.getSelectedFile();

                // read  and/or display the file somehow. ....
                FileInputStream fileInputStream = new FileInputStream(oldfile);


                setSourceImage(oldfile.getName());
                setTargetDir(oldfile.getParent());

                //System.out.println("dir:" + getTargetDir());
                //System.out.println("file name:" + getSourceImage());
            } catch (Exception e) {
                // do nothing
            }
        }

    }

}
