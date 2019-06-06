/*
 -----------------------------------------------------------------------------------
 Project 	 : Projet PRO
 File     	 : GenerateFile.java
 Author(s)   : R. Combremont, M. Dupraz, I. Ounon, P. Sekley, J. Ayoub 
 Date        : 10.05.2016
 Purpose     : Generate a file to be saved in JPEG or PDF. 
 remark(s)   : n/a
 Compiler    : jdk 1.8.0_60
 -----------------------------------------------------------------------------------
 */

package data_processing;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDPixelMap;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;




/**
 * Class that offers methods to generate a file from the application's main view.
 *
 * @author R. Combremont, M. Dupraz, I. Ounon, P. Sekley, J. Ayoub
 * @date 10.05.2016
 * @version 1.1
 */
public class GenerateFile {

	
	
	/**
	 * This methods generate a PDF from the application's main dashboard.
	 *
	 * @param tabPaneStat
	 * @param pdfFilePathname
	 * @throws IOException
	 */
	public void toPDF(TabPane tabPaneStat, String pdfFilePathname) throws IOException{
		
		/**
		 * Position of the header into the file to be generated
		 */
		float xPos = 100;
		float yPos = 610;
		final float xPosDefault = xPos;
		final float yPosDefault = yPos;
		final String heigLogoPath = "meteoImages/HEIG-VD_Logo.png";

		try {
			/**
			 * Initialization of Content stream that helps us to write into the file
			 */
			PDPageContentStream contentStream_1 = null;
			PDPageContentStream contentStream_2 = null;
			BufferedImage 		buffImage 		= null;
			PDXObjectImage	 	ximage 			= null;
			BufferedImage  		xHeigLogoImage  = null;
			PDXObjectImage 		ximageLogoHeig  = null;
			
			try (PDDocument document = new PDDocument()) {
				PDPage page_1 = new PDPage();
				PDPage page_2 = new PDPage();
				document.addPage(page_1);
				document.addPage(page_2);
				
				/**
				 * We get the first page and write the header
				 */
				page_1 = (PDPage)document.getDocumentCatalog().getAllPages().get(0);
				contentStream_1 = new PDPageContentStream(document, page_1, true, 
																			true);
				contentStream_2 = new PDPageContentStream(document, page_2, true, 
																			true);
				
				/** Read the image and then draw it in the file at a specific 
				 * position mentioned
				 */
				xHeigLogoImage = ImageIO.read(ResourceLoader.load(heigLogoPath));
				ximageLogoHeig = new PDPixelMap(document, xHeigLogoImage);
				contentStream_1.drawXObject(ximageLogoHeig, 80, 700, ximageLogoHeig
									.getWidth()-130, ximageLogoHeig.getHeight()-50);

				// Beginning of writing text and settings of the font
				contentStream_1.beginText();
				contentStream_1.setFont(PDType1Font.HELVETICA_BOLD_OBLIQUE, 26);
				
				contentStream_1.setNonStrokingColor(255,0,0);
				contentStream_1.moveTextPositionByAmount(420, 720);
				contentStream_1.drawString("PRO-2016");
				contentStream_1.endText();
				
				
				contentStream_1.beginText();
				contentStream_1.setFont(PDType1Font.HELVETICA, 29);
				contentStream_1.setNonStrokingColor(28,134,238);
				contentStream_1.moveTextPositionByAmount(65, 600);//80, 750
				contentStream_1.drawString("Graphes de variations météorologiques");
				

				contentStream_1.setFont(PDType1Font.HELVETICA, 16);
				contentStream_1.moveTextPositionByAmount(80, 200);//80, 400
				contentStream_1.endText();

				/**
				 * We get the second page of the document in which we are going to 
				 * draw images from statistics especially graphs
				 */
				page_2 = (PDPage)document.getDocumentCatalog().getAllPages().get(1);
				for(int i = 0; i < tabPaneStat.getTabs().size(); i++){
					buffImage = generate_png_from_container(tabPaneStat.getTabs()
															 		.get(i)
															 		.getContent());
					ximage = new PDJpeg(document, buffImage, 1.0f);

					contentStream_2.drawXObject(ximage, xPos, yPos, ximage.getWidth()-70, ximage.getHeight()-110);
					yPos -= 200;
				}
				/** We close the stream after writing into it */
				contentStream_1.close();
				contentStream_2.close();

				/** We sent to the default value for further treatments */
				xPos = xPosDefault;
				yPos = yPosDefault;

				/** Save the file under the name specified by the user */
				document.save(pdfFilePathname);
			}

		} catch (COSVisitorException ex) {
			Logger.getLogger(GenerateFile.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	

	/**
	 * This method generates a PNG file inspired by a hint on stackoverflow.
	 * See sources links mentioned in our reports.
	 *
	 * @param node
	 * @return the image
	 */
	public static BufferedImage generate_png_from_container(Node node) {
		SnapshotParameters param = new SnapshotParameters();
		param.setDepthBuffer(true);
		WritableImage snapshot = node.snapshot(param, null);
		BufferedImage tempImg  = SwingFXUtils.fromFXImage(snapshot, null);
		BufferedImage img      = null;
		byte[] imageInByte;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(tempImg, "png", baos);
			baos.flush();
			imageInByte = baos.toByteArray();
			baos.close();
			InputStream in = new ByteArrayInputStream(imageInByte);
			img = ImageIO.read(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		/** the final image sent to the PDJpeg */
		return img;
	}
	

	/**
	 * This methods generate a JPEG from the application's main dashboard.
	 *
	 * @param node
	 * @param jpegfilename
	 */
	public void toJpeg(Node node, String jpegfilename){
		
		WritableImage wi;
		SnapshotParameters parameters = new SnapshotParameters();
		parameters.setFill(Color.HONEYDEW);

		int imageWidth  = (int) node.getBoundsInLocal().getWidth();
		int imageHeight = (int) node.getBoundsInLocal().getHeight();

		wi = new WritableImage(imageWidth, imageHeight);
		node.snapshot(parameters, wi);

		Image image = wi;

		BufferedImage bufImageARGB = SwingFXUtils.fromFXImage(image, null);
		BufferedImage bufImageRGB = new BufferedImage(bufImageARGB.getWidth(), bufImageARGB.getHeight(), BufferedImage.OPAQUE);

		Graphics2D graphics = bufImageRGB.createGraphics();
		graphics.drawImage(bufImageARGB, 0, 0, null);

		try {
			ImageIO.write(bufImageRGB, "jpg", new File(jpegfilename));
		} catch (IOException e) {
			e.getMessage();
		}

		graphics.dispose();

	}
}