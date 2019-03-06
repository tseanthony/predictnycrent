package predictrent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;


public class Main {

	public static void main(String[] args) throws IOException {
		
		// create object to get data.
		Resource resource = new Resource("medianAskingRent_All.csv");
		File dataFile = resource.getFile();
		
		// parser for CSV file
		CSVParser parser = CSVParser.parse(dataFile, Charset.forName("US-ASCII"), CSVFormat.RFC4180);
		
		// count # of rows. First row is column headers
		List<CSVRecord> record = parser.getRecords();
		
		// EXAMPLE CALLS
		//ArrayList<ArrayList<String>> allMatrix = makeAllMatrix(record);
		double[][] doubleMatrix = makeDoubleMatrix(record, 1, 3);
		double[][] noempty = fillZerosWithLast(doubleMatrix);
		//double[][] percentdelta = percentChangeMatrix(doubleMatrix); 
		double[][] percentfromfirstpoint = percentFromFirstPoint(doubleMatrix);
		//printMatrix(percentdelta);
		matrixtoCSV(percentfromfirstpoint, "/Users/anthonytse/Desktop/Untitled.csv");
	}
	
	private static ArrayList<ArrayList<String>> makeAllMatrix(List<CSVRecord> record){
		
		// get dimensions of CSV file
		int rowCount = record.size();
		int columnCount = record.get(0).size();
		
		ArrayList<ArrayList<String>> dataMatrix = new ArrayList<ArrayList<String>>();
		
		for(int i = 0; i < rowCount; i++) {
			ArrayList<String> row = new ArrayList<String>();
			
			for (int j = 0; j < columnCount; j++) {

				String word = record.get(i).get(j);
				row.add(record.get(i).get(j));
			}
			
			dataMatrix.add(row);
		}
		return dataMatrix;
	}
	
	private static double[][] makeDoubleMatrix(List<CSVRecord> record, 
			int rowStart, int columnStart){
		
		
		// get dimensions of CSV file
		int rowCount = record.size();
		int columnCount = record.get(0).size();
		
		double[][] dataMatrix = new double[rowCount - rowStart][columnCount - columnStart];
		
		for(int i = 1; i < rowCount; i++) {
			
			for (int j = 3; j < columnCount; j++) {
				
				String word = record.get(i).get(j);
				if (word.equals("")) {
					word = "0";
				}
				//System.out.format("%5s ", word);
				dataMatrix[i - rowStart][j - columnStart] = Double.parseDouble(word);
			}
		}
		
		return dataMatrix;
		
	}
	
	private static double[][] fillZerosWithLast(double[][] inputMatrix) {
		
		//int rowCount = inputMatrix.length;
		int columnCount = inputMatrix[0].length;
		
		for (double[] row : inputMatrix) {
			
			double lastval = row[0];
			for (int i = 0; i < columnCount; i++) {
				if (row[i] == 0) {
					row[i] = lastval;
				} else {
					lastval = row[i];
				}	
			}
		}
		return inputMatrix;
	}
	
	//calculate percent change from previous value
	private static double[][] percentChangeMatrix(double[][] inputMatrix){
		
		//int rowCount = inputMatrix.length;
		int columnCount = inputMatrix[0].length;
		
		for (double[] row : inputMatrix) {
			
			double temp1 = row[0];
			double temp2;
			
			for (int i = 0; i < columnCount; i++) {
				temp2 = row[i];
				
				if (temp1 == 0) {
					row[i] = 0;
				}else {
					row[i] = (row[i] - temp1) / temp1 * 100;
				}
				
				temp1 = temp2;
				
			}
		}
		return inputMatrix;
			
	}
	
	//calculate percent change from first Valid Point
	private static double[][] percentFromFirstPoint(double[][] inputMatrix){
		
		//int rowCount = inputMatrix.length;
		int columnCount = inputMatrix[0].length;
		
		for (double[] row : inputMatrix) {
			
			double temp1 = 0;
	
			
			for (int i = 0; i < columnCount; i++) {
				
				if (temp1 == 0) {
					temp1 = row[i];
				}
						
				row[i] = (row[i] - temp1) / temp1 * 100;
	
				
			}
		}
		return inputMatrix;
			
	}
	
	private static double[][] printMatrix(double[][] inputMatrix){
		for (double[] row : inputMatrix) {
			
			for (double column : row) {
				System.out.format("   %4.0f", column);
				
			}
			System.out.println();
		}
		return inputMatrix;
		
		
	}
	
	// writes matrix to csv
	private static void matrixtoCSV(double[][] inputMatrix, String inputLocation) {
		try
	    {
	      FileWriter writer = new FileWriter(inputLocation);          
	         for(int i = 0; i < inputMatrix.length; i++)
	         {
	            for (int j=0; j < inputMatrix[0].length - 1; j++)
	             {
	                 writer.append(String.valueOf(inputMatrix[i][j]));
	                 writer.append(',');
	             }
	               writer.append(String.valueOf(inputMatrix[i][inputMatrix[0].length - 1]));
	               writer.append('\n');
	               writer.flush();
	         }
	         writer.close();
	      }        
	    catch(Exception e)
	    {
	      e.printStackTrace();
	    }
		
	}
	
	

}
