
// Programado por Michael Pereira

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

public class Spelling{

	private final SortedSet<String> nWords = new TreeSet<String>();;
	private int endStr;
	

	private final char[] alfabeto = { 'a', 'á', 'ä', 'à', 'ã', 'â', 'b', 'c', 'ç', 'd', 'e', 'é', 'ë', 'è', 'ê', 'f',
			'g', 'h', 'i', 'í', 'ï', 'ì', 'î', 'j', 'k', 'l', 'm', 'n', 'o', 'ó', 'ö', 'ò', 'õ', 'ô', 'p', 'q', 'r',
			's', 't', 'u', 'ú', 'ü', 'ù', 'û', 'v', 'x', 'y', 'z' };

	public Spelling(){
		
	}
	
	public Spelling(String file) throws IOException {
		
		java.io.BufferedReader in = new java.io.BufferedReader(new InputStreamReader(new FileInputStream(file)));
		
		for (String temp = ""; temp != null; temp = in.readLine()) {
			endStr = 0;
			for (int i = 0; i < temp.length(); i++) {
				if (Character.toString(temp.charAt(i)).equals("/")) {
					endStr = i;
				}
			}
			if (endStr != 0)
				nWords.add(temp.substring(0, endStr));
			else
				nWords.add(temp);
		}
		in.close();
	}
	
	private ArrayList<String> edits(String word) {
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i < word.length(); ++i) {
			result.add(word.substring(0, i) + word.substring(i + 1));
		}
		for (int i = 0; i < word.length() - 1; ++i) {
			result.add(word.substring(0, i) + word.substring(i + 1, i + 2) + word.substring(i, i + 1)
					+ word.substring(i + 2));
		}
		for (int i = 0; i < word.length(); ++i) {
			for (int z = 0; z < alfabeto.length; z++) {
				char c = alfabeto[z];
				result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i + 1));
			}
		}
		for (

		int i = 0; i <= word.length(); ++i)

		{
			for (int z = 0; z < alfabeto.length; z++) {
				char c = alfabeto[z];
				result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i));
			}

		}
		return result;
	}

	public final String[] correct(String word) {
		
		if(word.length()==1)
			return null;
		
		if (word.matches("[0-9]*"))
			return null;

		if (nWords.contains(word)) {
			return null;
		}
		ArrayList<String> list = edits(word);
		SortedSet<String> candidates = new TreeSet<String>();
		String[] noCandidates = {"noCand"};
		
		for (String s : list) {
			if (nWords.contains(s)) {
				candidates.add(s);
			}
		}

		if (candidates.size() > 0) {
			return candidates.toArray(new String[0]);
		}
		for (String s : list) {
			for (String w : edits(s)) {
				if (nWords.contains(w)) {
					candidates.add(w);
				}
			}
		}
		return candidates.size() > 0 ? candidates.toArray(new String[0]) : noCandidates;
	}

	public void addToDictionary(String palavra, String path) {

		FileWriter fileWrite = null;
		try {
			fileWrite = new FileWriter(path,true);

			String palavraAux = (System.getProperty("line.separator") + palavra);

			// Gravando palavra no pdf
			fileWrite.append(palavraAux);
			nWords.add(palavra);
			System.out.println(palavra);
			
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		try {
			fileWrite.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
