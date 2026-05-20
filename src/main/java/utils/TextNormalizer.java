package utils;

import java.util.HashSet;
import java.util.Set;

public class TextNormalizer {

    private static final Set<String> STOPWORDS = new HashSet<>();

    static {
        STOPWORDS.add("the");
        STOPWORDS.add("a");
        STOPWORDS.add("an");
        STOPWORDS.add("and");
        STOPWORDS.add("or");
        STOPWORDS.add("but");
        STOPWORDS.add("in");
        STOPWORDS.add("on");
        STOPWORDS.add("at");
        STOPWORDS.add("to");
        STOPWORDS.add("for");
        STOPWORDS.add("of");
        STOPWORDS.add("is");
        STOPWORDS.add("be");
        STOPWORDS.add("was");
        STOPWORDS.add("been");
        STOPWORDS.add("being");
        STOPWORDS.add("have");
        STOPWORDS.add("has");
        STOPWORDS.add("had");
        STOPWORDS.add("do");
        STOPWORDS.add("does");
        STOPWORDS.add("did");
        STOPWORDS.add("with");
        STOPWORDS.add("as");
        STOPWORDS.add("from");
        STOPWORDS.add("by");
        STOPWORDS.add("it");
        STOPWORDS.add("that");
        STOPWORDS.add("this");
        STOPWORDS.add("which");
        STOPWORDS.add("who");
        STOPWORDS.add("his");
        STOPWORDS.add("her");
        STOPWORDS.add("he");
        STOPWORDS.add("she");
        STOPWORDS.add("we");
        STOPWORDS.add("they");
        STOPWORDS.add("are");
        STOPWORDS.add("am");
        STOPWORDS.add("not");
        STOPWORDS.add("no");
        STOPWORDS.add("so");
        STOPWORDS.add("can");
        STOPWORDS.add("will");
        STOPWORDS.add("would");
        STOPWORDS.add("could");
        STOPWORDS.add("should");
        STOPWORDS.add("may");
        STOPWORDS.add("might");
        STOPWORDS.add("must");
        STOPWORDS.add("shall");
        STOPWORDS.add("you");
        STOPWORDS.add("your");
        STOPWORDS.add("my");
        STOPWORDS.add("me");
        STOPWORDS.add("him");
        STOPWORDS.add("them");
        STOPWORDS.add("there");
        STOPWORDS.add("their");
        STOPWORDS.add("what");
        STOPWORDS.add("when");
        STOPWORDS.add("where");
        STOPWORDS.add("why");
        STOPWORDS.add("how");

        STOPWORDS.add("o");
        STOPWORDS.add("a");
        STOPWORDS.add("os");
        STOPWORDS.add("as");
        STOPWORDS.add("um");
        STOPWORDS.add("uma");
        STOPWORDS.add("uns");
        STOPWORDS.add("umas");
        STOPWORDS.add("e");
        STOPWORDS.add("ou");
        STOPWORDS.add("em");
        STOPWORDS.add("no");
        STOPWORDS.add("na");
        STOPWORDS.add("nos");
        STOPWORDS.add("nas");
        STOPWORDS.add("de");
        STOPWORDS.add("do");
        STOPWORDS.add("da");
        STOPWORDS.add("dos");
        STOPWORDS.add("das");
        STOPWORDS.add("ao");
        STOPWORDS.add("à");
        STOPWORDS.add("aos");
        STOPWORDS.add("às");
        STOPWORDS.add("é");
        STOPWORDS.add("são");
        STOPWORDS.add("era");
        STOPWORDS.add("eram");
        STOPWORDS.add("ser");
        STOPWORDS.add("estar");
        STOPWORDS.add("tem");
        STOPWORDS.add("têm");
        STOPWORDS.add("teve");
        STOPWORDS.add("tiver");
        STOPWORDS.add("por");
        STOPWORDS.add("para");
        STOPWORDS.add("com");
        STOPWORDS.add("sem");
        STOPWORDS.add("se");
        STOPWORDS.add("que");
    }

    public static String normalize(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        text = text.toLowerCase();

        text = text.replaceAll("[^a-záéíóúâêôãõçñ0-9\\s]", "");

        text = text.replaceAll("\\s+", " ");

        return text.trim();
    }

    public static boolean isStopword(String word) {
        return STOPWORDS.contains(word.toLowerCase());
    }

    public static String[] getWords(String normalizedText) {
        if (normalizedText == null || normalizedText.isEmpty()) {
            return new String[0];
        }

        String[] words = normalizedText.split("\\s+");
        return words;
    }

    public static String[] getWordsWithoutStopwords(String normalizedText) {
        String[] words = getWords(normalizedText);
        Set<String> filteredWords = new HashSet<>();

        for (String word : words) {
            if (!word.isEmpty() && !isStopword(word)) {
                filteredWords.add(word);
            }
        }

        return filteredWords.toArray(new String[0]);
    }
}