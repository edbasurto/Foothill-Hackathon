import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class ScheduleMaker {
   
   public static Scanner scanner;
   private ArrayList<String> courseNames = new ArrayList<String>();
   private ArrayList<HSSFCell[][]> courses = new ArrayList<HSSFCell[][]>();
   
   private void getCoursesAva() {
	   try {
		    File file = new File("C:/coursecatalog.xlsx");
		    POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
		    HSSFWorkbook wb = new HSSFWorkbook(fs);
		    for(int s = 0; s < wb.getNumberOfSheets(); s++) {
		    	HSSFSheet sheet = wb.getSheetAt(s);
			    HSSFRow row;
			    HSSFCell cell;
			    
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
			    
			    HSSFCell[][] temp = new HSSFCell[rows][cols];
			    for(int r = 0; r < rows; r++) {
			        row = sheet.getRow(r);
			        if(row != null) {
			            for(int c = 0; c < cols; c++) {
			                cell = row.getCell((short)c);
			                if(cell != null) {
			                	temp[r][c] = cell;
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
      scanner = new Scanner(System.in);
      String userInputStr;
      int userInputInt;
      
      Course[] courses;
      
      
      // enter course info
      System.out.println("How many courses would you like to take?");
      userInputStr = scanner.nextLine();
      userInputInt = Integer.parseInt(userInputStr);
      courses = new Course[userInputInt];
      
      for (int i = 0; i < userInputInt; i++) {
         System.out.println("Enter the class: ");
         //courses[i].setName(scanner.nextLine());
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
   
   private static boolean checkName(String courseName) {
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
   
   private static boolean checkSchool(char cSchl) {
	   return cSchl == 'F' || cSchl == 'D';
   }
   
   private static boolean checkNum(int num) {
	   return num > 0 && num < 1000; // Course number needs to be 3 digits EX: 005, 057, 500
   }
   
   private static boolean checkLevel(char lvl) {
	   return lvl == '.' || Character.toUpperCase(lvl) == 'A' || Character.toUpperCase(lvl) == 'B' ||
			   Character.toUpperCase(lvl) == 'C' || Character.toUpperCase(lvl) == 'D';
   }
   
   private static boolean checkUnits(int numUnits) {
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