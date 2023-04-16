public class MLVector<T> {
    T Name;
    private static final int NUMBER_OF_ATTRIBUTES = 26; // Ilość znakow które ma pobrać i przekształcić na atrybut z wczytanego stringa

    private int dimension;

    private double[] data;


    public MLVector(T Name, int dimensions) {

        data = new double[dimensions];
        this.dimension = dimensions;
        this.Name = Name;
    }

    public MLVector(T Name, double[] data) {

        this.data = data;
        this.dimension = data.length;
        this.Name = Name;
    }

    public MLVector add(int... Value) {
        for (int i = 0; i < dimension; i++)
            data[i] = Value[i];
        return this;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++)
            sb.append((i + 1) + ". " + data[i] + ", ");
        return Name + " attributes: " + sb;
    }

    public double[] getData() {
        return data;
    }

    public int getDimension() {
        return dimension;
    }

    public static int countOccurrences(String str, char ch) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ch) {
                count++;
            }
        }
        return count;
    }
    public static MLVector parseStringToMLVector(String text) {
        String decisionParameter = text.substring(0,text.indexOf(','));
        text = text.substring(text.indexOf(',')+2); // Plus 2 bo ',"'
        text = text.replaceAll("[^a-zA-Z\s]", "");
        text = text.toLowerCase();
//        System.out.println("decisionParameter: " + decisionParameter + ", text: " + text);
        double[] attributes = new double[NUMBER_OF_ATTRIBUTES];
        for (int i = 97; i < NUMBER_OF_ATTRIBUTES+97; i++) { // -97 bo 'a' w ascii to 97
            attributes[i-97] = (double)(countOccurrences(text,(char)i))/text.length();
//            System.out.println(attributes[i-97]);
        }
        return new MLVector(decisionParameter, attributes);
    }
}
