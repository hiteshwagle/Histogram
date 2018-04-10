/*
 * Name: Histogram
 * Purpose: To study to histogram of a gryscale image and to modify unique intensity according to need.
 * 
 * Author: Hitesh Wagle
 * Uni Key: hwag3386
 * Created: 11/04/2018
*/

package Histogram;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;

public class histogram {

	public static void main(String[] args) {
		
		/*Image source : Kindly provide the complete path of an image with image name and extension. Use PNG format as it 
		Lossless image and retains all the property and provide better output. Pixels are advised to be less than 100px 
		for easy readability of histogram. */
		
		
		File OriginalImage = new File("D:\\University\\Multimedia\\homework1\\original.png");
				
		BufferedImage img = null;
	
		try {
			
			img = ImageIO.read(OriginalImage);
			
			//Grayscaling the image in case file is colored image
			BufferedImage GrayscaleImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
			
			int [][]grayscale = new int[img.getWidth()][img.getHeight()];
			int a=0;
			for (int i=0;i<img.getWidth();i++) {
				
				for (int j=0;j<img.getHeight();j++) {
					
					//retrieving RGB Color on each pixel
															
					Color c = new Color(img.getRGB(i,j));
					int r = c.getRed();
					int g = c.getGreen();
					int b = c.getBlue();
					a = c.getAlpha();
					
					// grayscaling conversion: Simple => (R+B+G)/3, grayscale luminosity =>  0.21R + 0.72G + 0.07B
					
					// Simple grayscaling
					
					int gr = (r+g+b)/3;
					
					grayscale[i][j]=gr;
					
					//Creating grayscale code
					Color color = new Color (gr,gr,gr,a);
					GrayscaleImage.setRGB(i,j,color.getRGB());
					
				}	
			}
			
		// Creating new grayscale File. Also provide the output file path in path string
			
		String path = "D:\\University\\Multimedia\\homework1\\";
		
		ImageIO.write(GrayscaleImage, "png", new File(path+"Grayscale.png"));
		
		
		/* converting the 2D array of grayscale image into frequency table, 1st column = unique number 
		and 2nd column = frequency of that unique number */ 
		
		PrintWriter writer = new PrintWriter(path+"Histogram_File.txt", "UTF-8");
				
		
		int q=1;
		int num, flag;
		int new_array [][] = new int[256][2];
		
		
		new_array [0][0] = grayscale[0][0];
		for (int c = 0; c< img.getWidth(); c++) {
			for (int d = 0; d< img.getHeight(); d++) {
				num = grayscale[c][d];
				flag=0;
				for (int k =0; k<256; k++) {
					if(new_array[k][0]==num) {
						new_array[k][1]++;
						flag=1;
						break;
					}
				}
				if(flag==0) {
					new_array[q][0] = num;
					new_array[q][1]++;
					q++;
					}
			}
		}
		
			
		// Sorting the new created array to depict the histogram
		
		int temp, count1;
		
		for (int i=0; i<q; i++) {
			for (int j= i; j<q;j++) {
				if(new_array[j][0]<new_array[i][0]) {
					temp = new_array[i][0];
					count1=new_array[i][1];
					new_array[i][0] = new_array[j][0];
					new_array[i][1]=new_array[j][1];
					new_array[j][0]=temp;
					new_array[j][1]=count1;
					
				}
				
			}
		}
		
		//Printing histogram for grayscaled image
		
		writer.println("Normal Histogram of Grayscaled Image");
		writer.println();
		writer.println("Total Number of Grayscale Level in normal Histogram is: "+q);
		writer.println();
		writer.println("(Level, Frequency)");
		//System.out.println("Normal Histogram of Grayscaled Image");
		//System.out.println();
		for(int r=0;r< q; r++) {
				//System.out.print("("+new_array[r][0]+","+new_array[r][1]+") ");
				writer.print("("+new_array[r][0]+","+new_array[r][1]+") ");
				for (int s=0; s< new_array[r][1]; s++) {
					//System.out.print("*");
					writer.print("*");
				}
				//System.out.println();
				writer.println();
		}
		
		writer.close();
		
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		//Creating and working to make equalised histogram
		
		
		double array_prob [][] = new double[q][2];
		
		// Finding probability of each intensity
		for (int i=0; i<q; i++) {
			array_prob[i][0]=new_array[i][0];
			array_prob[i][1]= (double)new_array[i][1]/(img.getWidth()*img.getHeight());
		}
		
		//Finding cumulative frequency
		for (int i=1; i<q; i++) {
			array_prob[i][1]= array_prob[i][1] + array_prob[i-1][1];
		}
		
		//Creating histogram 
		for (int i=0; i<q; i++) {
			array_prob[i][0]= (int)Math.round(array_prob[i][1] * 255);
			array_prob[i][1] = new_array[i][1];
		}
		
		// Creating Equalised histogram with respective frequency
		int hist_equi [][] = new int[q][2]; 
		int count2=0, hist_len=0;
		for (int i=0; i<q;i++) {
			//System.out.println(count2+" "+i);
			if(count2==q)
				break;
			hist_equi[i][0]= (int)array_prob[count2][0];
			hist_len = i+1;
			for (int j = i; j<q;j++) {
				if(hist_equi[i][0]==(int)array_prob[j][0]) {
				hist_equi[i][1]= hist_equi[i][1] + (int)array_prob[j][1];
				count2++;
				}
			}
		}
		
		
		// Creating Equalised Histogram Image
		BufferedImage Equi_image = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		for (int i =0; i<q; i++) {
			for(int j=0; j<img.getWidth();j++) {
				for (int k=0; k<img.getHeight(); k++) {
					if(new_array[i][0] == grayscale[j][k]) {
						grayscale[j][k]= (int) array_prob[i][0];
						
						Color equi_color = new Color ((int) array_prob[i][0],(int) array_prob[i][0],(int) array_prob[i][0],a);
						Equi_image.setRGB(j,k,equi_color.getRGB());
					}
				}
			}
		}
		
		ImageIO.write(Equi_image, "png", new File(path+"Eqiu_image.png"));
		
		PrintWriter new_writer = new PrintWriter(path+"Equalised_Histogram.txt", "UTF-8");
		
		//Printing equalised histogram for grayscaled image
		
		new_writer.println("Equalised Histogram of Grayscaled Image");
		new_writer.println();
		new_writer.println("Total Number of Grayscale Level in Equalised Histogram is: "+hist_len);
		new_writer.println();
		new_writer.println("(Level, Frequency)");
		//System.out.println("Equalised Histogram of Grayscaled Image");
		//System.out.println();
		for(int r=0;r< hist_len; r++) {
				new_writer.print("("+hist_equi[r][0]+","+hist_equi[r][1]+") ");
				//System.out.print("("+hist_equi[r][0]+","+hist_equi[r][1]+") ");
				for (int s=0; s< hist_equi[r][1]; s++) {
					new_writer.print("*");
					//System.out.print("*");
			}
				new_writer.println();
				//System.out.println();
		}
		
		new_writer.close();
				
		}
		catch (IOException e) {
			e.printStackTrace();
		}	
	
	}
}
