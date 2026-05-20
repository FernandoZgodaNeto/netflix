import utils.TextNormalizer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class NetflixAnalyzer {

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Uso: java NetflixAnalyzer <input_csv> <output_dir>");
            System.exit(1);
        }

        String inputPath = args[0];
        String outputDir = args[1];

        new File(outputDir).mkdirs();

        System.out.println("===== ANÁLISE DO DATASET NETFLIX =====\n");
        System.out.println("Lendo arquivo: " + inputPath);

        List<TitleDescription> titles = new ArrayList<>();
        Map<String, Long> wordFrequency = new HashMap<>();
        long totalWords = 0;

        List<String> lines = Files.readAllLines(Paths.get(inputPath));
        System.out.println("Total de linhas: " + lines.size());

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] fields = parseCSV(line);

            if (fields.length < 12) {
                continue;
            }

            String title = fields[2].trim();
            String description = fields[11].trim();

            if (description == null || description.isEmpty()) {
                continue;
            }

            String normalizedDesc = TextNormalizer.normalize(description);

            if (normalizedDesc.isEmpty()) {
                continue;
            }

            String[] words = TextNormalizer.getWords(normalizedDesc);
            int wordCount = words.length;
            totalWords += wordCount;

            titles.add(new TitleDescription(title, wordCount, normalizedDesc));

            String[] wordsWithoutStopwords = TextNormalizer.getWordsWithoutStopwords(normalizedDesc);
            for (String word : wordsWithoutStopwords) {
                if (!word.isEmpty()) {
                    wordFrequency.put(word, wordFrequency.getOrDefault(word, 0L) + 1);
                }
            }
        }

        System.out.println("Títulos processados: " + titles.size());
        System.out.println("Palavras únicas (sem stopwords): " + wordFrequency.size());
        System.out.println();

        generateReport(titles, wordFrequency, totalWords, outputDir);

        System.exit(0);
    }

    private static void generateReport(List<TitleDescription> titles,
                                       Map<String, Long> wordFrequency,
                                       long totalWords,
                                       String outputDir) throws IOException {

        System.out.println("========== RESULTADOS FINAIS ==========\n");

        if (!titles.isEmpty()) {
            TitleDescription longest = titles.stream()
                    .max(Comparator.comparingInt(t -> t.wordCount))
                    .orElse(null);

            if (longest != null) {
                System.out.println("1. TÍTULO COM DESCRIÇÃO MAIS LONGA:");
                System.out.println("   Título: " + longest.title);
                System.out.println("   Palavras: " + longest.wordCount);
                System.out.println();
            }
        }

        if (!titles.isEmpty()) {
            TitleDescription shortest = titles.stream()
                    .min(Comparator.comparingInt(t -> t.wordCount))
                    .orElse(null);

            if (shortest != null) {
                System.out.println("2. TÍTULO COM DESCRIÇÃO MAIS CURTA:");
                System.out.println("   Título: " + shortest.title);
                System.out.println("   Palavras: " + shortest.wordCount);
                System.out.println();
            }
        }

        System.out.println("3. TOTAL DE PALAVRAS EM TODAS AS DESCRIÇÕES:");
        System.out.println("   " + totalWords + " palavras");
        System.out.println();

        System.out.println("4. TOP 5 PALAVRAS MAIS FREQUENTES:");
        List<Map.Entry<String, Long>> topWords = wordFrequency.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(5)
                .collect(Collectors.toList());

        for (int i = 0; i < topWords.size(); i++) {
            Map.Entry<String, Long> entry = topWords.get(i);
            System.out.println("   " + (i + 1) + ". " + entry.getKey() + ": " + entry.getValue() + " vezes");
        }
        System.out.println();

        System.out.println("5. TOP 5 PALAVRAS MENOS FREQUENTES:");
        List<Map.Entry<String, Long>> bottomWords = wordFrequency.entrySet().stream()
                .sorted(Comparator.comparingLong(Map.Entry::getValue))
                .limit(5)
                .collect(Collectors.toList());

        for (int i = 0; i < bottomWords.size(); i++) {
            Map.Entry<String, Long> entry = bottomWords.get(i);
            System.out.println("   " + (i + 1) + ". " + entry.getKey() + ": " + entry.getValue() + " vezes");
        }
        System.out.println();

        String reportPath = outputDir + File.separator + "relatorio.txt";
        try (FileWriter fw = new FileWriter(reportPath)) {
            fw.write("========== RELATÓRIO DE ANÁLISE NETFLIX ==========\n\n");

            if (!titles.isEmpty()) {
                TitleDescription longest = titles.stream()
                        .max(Comparator.comparingInt(t -> t.wordCount))
                        .orElse(null);
                if (longest != null) {
                    fw.write("1. TÍTULO COM DESCRIÇÃO MAIS LONGA:\n");
                    fw.write("   Título: " + longest.title + "\n");
                    fw.write("   Palavras: " + longest.wordCount + "\n\n");
                }
            }

            if (!titles.isEmpty()) {
                TitleDescription shortest = titles.stream()
                        .min(Comparator.comparingInt(t -> t.wordCount))
                        .orElse(null);
                if (shortest != null) {
                    fw.write("2. TÍTULO COM DESCRIÇÃO MAIS CURTA:\n");
                    fw.write("   Título: " + shortest.title + "\n");
                    fw.write("   Palavras: " + shortest.wordCount + "\n\n");
                }
            }

            fw.write("3. TOTAL DE PALAVRAS:\n");
            fw.write("   " + totalWords + " palavras\n\n");

            fw.write("4. TOP 5 PALAVRAS MAIS FREQUENTES:\n");
            for (int i = 0; i < topWords.size(); i++) {
                Map.Entry<String, Long> entry = topWords.get(i);
                fw.write("   " + (i + 1) + ". " + entry.getKey() + ": " + entry.getValue() + " vezes\n");
            }
            fw.write("\n");

            fw.write("5. TOP 5 PALAVRAS MENOS FREQUENTES:\n");
            for (int i = 0; i < bottomWords.size(); i++) {
                Map.Entry<String, Long> entry = bottomWords.get(i);
                fw.write("   " + (i + 1) + ". " + entry.getKey() + ": " + entry.getValue() + " vezes\n");
            }
        }

        System.out.println("Relatório salvo em: " + reportPath);
        System.out.println("=======================================\n");
    }

    private static String[] parseCSV(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        result.add(current.toString());

        return result.toArray(new String[0]);
    }

    private static class TitleDescription {
        String title;
        int wordCount;
        String normalizedDescription;

        TitleDescription(String title, int wordCount, String normalizedDescription) {
            this.title = title;
            this.wordCount = wordCount;
            this.normalizedDescription = normalizedDescription;
        }
    }
}