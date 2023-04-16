import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class MultiPerceptron {


    private ArrayList<MLVector<String>> objectsDataTrain;
    private LinkedHashMap<String, Integer>[] normalizedHashMapForPerceptrons;
    private HashMap<String, Integer> parseResultHashMap;
    private static final double sigmoidSteepParam = 0.01;
    int numberOfPerceptrons;
    private Perceptron[] perceptronArray;
    private static final IActivationFunction activationFunction = (x) -> {
        double toReturn = 1d / (1d + Math.exp(-x* sigmoidSteepParam));
//        System.out.println(toReturn);
        return toReturn;

    };

    public MultiPerceptron(HashMap<String, Integer> parseResultHashMap, ArrayList<MLVector<String>> objectsDataTrain) {
        this.parseResultHashMap = parseResultHashMap;
        this.objectsDataTrain = objectsDataTrain;
        this.numberOfPerceptrons = parseResultHashMap.size();
        this.perceptronArray = new Perceptron[numberOfPerceptrons];

        normalizedHashMapForPerceptrons = new LinkedHashMap[numberOfPerceptrons]; // tylko jeden klucz ma wartość 1 reszta 0
        //init hash map
        for (int i = 0; i < numberOfPerceptrons; i++)
            normalizedHashMapForPerceptrons[i] = new LinkedHashMap<>();

        ArrayList<String> keys = new ArrayList<>();
        parseResultHashMap.entrySet().stream().forEach(x -> keys.add(x.getKey()));

        for (int i = 0; i < numberOfPerceptrons; i++) {
            for (int j = 0; j < numberOfPerceptrons; j++) {
                int value = 0;
                if (i == j)
                    value = 1;
                normalizedHashMapForPerceptrons[i].put(keys.get(j), value);
            }
            //Tworzenie perceptronu
            perceptronArray[i] = new Perceptron(objectsDataTrain.get(0).getData().length, 0.0, activationFunction, normalizedHashMapForPerceptrons[i], keys.get(i));
        }

    }

    private int findMaxIndex(double... nums) {
        double max = nums[0];
        int maxIndex = 0;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > max) {
                max = nums[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }


    public void teachPerceptrons() {
        double wartoscBledu = 1;

        //Stworzenie wektora z oczekiwanych wyjść
        String[] expectedResult = new String[objectsDataTrain.size()];
        for (int i = 0; i < expectedResult.length; i++)
            expectedResult[i] = objectsDataTrain.get(i).Name;
        int[] result = new int[objectsDataTrain.size()];

        int iter = 0;
        while (wartoscBledu > Main.Emax) {
            wartoscBledu = 0;

            for (int i = 0; i < objectsDataTrain.size(); i++) {
                double[] resultArr = new double[numberOfPerceptrons];
                for (int j = 0; j < numberOfPerceptrons; j++)
                    resultArr[j] = perceptronArray[j].teachPerceptronForSigmoid(objectsDataTrain.get(i));

                int maxActivationPerceptronIndex = findMaxIndex(resultArr);
                result[i] = parseResultHashMap.get(perceptronArray[maxActivationPerceptronIndex].getLookedForParam());
            }
            for (int i = 0; i < numberOfPerceptrons; i++)
                wartoscBledu += perceptronArray[i].iterationError(expectedResult, result,parseResultHashMap);
            wartoscBledu = wartoscBledu /3;
            iter++;
            if (iter % 50000 == 0)
                System.out.println(wartoscBledu);
        }
        System.out.println("Result:");
        for (int i = 0; i < objectsDataTrain.size(); i++) {
            if (expectedResult[i].equals(parseResultHashMap.keySet().toArray()[result[i]]))
                continue;
            System.out.println("Expected: " + expectedResult[i] + " Result: " + parseResultHashMap.keySet().toArray()[result[i]]);
        }

        System.out.println("Wartość błędu:");
        System.out.println(wartoscBledu);

    }

    public int calcResult(MLVector vec) {

        double[] resultArr = new double[numberOfPerceptrons];
        for (int j = 0; j < numberOfPerceptrons; j++)
            resultArr[j] = perceptronArray[j].calcResult(vec);
        int maxActivationPerceptronIndex = findMaxIndex(resultArr);
        return maxActivationPerceptronIndex;

    }


}
