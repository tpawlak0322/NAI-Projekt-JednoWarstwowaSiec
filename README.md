# NAI-Projekt-JednoWarstwowaSiec

## Project Overview
This project implements a single-layer neural network (multi-perceptron) in Java for classification tasks. It is designed for educational purposes and demonstrates the basics of perceptron learning, activation functions, and vectorized data processing.

## Core Components

### 1. MultiPerceptron
- **Purpose:** Represents a layer of perceptrons, each responsible for classifying input data into one of the possible classes.
- **Key Methods:**
   - `teachPerceptrons()`: Trains all perceptrons using the provided training data until the error falls below a threshold (`Emax`).
   - `calcResult(MLVector vec)`: Returns the index of the perceptron with the highest activation for a given input vector.
- **Activation Function:**
   - The activation function is currently set to the identity function (`return x;`).
   - There is a commented-out sigmoid function: `1 / (1 + exp(-x * sigmoidSteepParam))`, which can be enabled for non-linear activation.
- **Training:**
   - Each perceptron is trained to recognize one class (one-vs-all approach).
   - The error is calculated and logged every 50,000 iterations.

### 2. Perceptron
- **Purpose:** Implements a single perceptron (neuron) with adjustable weights and bias (offset).
- **Key Methods:**
   - `calcResult(MLVector input)`: Computes the output using the activation function.
   - `teachPerceptron(MLVector teachVector)`: Updates weights and bias based on the error (for identity activation).
   - `teachPerceptronForSigmoid(MLVector teachVector)`: Updates weights and bias for sigmoid activation.
   - `iterationError(...)`: Calculates the mean squared error for the current iteration.
- **Activation Function:**
   - Passed as an `IActivationFunction` instance, allowing flexibility.

### 3. IActivationFunction
- **Purpose:** Functional interface for activation functions.
- **Method:**
   - `double calc(double input)`: Computes the activation for a given input.

### 4. MLVector
- **Purpose:** Represents a labeled data vector for machine learning.
- **Key Methods:**
   - `getData()`: Returns the feature array.
   - `parseStringToMLVector(String text)`: Converts a string to an `MLVector` by extracting the label and calculating character frequencies as features.

### 5. Main
- **Purpose:** Entry point for running the application and interacting with the user.
- **Features:**
   - Allows training, testing, and adjusting hyperparameters (`alfa`, `Emax`).
   - Supports testing on custom input and test datasets.

## Activation Function Used
- **Default:** Identity function (`f(x) = x`).
- **Alternative (commented):** Sigmoid function (`f(x) = 1 / (1 + exp(-x * sigmoidSteepParam))`).
- You can switch to sigmoid by uncommenting the relevant line in `MultiPerceptron`.

## How the MultiPerceptron Layer Works
- Each perceptron in the layer is trained to recognize a specific class.
- For a given input, all perceptrons compute their activation; the class with the highest activation is chosen as the result.
- Training is performed in a one-vs-all fashion, updating weights and bias to minimize classification error.


## Data Requirements
The project requires a `Data/lang.train.csv` file inside a `Data` folder in the project root. The file should contain training data in the following format:

```
<label>,<text>
<label>,<text>
...
```

- **label**: The class or category for the sample (e.g., language name).
- **text**: The text sample to be analyzed (e.g., a sentence or paragraph).

Example:
```
English,This is a sample English sentence.
Polish,To jest przykładowe polskie zdanie.
```

Make sure the file is properly formatted, as it is required for training and running the application.

## How to Run
1. Compile the project:
   ```sh
   javac src/*.java
   ```
2. Run the main class:
   ```sh
   java -cp src Main
   ```

## Customization
- To use a different activation function, modify the `activationFunction` in `MultiPerceptron.java`.
- Adjust learning rate (`alfa`) and error threshold (`Emax`) in `Main.java` or via the console menu.

