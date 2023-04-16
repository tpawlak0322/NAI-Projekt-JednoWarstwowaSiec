import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class Main {
    public static double alfa = 0.01;
    public static double Emax = 0.017;
    private static final String pathToTrainData = "C:\\Users\\tpawl\\Downloads\\drive-download-20230415T184539Z-001\\lang.train.csv";
    private static final String pathToTestData = "C:\\Users\\tpawl\\Downloads\\drive-download-20230415T184539Z-001\\lang.test.csv";
    private static HashMap<String, Integer> parseResultHashMap = new LinkedHashMap<>();

    private static ArrayList<MLVector<String>> objects_data_train;
    private static MultiPerceptron multiPerceptron;

    public static void main(String[] args) {

        objects_data_train = parseTextToArray(pathToTrainData);
        multiPerceptron = new MultiPerceptron(parseResultHashMap, objects_data_train);


        while (true) {
            System.out.println("1. Naucz algorytm na danych testowych");
            System.out.println("2. Podaj własny wektor");
            System.out.println("3. Wprowadz Emax");
            System.out.println("4. Wprowadz nowe alfa");
            System.out.println("5. Przetestuj na zbiorze testowym");
            System.out.println("6. Exit");
            String input = new Scanner(System.in).nextLine();
            switch (input) {
                case "1":
                    multiPerceptron.teachPerceptrons();
                    break;
                case "2":
                    testForVector();
                    break;
                case "3":
                    System.out.println("Podaj nowe Emax");
                    Emax = Double.parseDouble(new Scanner(System.in).nextLine());
                    break;
                case "4":
                    System.out.println("Podaj nowe alfa");
                    alfa = Double.parseDouble(new Scanner(System.in).nextLine());
                    break;
                case "5":
                    checkAccuracyForTrainData();
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Uknown operation");
            }
        }
    }

    private static void checkAccuracyForTrainData() {
        ArrayList<MLVector<String>> objects_data_test;
        objects_data_test = parseTextToArray(pathToTestData);
        int numberOfIterations = objects_data_test.size();
        int correct = 0;
        for (MLVector<String> vec : objects_data_test) {

            String wantedResult = vec.Name;
            String result = (String) parseResultHashMap.keySet().toArray()[multiPerceptron.calcResult(vec)];
            if (result.equals(wantedResult))
                correct++;

            if (!wantedResult.equals(result))
                System.out.println("Expected: " + wantedResult + " Result: " + result);


        }
        double accuracy = (double) (correct) / (numberOfIterations);
        accuracy *= 10000;
        accuracy = Math.round(accuracy) / 100;

        System.out.println("ACCURACY: " + accuracy + "%");
    }



    public static void testForVector() {
        System.out.println("Podaj atrybuty");
        String input = new Scanner(System.in).nextLine();
        MLVector inputMLVector = MLVector.parseStringToMLVector(input);
//        System.out.println("Provided vector: " + inputMLVector);
        String result = (String) parseResultHashMap.keySet().toArray()[multiPerceptron.calcResult(inputMLVector)];
        System.out.println("Result: " + result);

    }

    public static ArrayList<MLVector<String>> parseTextToArray(String PathToFile) {
        ArrayList<MLVector<String>> mlVectors = new ArrayList<>();
        try {
            for (String s : Files.readAllLines(Paths.get(PathToFile))) {

                MLVector<String> toAdd = MLVector.parseStringToMLVector(s);
                mlVectors.add(toAdd);
                if (parseResultHashMap.containsKey(toAdd.Name)) continue;
                parseResultHashMap.put(toAdd.Name, parseResultHashMap.size());

            }
            System.out.println("Keys:");
            parseResultHashMap.entrySet().stream().forEach(System.out::println);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return mlVectors;
    }
}