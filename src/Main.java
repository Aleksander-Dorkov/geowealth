import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        List<String> allWords = loadWordsFromURL()
                .stream()
                .map(String::toUpperCase) // just in any case to avoid lower/upper confusion
                .collect(Collectors.toList());

        long startTime = System.nanoTime();

        Set<String> nineLetterWords = allWords.stream()
                .filter(s -> s.length() == 9)
                .collect(Collectors.toSet());

        Set<String> lessThan9LetterWords = allWords.stream()
                .filter(s -> s.length() < 9)
                .collect(Collectors.toSet());
        lessThan9LetterWords.add("I");
        lessThan9LetterWords.add("A");

        System.out.printf("Data:\nTotal words: %d\n9 letter words: %d\nless then 9 letter words: %d\n", allWords.size(), nineLetterWords.size(), lessThan9LetterWords.size());

        List<String> solution = new LinkedList<>();
        for (String nineLetterWord : nineLetterWords) {
            if (isValid(nineLetterWord, lessThan9LetterWords)) {
                solution.add(nineLetterWord);
            }
        }
        System.out.printf("\nSolution:\n9 letter valid words count: %d\n%s\n\n", solution.size(), solution);

        long duration = System.nanoTime() - startTime;
        System.out.println("Duration in seconds: " + duration / 1_000_000_000.0);
    }

    private static boolean isValid(String word, Set<String> allWordsSet) {
        Queue<String> queue = new LinkedList<>();
        queue.add(word);

        while (!queue.isEmpty()) {
            String currentWord = queue.poll();

            if (currentWord.length() == 1) {
                if (currentWord.equals("I") || currentWord.equals("A")) {
                    return true;
                }
                continue;
            }

            for (int i = 0; i < currentWord.length(); i++) {
                String shorterWord = currentWord.substring(0, i) + currentWord.substring(i + 1);
//                System.out.println("Checking: " + shorterWord);
                if (allWordsSet.contains(shorterWord)) {
                    queue.add(shorterWord);
                }
            }
        }

        return false;
    }

    private static List<String> loadWordsFromURL() {
        List<String> words = new LinkedList<>();
        try {
            URL url = new URL("https://raw.githubusercontent.com/nikiiv/JavaCodingTestOne/master/scrabble-words.txt");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
                words = reader.lines().collect(Collectors.toCollection(LinkedList::new));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }
}
