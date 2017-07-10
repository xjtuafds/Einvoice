package com.park.einvoice.common;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 此类存放关于pdf类型的各种操作
 * @author WangYuefei
 *
 */
public class PdfTools {
	
	public static File pdf2PicFile(File pdfFile, String name){
		try {
			PDDocument doc = PDDocument.load(pdfFile);
			PDFRenderer renderer = new PDFRenderer(doc);
			int pageCount = doc.getNumberOfPages();
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			String path = request.getSession().getServletContext().getRealPath("/WEB-INF/pdf/");
			File imgFile = new File(path + name + ".png");
			for(int i=0;i<pageCount;i++){
				//第二个参数为分辨率
				BufferedImage image = renderer.renderImageWithDPI(i, 296);
				//第二个参数为缩放比
				//BufferedImage image = renderer.renderImage(i, 2.5f);
				ImageIO.write(image, "PNG", imgFile);
			}
			return imgFile;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
    
}
