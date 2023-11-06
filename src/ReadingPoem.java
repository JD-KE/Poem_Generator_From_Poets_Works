import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReadingPoem {
    public static void main(String[] args) throws Exception {

         try (FileReader fr = new FileReader(args[0])) {
            BufferedReader br = new BufferedReader(fr);

            Map<String, Map<String, Long>> name = br.lines()
            // Clean the line
            .map(line -> line.trim().replaceAll("[^\\sa-zA-Z0-9]", ""))
            // Remove empty lines
            .filter(line -> line.length() > 0)
            // line -> array of words
            .map(line -> line.split(" "))
            // .map(words ->{
            //     String[][] wordPairs = new String[words.length][2];
            //     for(int i = 0; i < words.length; i++) {
            //         String[] wordPair = new String[2];
            //         if (i != words.length - 1) {
            //             wordPair[0] = words[i]; wordPair[1] = words[i+1];
            //         } else {
            //             wordPair[0] = words[i]; wordPair[1] = "\\n";
            //         }
            //         wordPairs[i] = wordPair;
            //     }
            //     return wordPairs;

            // })
            // .flatMap(wordPairs ->Stream.of(wordPairs))
            // .peek(System.out::println)
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
            .flatMap(wordPairs -> Stream.of(wordPairs))
            .peek(System.out::println)
            .map(wordPair -> wordPair.split(" "))
            .collect(Collectors.groupingBy(wordPair -> wordPair[0], Collectors.groupingBy(wordPair -> wordPair[1], Collectors.counting())))
            ;

            System.out.println(name);
         }
        
    }
}