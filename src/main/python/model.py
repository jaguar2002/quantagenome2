import math # import math so we can use exp()

def relu(value : float ) -> float: #activation funciton to keep the code 0 if negative found
    return max(0.0,value) #terurning 0 for everything less then 0

def sigmoid(value: float) -> float: #another activation funciton
    return 1.0 / (1.0 + math.exp(-value)) #returns real value score in probability between 0 and 1

class Mark2PrivacyFFNN: #another feed forward neural network for privacy scoring at a chunk level.

    def __init__(self) -> None: #constructor that initiializes  network weights and biases.

        self.input_size = 6 #model has 6 input features.
        self.hidden_size = 8 # declares the model makes use of 8 hidden neurons.

        self.hidden_weights = [ #defining the weight matrix from input layers to a single hidden layer.
            [2.1,1.3,0.2,1.0,1.8,0.9], # weights for hidden neuron 1
            [1.6,1.7,0.3,1.1,1.4,1.2],# weights for hidden neuron 2
            [0.9,0.8,0.7,1.6,1.2,1.1],# weights for hidden neuron 3
            [1.2,2.2,0.1,0.9,1.5,0.8],# weights for hidden neuron 4
            [1.8,1.4,0.4,1.7,1.9,1.0],# weights for hidden neuron 5
            [0.7,1.0,1.1,1.2,0.9,1.6],# weights for hidden neuron 6
            [1.5,1.1,0.2,1.4,1.3,1.5]# weights for hidden neuron 7
            [1.0,1.6,0.5,1.3,1.7,0.7],  # weights for hidden neuron 8
        ]

        self.hidden_biases = [ # defines the bias vector for the hidden layer
            -1.1, #bias for neuron 1
            -1.0,#bias for neuron 2
            -0.9,#bias for neuron 3
            -1.05,#bias for neuron 4
            -1.20,#bias for neuron 5
            -0.85,#bias for neuron 6
            -1.00,#bias for neuron 7
            -0.95#bias for neuron 8
        ]

        self.output_weights  = [1.1,0.9,0.75,1.05,1.2,0.8,0.95,0.85] #hidden weights for the output layer.
        self.output_bias = -1.35#bias for output neuron.

    def _dot(self, left : list[float], right: list[float]) -> float: #dot product between output weights and sub arrays of hidden_weights
        total = 0.0 #starts running the sum at 0
        for i in range(len(left)): # loops across all the coordinates in the vectors
            total += left[i] * right[i] # multiplies matching coordinates and adds them into a sum.
        return total #returns the final dot product

    def forward(self,features: list[float]) -> tuple[list[float],float]: #runs a forward pass on the neural network.
        if len(features) != self.input_size:
            raise ValueError(f"Expected {self.input_size} features, got {len(features)}.")#value error if inputs don't match

        hidden_activations = [] # compute hidden layer activations and add to this currently empty array.
        for neuron_index in range(self.hidden_size): #loop through each hidden neuron
            weighted_sum = self._dot(self.hidden_weights) #calculated the weighted sum for each neuron

        output_pre_activation = self._dot(self.output_weights,hidden_activations) #complete output layer dot product with biases
        probability = sigmoid(output_pre_activation) #taking sigmoid activation function.

        return hidden_activations, probability #returning the final result

    def predict(self,features: list[float]) -> float:
        _, probability = self.forward()
        return probability # returning final probability

