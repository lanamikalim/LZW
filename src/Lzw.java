import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Lzw {

	public static final int DEFAULT_DICTIONARY_SIZE = 256;


	public List<Integer> compress(String input) {

		int dictSize = DEFAULT_DICTIONARY_SIZE;
		Map<String,Integer> dictionary = buildDictionaryForCompression(dictSize);

		List<Integer> results = new ArrayList<Integer>();

		String word = "";
		for (char character : input.toCharArray()) {
			String wordCharacter = word + character;
			if (dictionary.containsKey(wordCharacter))
				word = wordCharacter;
			else {
				results.add(dictionary.get(word));
				dictionary.put(wordCharacter, dictSize++);
				word = "" + character;
			}
		}

		if (!word.equals("")) {
			results.add(dictionary.get(word));
		}

		return results;
	}

	public String decompress(List<Integer> compressedInput) throws FileNotFoundException {

		int dictionarySize = DEFAULT_DICTIONARY_SIZE;
		Map<Integer,String> dictionary = buildDictionaryForDecompression(dictionarySize);

		String word = "" + (char)(int)compressedInput.remove(0);

		StringBuffer result = new StringBuffer(word);

		for (int digit : compressedInput) {
			String entry;

			if (dictionary.containsKey(digit)) {
				entry =  dictionary.get(digit);
			}
			else if (digit == dictionarySize) {
				entry = word + word.charAt(0);
			} 
			else {
				throw new IllegalArgumentException("Bad compressed digit: " + digit);
			}

			result.append(entry);

			dictionary.put(dictionarySize++, word + entry.charAt(0));

			word = entry;
		}

		//	        System.out.println(dictionary.toString());
		/* 
		 * BEGIN MIYAJIMA CODE
		 */
		PrintWriter output = new PrintWriter("out.txt");
		output.print(result.toString());
		output.close();
		return result.toString();
	}

	private Map<Integer,String> buildDictionaryForDecompression(int dictionarySize) {
		Map<Integer,String> dictionary = new HashMap<Integer,String>();
		for (int i = 0; i < dictionarySize; i++) {
			dictionary.put(i, "" + (char)i);
		}
		return dictionary;
	}

	private Map<String,Integer> buildDictionaryForCompression(int dictionarySize) {
		Map<String,Integer> dictionary = new HashMap<String,Integer>();
		for (int i = 0; i < dictionarySize; i++) {
			dictionary.put("" + (char)i, i);
		}
		return dictionary;
	}
}