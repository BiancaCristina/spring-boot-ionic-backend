package br.ufu.facom.cursomc.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

@Service
public class S3Service {

	private Logger LOG = LoggerFactory.getLogger(S3Service.class);

	@Autowired
	private AmazonS3 s3client;

	@Value("${s3.bucket}")
	private String bucketName; // Pega o nome do meu bucket no AmazonS3

	public URI uploadFile(MultipartFile multiPartFile) {
		// Esse metodo faz o upload de arquivos la pro AmazonS3 e retorna o endereco web
		// do novo recurso que foi gerado (URI)
		try {
			String fileName = multiPartFile.getOriginalFilename(); // Pega o nome do arquivo
			InputStream is = multiPartFile.getInputStream();
			String contentType = multiPartFile.getContentType(); // Informacao do tipo de arquivo que foi enviado (extensao)
			return uploadFile(is, fileName, contentType);		
		} 
		
		catch (IOException e) {
			throw new RuntimeException("Erro de IO: " + e.getMessage());
		}

	}

	public URI uploadFile(InputStream is, String fileName, String contentType) {
		// Metodo sobrecarga do uploadFile
		try {
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentType(contentType);

			LOG.info("Iniciando upload");
			s3client.putObject(bucketName, fileName, is, meta);
			LOG.info("Upload finalizado");

			return s3client.getUrl(bucketName, fileName).toURI(); // Retorna URI
		}

		catch (URISyntaxException e) {
			throw new RuntimeException("Erro ao converter URL para URI");
		}
	}
}
