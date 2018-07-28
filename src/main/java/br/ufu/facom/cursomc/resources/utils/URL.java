package br.ufu.facom.cursomc.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class URL {
	
	public static List<Integer> decodeIntList(String s){
		// Esse metodo aqui converte um string como "1,2,3" em uma lista de IDS
		String[] vet = s.split(",");
		List<Integer> list = new ArrayList<>();
		
		for (int i =0;i<vet.length;i++) {
			list.add(Integer.parseInt(vet[i]));
		}
		
		return list;
		
		// O return abaixo substitui tudo que foi escrito acima
		//return Arrays.asList(s.split(",")).stream().map(x -> Integer.parseInt(x)).collect(Collectors.toList());
	}
	
	public static String decodeParam(String s) {
		// Faz um "encode" da string. Esse encode eh pra prevenir que venha caracteres especiais tipo o espaco quando o usuario digita o nome da categoria
		// Dessa forma, caso seja digitado "TV LED", o encode faz com que isso fique tipo "TV%20LED", sendo %20 o encode pra espaco
		// UTF-8 eh o padrao de 
		
		try {
			return URLDecoder.decode(s, "UTF-8");
		} 
		
		catch (UnsupportedEncodingException e) {
			return ""; // Caso de erro, eh retornada uma string vazia
		}
	}
}
