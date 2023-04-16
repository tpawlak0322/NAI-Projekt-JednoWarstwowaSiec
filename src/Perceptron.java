import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Perceptron {
    public HashMap<String, Integer> parseResult;

    private double[] wektorWag;
    private double offset;
    private IActivationFunction activationFunction;
    private String lookedForParam;


    public String getLookedForParam() {
        return lookedForParam;
    }

    public Perceptron(double[] wektorWag, double offset, IActivationFunction activationFunction, LinkedHashMap<String, Integer> parseResult, String lookedForParam) {
        this.wektorWag = wektorWag;
        this.offset = offset;
        this.activationFunction = activationFunction;
        this.parseResult = parseResult;
        this.lookedForParam = lookedForParam;

        //sprawdz klucze i wartości
//        parseResult.forEach((x,y) -> System.out.println(x + " val: " + y));

    }

    public Perceptron(int dimension, double offset, IActivationFunction activationFunction, LinkedHashMap parseResult, String lookedForParam) {
        this(null, offset, activationFunction, parseResult, lookedForParam);
        wektorWag = new double[dimension];
        wektorWag = Arrays.stream(wektorWag).map(x -> ((Math.random() * 10) + 1)).toArray();
//        Arrays.stream(wektorWag).forEach(System.out::println);

    }


    public double calcResult(MLVector MLVectorInput) {
        if (wektorWag.length != MLVectorInput.getData().length)
            throw new RuntimeException("wektor wag i wektor wejsc mają różne długości");

        double net = calcNet(MLVectorInput.getData());
//        System.out.println(net);
        return (double)activationFunction.calc(net);
    }


    private double calcNet(double[] vectorInput) {
        double net = 0;
        //dodanie do net iloczynu wektora wag i wektora wejsc
        for (int i = 0; i < wektorWag.length; i++)
            net += wektorWag[i] * vectorInput[i];
//        System.out.println(net);
        // uwzględnienie progu
        net -= offset;

        return net;
    }

    /**
     * Modyfikuje wagi oraz wartość progu
     *
     * @param teachVector
     * @return Otrzymana wartość wyjściowa dla danego wektora
     */
    public double teachPerceptron(MLVector<String> teachVector) {
//        System.out.println("Offset:" + offset);
//        System.out.println("Wektor Wag");
//        Arrays.stream(wektorWag).forEach(x-> System.out.print(x + ", "));
//        System.out.println();
//        System.out.println();
        //Oczekiwana wartość wyjściowa
        int d = parseResult.get(teachVector.Name);
        //Otrzymana wartość wyjściowa
        double y = calcResult(teachVector);
//        System.out.println("d" + d + " y " + y);
        // modyfikacja wag
        for (int i = 0; i < wektorWag.length; i++)
            wektorWag[i] = wektorWag[i] + Main.alfa * (d - y) * teachVector.getData()[i];
        // modyfikacja progu
        offset = offset - Main.alfa * (d - y);
        return y;
    }

    public double teachPerceptronForSigmoid(MLVector<String> teachVector) {
//        System.out.println("Offset:" + offset);
//        System.out.println("Wektor Wag");
//        Arrays.stream(wektorWag).forEach(x-> System.out.print(x + ", "));
//        System.out.println();
//        System.out.println();
        //Oczekiwana wartość wyjściowa
        double d = parseResult.get(teachVector.Name);
        //Otrzymana wartość wyjściowa
        double y = calcResult(teachVector);

//        System.out.println("d" + d + "y" + y);
        // modyfikacja wag
        for (int i = 0; i < wektorWag.length; i++)
            wektorWag[i] = wektorWag[i] + (Main.alfa * (d - y) * y * (1 - y) * teachVector.getData()[i]);
        // modyfikacja progu
        offset = offset - (Main.alfa * (d - y) * y * (1 - y));

        return y;
    }

    public double iterationError(String[] expectedExitValueArray, int[] exitValueArray,HashMap<String,Integer> mainHashMap) {
        double D = exitValueArray.length;
        double sum = 0;
        for (int i = 0; i < D; i++) {
            int returnedResult = 0;
            if (exitValueArray[i] == mainHashMap.get(lookedForParam))
                returnedResult = 1;
//            System.out.println(exitValueArray[i]);
                sum += Math.pow(parseResult.get(expectedExitValueArray[i]) - returnedResult, 2);
        }
//        System.out.println(sum/D);
        return sum / D;
    }


}
