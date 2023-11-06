import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReadingPoem {
    public static Map<String, Map<String, Long>> readPoem(File poem) throws Exception {

         try (FileReader fr = new FileReader(poem)) {
            BufferedReader br = new BufferedReader(fr);

            Map<String, Map<String, Long>> nextWordDict = br.lines()
            // Clean the line
            .map(line -> line.trim().replaceAll("[^\\sa-zA-Z0-9]", ""))
            // Remove empty lines
            .filter(line -> line.length() > 0)
            // line -> array of words
            .map(line -> line.split(" "))
            // array of words - array of word pairs
            .map(words -> {
                String[] wordPairs = new String[words.length];
                for(int i = 0; i < words.length; i++) {
                    String wordPair;
                    if (i != words.length - 1) {
                        wordPair = String.format("%s %s", words[i], words[i+1]);
                    } else {
                        wordPair = String.format("%s %s", words[i], "\\n");;
                    }
                    wordPairs[i] = wordPair;
                }
                return wordPairs;
            })
            // array of word pairs -> word pair arrays
            .flatMap(wordPairs -> Stream.of(wordPairs))
            .map(wordPair -> wordPair.toLowerCase())
            // .peek(System.out::println)
            .map(wordPair -> wordPair.split(" "))
            // word pair array -> map of word : (map of next word: count)
            .collect(Collectors.groupingBy(wordPair -> wordPair[0], Collectors.groupingBy(wordPair -> wordPair[1], Collectors.counting())))
            ;

            return nextWordDict;
         }
        
    }
}