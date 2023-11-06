import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ReadingPoems {

    

    public static void main(String[] args) throws Exception {
        File poemFile = new File("");
        Map<String,Map<String,Long>> nextWordDict = new TreeMap<>();
        int lines = 16;
        Random random = new Random();
        int jitter = 50;
        
        //get file name or directory name and read file/files in directory; exit if no file is input or file does not exist
        
        
        if (args.length > 0){
            poemFile = new File(args[0]);
        } else {
            System.out.println("No input detected.");
            System.exit(1);
        }

        // System.out.println(poemFile.getName());
        //produces nextWordDict from file/directory using readPoem method from ReadingPoem

        if (!poemFile.exists() || poemFile.getName() == "") {
            System.out.println("File does not exist.");
            System.exit(1);
        } else if (poemFile.isFile()) {
            nextWordDict = ReadingPoem.readPoem(poemFile);
            

        } else if (poemFile.isDirectory()) {
            System.out.println("File is directory, reading files inside");
            File[] files = poemFile.listFiles();
            Map<String,Map<String,Long>> tempDict = new TreeMap<>();

            for (File file : files) {
                
                // merge Map from next file to temp file. v1 and v2, and u1 and u2 are how duplicate keys and their values are
                //  handled, default is replacement with the mapping function
                Map<String, Map<String, Long>> nextDict = ReadingPoem.readPoem(file);
                nextDict.forEach((word, nextWordMap) -> 
                    tempDict.merge(word, nextWordMap, (v1, v2) -> 
                        {
                            v2.forEach((key, value) ->
                                v1.merge(key, value, (u1, u2) -> u1 + u2 ));
                            return v1;
                        }));

            }
            nextWordDict = tempDict;
        }

        // sort nextwords by number of times they appear
        nextWordDict = new TreeMap<String, Map<String, Long>>(sortNextWordDict(nextWordDict));

        // for (String word : nextWordDict.keySet()) {
        //     System.out.printf("%s:%n", word);
        //     Map<String, Long> nextWordMap = nextWordDict.get(word);
        //     for(String nextWord: nextWordMap.keySet()) {
        //         System.out.printf("\t%s: %d times%n", nextWord, nextWordMap.get(nextWord));
        //     }   
        // }

        List<String> keys = new ArrayList<>(nextWordDict.keySet());
        // keys.forEach(System.out::println);
        StringBuilder poem = new StringBuilder();
        String firstWord;
        String nextWord;
        int startWordIndex;

        for (int i = 0; i <= lines - 1; i++) {
            // get new word for new line and append to Stringbuilder
            startWordIndex = random.nextInt(keys.size());
            nextWord = keys.get(startWordIndex);
            // System.out.println(nextWord);
            firstWord = nextWord.length() == 1 ? nextWord.toUpperCase() : 
                String.format("%s%s", nextWord.substring(0, 1).toUpperCase(),nextWord.substring(1));
            poem.append(firstWord);
            int index = 0;
            
            while (nextWord != "\\n"){
                index++;
                // System.out.println(index);
                // get Map of next words and store keys as list
                Map<String, Long> nextWordMap = nextWordDict.get(nextWord);
                List<String> nextWords = new ArrayList<>(nextWordMap.keySet());
                // nextWords.forEach(System.out::println);
                // with random jitter chance, either get first word in list or equal chance to get any word
                if (jitter >= (random.nextInt(100) + 1)) {
                    int nextWordIndex = random.nextInt(nextWords.size());
                    nextWord = nextWords.get(nextWordIndex);
                    // System.out.printf("Random word %s%n", nextWord);
                } else {
                    nextWord = nextWords.get(0);
                    // System.out.printf("Top word %s%n", nextWord);
                }
                // System.out.println(nextWord.equals("\\n"));

                // append next word to line. End line if hit \n, else continue
                if (!nextWord.equals("\\n")) {
                    poem.append(String.format(" %s", nextWord));
                } else {
                    poem.append("\n");
                    break;
                }

            }
        }

        System.out.println(poem.toString());
    }

    public static Map<String, Map<String, Long>> sortNextWordDict (Map<String, Map<String, Long>> dict) {
        for (String word :dict.keySet()) {
            Map<String, Long> nextWordMap = dict.get(word);
            Map<String,Long> sortedNextWordMap =
                nextWordMap.entrySet().stream()
                // .sorted(Map.Entry.comparingByKey(Comparator.naturalOrder()))
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                    Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
                    // The LinkedHashMap seen above iterates entries in the order in which they were inserted. From StackOverflow.
            dict.put(word, sortedNextWordMap);
        }
        return dict;
    }
    
}
