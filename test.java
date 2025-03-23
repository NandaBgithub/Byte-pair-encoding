import java.util.HashSet;

public class test {
public static void main(String[] args) throws Exception {
        BytePairParallelised bpe = new BytePairParallelised();
        String input = "abbabbaaabbabbaaabb";
        HashSet<Character> charSet = new HashSet<>();

        // Was lazy to get all alphanumeric characters as possible replacements, so just use these instead
        for (char c = 'a'; c <= 'z'; c++) {
            charSet.add(c);
        }

        // Call the encode method to apply the byte pair encoding
        String encodedString = bpe.encode(input, charSet);

        // Print the original and encoded strings
        System.out.println("Original String: " + input);
        System.out.println("Encoded String: " + encodedString);
    }
}
