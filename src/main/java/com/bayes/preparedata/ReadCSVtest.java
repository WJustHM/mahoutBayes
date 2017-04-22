package com.bayes.preparedata;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

/**
 * Created by linux on 17-3-19.
 */
public class ReadCSVtest {

    private static int data = 1;
    private static String gen = null;
    private static String[] str = {"one", "two", "three", "four", "five", "six", "seven", "eight"};
    private static int start = 0;
    private static int num = 0;
    private static int total = 0;

    public static void main(String[] args) throws Exception {
        //train
        for (; data <= 3; data++) {
            gen = System.getProperty("user.dir") + "/Testdataclean/test" + data;
            System.out.println(gen);
            run();
        }
    }

    public static void run() {
        if (data == 1) {
            start = 0;
            num = 0;
            total = 80;
        } else if (data == 2) {
            start = 80;
            num = 80;
            total = 170;
        } else {
            start = 170;
            num = 170;
            total = 250;
        }

        String file = System.getProperty("user.dir") + "/testdata/test0" + data + ".xlsx";
        ReadCSVtest read = new ReadCSVtest();
        read.deleteDir(new File(gen));
        read.createdir(gen);
        read.readxslx(file);
    }

    public void readxslx(String file) {
        try {
            InputStream in = new FileInputStream(new File(file));
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(in);
            // 获取每一个工作薄
            for (int numsheet = 0; numsheet < xssfWorkbook.getNumberOfSheets(); numsheet++) {
                XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numsheet);
                if (xssfSheet == null) {
                    continue;
                }
                // 获取当前工作薄的每一行
                for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                    XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                    if (xssfRow != null) {
                        //2
                        if (data == 2) {
                            XSSFCell one = xssfRow.getCell(0);
                            output(str[0], getValue(one));
                            XSSFCell two = xssfRow.getCell(1);
                            output(str[1], getValue(two));
                            XSSFCell three = xssfRow.getCell(2);
                            output(str[2], getValue(three));
                            XSSFCell four = xssfRow.getCell(3);
                            output(str[3], getValue(four));
                            XSSFCell five = xssfRow.getCell(4);
                            output(str[4], getValue(five));
                            XSSFCell six = xssfRow.getCell(5);
                            output(str[4], getValue(six));
                            XSSFCell seven = xssfRow.getCell(6);
                            output(str[5], getValue(seven));
                            XSSFCell eight = xssfRow.getCell(7);
                            output(str[6], getValue(eight));
                            XSSFCell eijiu = xssfRow.getCell(8);
                            output(str[7], getValue(eijiu));
                        }

                        //1
                        if (data == 1) {
                            XSSFCell one = xssfRow.getCell(0);
                            output(str[0], getValue(one));
                            XSSFCell two = xssfRow.getCell(1);
                            output(str[1], getValue(two));
                            XSSFCell three = xssfRow.getCell(2);
                            output(str[2], getValue(three));
                            XSSFCell four = xssfRow.getCell(3);
                            output(str[3], getValue(four));
                            XSSFCell five = xssfRow.getCell(4);
                            output(str[4], getValue(five));
                            XSSFCell six = xssfRow.getCell(5);
                            output(str[5], getValue(six));
                            XSSFCell seven = xssfRow.getCell(6);
                            output(str[6], getValue(seven));
                            XSSFCell eight = xssfRow.getCell(7);
                            output(str[7], getValue(eight));
                        }

                        //3
                        if (data == 3) {
                            XSSFCell one = xssfRow.getCell(0);
                            output(str[0], getValue(one));
                            XSSFCell two = xssfRow.getCell(1);
                            output(str[1], getValue(two));
                            XSSFCell three = xssfRow.getCell(2);
                            output(str[2], getValue(three));
                            XSSFCell four = xssfRow.getCell(3);
                            output(str[3], getValue(four));
                            XSSFCell five = xssfRow.getCell(4);
                            output(str[4], getValue(five));
                            XSSFCell six = xssfRow.getCell(5);
                            output(str[5], getValue(six));
                            XSSFCell seven = xssfRow.getCell(6);
                            output(str[6], getValue(seven));
                            XSSFCell eight = xssfRow.getCell(7);
                            output(str[7], getValue(eight));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //转换数据格式 http://blog.csdn.net/zhengyikuangge/article/details/51524691
    private String getValue(XSSFCell xssfRow) {
        if (xssfRow != null && !xssfRow.getStringCellValue().equals("0")) {
            return String.valueOf(xssfRow.getStringCellValue() + "\n");
        }
        return null;
    }

    public void output(String filename, String context) {
        if (num == total) num = start;
        FileOutputStream fis = null;
        String file = gen + "/" + filename + "/" + num++;
        try {
            File name = new File(file);
            fis = new FileOutputStream(name, true);
            if (!name.exists()) {
                name.createNewFile();
            }
            if (context != null && context != "") {
                fis.write(context.getBytes());
                fis.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void truncate(File files) {
        if (files.isFile() || files.list().length == 0) {
            files.delete();
        } else {
            File[] dir = files.listFiles();
            for (int i = 0; i < dir.length; i++) {
                truncate(dir[i]);
                dir[i].delete();
            }
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    public void createdir(String dir) {
        for (int i = 0; i < str.length; i++) {
            File file = new File(gen + "/" + str[i]);
            if (file.exists()) return;
            file.mkdirs();
        }
    }
}
