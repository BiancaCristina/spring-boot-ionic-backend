package br.ufu.facom.cursomc.services;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.ufu.facom.cursomc.services.exceptions.FileException;

@Service
public class ImageService {
	
	public BufferedImage getJpgImageFromFile(MultipartFile uploadedFile) {
		// Esse metodo recebe o uploadeadFile e retorna BufferedImage (uma imagem no formato .jpg)
		
		// O comando abaixo pega a extensao do arquivo uploadedFile
		String ext = FilenameUtils.getExtension(uploadedFile.getOriginalFilename());
		// Fim do comando
		
		if (!"png".equals(ext) && !"jpg".equals(ext)) {
			// Verifica se eh .png ou .jpg
			throw new FileException("Somente imagens PNG e JPG são permitidas");
		}
		
		// O comando abaixo tenta obter BufferedImage a partir do uploadeadFile
		try {
			BufferedImage img = ImageIO.read(uploadedFile.getInputStream()); 
			
			// Testa se o arquivo eh .png, se for precisa converter em .jpg
			if ("png".equals(ext)) {
				// Se for .png, converte em .jpg
				img = pngToJpg(img);
			}
			
			return img;
		} 
		
		catch (IOException e) {
			throw new FileException("Erro ao ler arquivo");
		}
	}

	public BufferedImage pngToJpg(BufferedImage img) {
		// Esse metodo onverte imagem .png em .jpg
		BufferedImage jpgImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);	
		
		jpgImage.createGraphics().drawImage(img, 0, 0, Color.WHITE, null);
		
		return jpgImage;
	}
	
	public InputStream getInputStream(BufferedImage img, String extension) {
		// Esse metodo retorna um InputStream a partir de um BufferedImage
		// Esse metodo eh usado porque o metodo que faz upload pro S3 recebe um InputStream, entao preciso fazer essa conversao
		
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(img, extension, os);
			
			return new ByteArrayInputStream(os.toByteArray());
		} 
		
		catch (IOException e) {
			throw new FileException("Erro ao ler arquivo");
		}
	}
	
	public BufferedImage cropSquare(BufferedImage sourceImg) {
		// Esse metodo "recorta" a imagem
		
		// Esse comando descobre qual eh a menor dimensao (largura ou altura)
		int min = (sourceImg.getHeight() <= sourceImg.getWidth()) ? sourceImg.getHeight() : sourceImg.getWidth();
		// Fim do comando
		
		// O return abaixo retrona a imagem recortada 
		return Scalr.crop(
			sourceImg, 
			(sourceImg.getWidth()/2) - (min/2), 
			(sourceImg.getHeight()/2) - (min/2), 
			min, 
			min);		
		// Fim do return
	}
	
	public BufferedImage resize(BufferedImage sourceImg, int size) {
		// Esse metodo redimensiona a imagem
		return Scalr.resize(sourceImg, Scalr.Method.ULTRA_QUALITY, size);
	}
	
	
	
}
