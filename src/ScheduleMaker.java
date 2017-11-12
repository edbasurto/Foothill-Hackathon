import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ScheduleMaker {
	
	//This is our project
   private static ArrayList<String> courseNames = new ArrayList<String>();
   private ArrayList<String[][]> courses = new ArrayList<String[][]>();
   
private static void getCoursesAva() {
	   try {
		    File file = new File("C:/coursecatalog.xlsx");
		    POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
		    XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(file));
		    for(int s = 0; s < wb.getNumberOfSheets(); s++) {
		    	XSSFSheet sheet = wb.getSheetAt(s);
			    XSSFRow row;
			    XSSFCell cell;
			    
			    DataFormatter df = new DataFormatter();
			    
			    courseNames.add(sheet.getSheetName());

			    int rows; // No of rows
			    rows = sheet.getPhysicalNumberOfRows();

			    int cols = 0; // No of columns
			    int tmp = 0;

			    // This trick ensures that we get the data properly even if it doesn't start from first few rows
			    for(int i = 0; i < 10 || i < rows; i++) {
			        row = sheet.getRow(i);
			        if(row != null) {
			            tmp = sheet.getRow(i).getPhysicalNumberOfCells();
			            if(tmp > cols) cols = tmp;
			        }
			    }
			    
			    String[][] temp = new String[rows][cols];
			    for(int r = 0; r < rows; r++) {
			        row = sheet.getRow(r);
			        if(row != null) {
			            for(int c = 0; c < cols; c++) {
			                cell = row.getCell((short)c);
			                if(cell != null) {
			                	temp[r][c] = df.formatCellValue(cell);
			                }
			            }
			        }
			    }
		    }
		} catch(Exception ioe) {
		    ioe.printStackTrace();
		}
   }
   
   public static void main(String[] args) {
	   
	   getCoursesAva();
	   
	  int numClasses = 0;
      Scanner scan = new Scanner(System.in);
      
      
      // enter course info
      System.out.println("How many courses would you like to take?");
      numClasses = scan.nextInt();
      String[] classNames = new String[numClasses];
      int[] classNums = new int[numClasses];
      char[] classLevels = new char[numClasses];
      boolean[] classHonors = new boolean[numClasses]; 
      for (int i = 0; i < numClasses; i++) {
    	 boolean error = true;
         while(error) {
        	 System.out.println("Enter the class name (4 character format): "); // Math 1B
             String temp = scan.nextLine();
             if(Course.checkName(temp)) {
            	 classNames[i] = temp;
            	 error = false;
             } else {
            	 System.out.println("Please enter a valid course name");
             }
         }
         error=true;
         while(error) {
        	 int temp = 0;
             System.out.println("Enter num level: ");
             try {
            	 temp = Integer.parseInt(scan.nextLine());
            	 if(Course.checkNum(temp)) {
            		 classNums[i] = temp;
            		 error = false;
            	 }
             } catch (Exception e) {
            	 System.out.println("Please enter a valid number between 1 and 999");
             }
         }
         error = true;
         while(error) {
        	 String line = "";
        	 System.out.println("Enter the class level ['.' if N/A]: ");
        	 line = scan.nextLine();
        	 if(line.length() == 1) {
        		 if(Course.checkLevel(line.charAt(0))) {
        			 classLevels[i] = line.charAt(0);
        			 error = false;
        		 }
        	 }
         }
         error = true;
         while(error) {
        	 String line = "";
        	 System.out.println("Is it an Honors class? [y/n]:  ");
        	 line = scan.nextLine();
        	 if(line.length() == 1) {
        		 if(Character.toUpperCase(line.charAt(0)) == 'Y') {
        			 classHonors[i] = true;
        			 error = false;
        		 } else if(Character.toUpperCase(line.charAt(0)) == 'N') {
        			 classHonors[i] = false;
        			 error = false;
        		 } else {
            		 System.out.println("Please enter either Y or N");
            	 }
        	 } else {
        		 System.out.println("Please enter either Y or N");
        	 }
         }
      }
   }
}

class Course{
   private String courseName;
   private char cSchl;
   private int cNum;
   private char cLvl;
   private boolean honors;
   private double numUnits;
   private boolean errorFlag;
   
   public Course(String courseName, char cSchl, int cNum, char cLvl, boolean honors, double numUnits) {
	   setCourseName(courseName);
   }
   
   public void setCourseName(String courseName) {
	   if(checkName(courseName)) {
		   this.courseName = courseName;
	   } else {
		   errorFlag = true;
	   }
   }
   
   public void setSchool(char cSchl) {
	   if(checkSchool(cSchl)) {
		   this.cSchl = cSchl;
	   } else {
		   errorFlag = true;
	   }
   }
   
   public void setNum(int cNum) {
	   if(checkNum(cNum)) {
		   this.cNum = cNum;
	   } else {
		   errorFlag = true;
	   }
   }
   
   public void setLvl(char cLvl) {
	   if(checkLevel(cLvl)) {
		   this.cLvl = cLvl;
	   } else {
		   errorFlag = true;
	   }
   }
   
   public void setUnits(int numUnits) {
      if(checkUnits(numUnits)) {
    	  this.numUnits = numUnits;
      } else {
    	  errorFlag = true;
      }
   }
   
   public String getCourseName() { return courseName; }
   public char getSchl() { return cSchl; }
   public int getNum() { return cNum; }
   public char getLvl() { return cLvl; }
   public double getNumUnits() { return numUnits; }
   
   public static boolean checkName(String courseName) {
	   int len = courseName.length();
	   if(len < 3 || len > 4) {
		   return false;
	   }
	   char[] chars = courseName.toCharArray();
	   for(char c: chars) {
		   if(c != ' ') {
			   if(!Character.isLetter(c)) {
				   return false;
			   }
		   }
	   }
	   return true;
   }
   
   public static boolean checkSchool(char cSchl) {
	   return cSchl == 'F' || cSchl == 'D';
   }
   
   public static boolean checkNum(int num) {
	   return num > 0 && num < 1000; // Course number needs to be 3 digits EX: 005, 057, 500
   }
   
   public static boolean checkLevel(char lvl) {
	   return lvl == '.' || Character.toUpperCase(lvl) == 'A' || Character.toUpperCase(lvl) == 'B' ||
			   Character.toUpperCase(lvl) == 'C' || Character.toUpperCase(lvl) == 'D';
   }
   
   public static boolean checkUnits(int numUnits) {
	   return numUnits > 1 || numUnits < 6;
   }
}

class Section{
   private double startTime;
   private double endTime;
   
   public boolean setStartTime(String time) {
	   startTime = formatTime(time);
	   return true;
   }
   
   private double formatTime(String time) {
      String[] convertTime = time.split(":", 2);
      double minutes = Double.parseDouble(convertTime[1]) / 60;
      double newTime = Double.parseDouble(convertTime[0]) + minutes;
     
      return newTime;
   }
}