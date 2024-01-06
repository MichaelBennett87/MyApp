// Chat button click event listener
document.getElementById("chat-bubble").addEventListener("click", function() {
    document.getElementById("messenger").style.display = "block";
});

// Send button click event listener
document.getElementById("send-btn").addEventListener("click", function() {
    sendMessage();
});

// Enter key press event listener on message input
document.getElementById("message-input").addEventListener("keypress", function(e) {
    if (e.key === "Enter") {
        sendMessage();
    }
});


// Function to send a message
function sendMessage() {
    const messageInput = document.getElementById("message-input");
    const message = messageInput.value.trim().toLowerCase(); // Convert the message to lowercase for easier comparison

    if (message !== "") {
        const chatMessages = document.getElementById("chat-messages");

        // Create user message container
        const userMessageContainer = document.createElement("div");
        userMessageContainer.classList.add("message-container");
        userMessageContainer.classList.add("user"); // Add "user" class

        const userMessage = document.createElement("div");
        userMessage.classList.add("message");
        userMessage.textContent = message;

        userMessageContainer.appendChild(userMessage);

        // Append user message to chat
        chatMessages.appendChild(userMessageContainer);

        // Create chatbot message container
        const chatbotMessageContainer = document.createElement("div");
        chatbotMessageContainer.classList.add("message-container");
        chatbotMessageContainer.classList.add("chatbot"); // Add "chatbot" class

        const chatbotMessage = document.createElement("div");
        chatbotMessage.classList.add("message");
        chatbotMessage.textContent = "Desarapen:";

        chatbotMessageContainer.appendChild(chatbotMessage);

        // Append a chatbot message to chat
        chatMessages.appendChild(chatbotMessageContainer);

        // Check if the message is a question about the pet's status
        if (message.includes("hungry")) {
            // Check hunger level and respond
            const hungerLevel = localStorage.getItem("hungerLevel");
            const responseMessage = createResponseMessage("Yes, I'm feeling " + hungerLevel + "% hungry. Can I have a snack?");
            chatMessages.appendChild(responseMessage);
        } else if (message.includes("do you feel ok")) {
            // Check health level and respond
            const healthLevel = localStorage.getItem("healthLevel");
            const responseMessage = createResponseMessage("My health level is " + healthLevel + "%. I'm feeling great!");
            chatMessages.appendChild(responseMessage);
        } else if (message.includes("happy")) {
            // Check happiness level and respond
            const happinessLevel = localStorage.getItem("happinessLevel");
            const responseMessage = createResponseMessage("I'm " + happinessLevel + "% happy! Want to play?");
            chatMessages.appendChild(responseMessage);
        } else if (message.includes("clean")) {
            // Check cleanliness level and respond
            const cleanlinessLevel = localStorage.getItem("cleanlinessLevel");
            const responseMessage = createResponseMessage("I'm " + cleanlinessLevel + "% clean. Do you like my shiny fur?");
            chatMessages.appendChild(responseMessage);
        } else if (message.includes("obedience")) {
            // Check obedience level and respond
            const obedienceLevel = localStorage.getItem("obedienceLevel");
            const responseMessage = createResponseMessage("My obedience level is " + obedienceLevel + "%. I'm a good listener!");
            chatMessages.appendChild(responseMessage);
        } else if (message.includes("energy")) {
            // Check energy level and respond
            const energyLevel = localStorage.getItem("energyLevel");
            const responseMessage = createResponseMessage("I have " + energyLevel + "% energy. Let's have some fun!");
            chatMessages.appendChild(responseMessage);
        } else if (message.includes("math")) {
            // Generate a math problem and ask
            const mathProblem = generateMathProblem();
            const responseMessage = createResponseMessage("Here's a math problem for you: " + mathProblem);
            chatMessages.appendChild(responseMessage);
        } else if (message.includes("snack")) {
            // Trigger feeding event
            const feedButton = document.getElementById("feed-btn");
            feedButton.click(); // Trigger the "Feed" button click event

            // Add a response message after feeding the pet
            const snackResponseMessage = createResponseMessage("Yummy! Thanks for the snack!");
            chatMessages.appendChild(snackResponseMessage);
        } else if (isMathAnswer(message)) {
            // Check if the message is a math answer and respond
            const correctMessage = createResponseMessage("Congratulations! That's the correct answer!");
            chatMessages.appendChild(correctMessage);
        } else if (message.includes("hello") || message.includes("hi")) {
            // Respond to greetings
            const responseMessage = createResponseMessage("Hello! How can I help you today?");
            chatMessages.appendChild(responseMessage);
        } else if (message.includes("bye") || message.includes("goodbye")) {
            // Respond to goodbye
            const responseMessage = createResponseMessage("Goodbye! Have a great day!");
            chatMessages.appendChild(responseMessage);
        } else {
            // Respond to unknown messages
            const responseMessage = createResponseMessage("Sorry, I don't understand. Can you please ask another question?");
            chatMessages.appendChild(responseMessage);
        }


        // Clear input
        messageInput.value = "";
        messageInput.focus();

        // Scroll to the bottom of the chat
        chatMessages.scrollTop = chatMessages.scrollHeight;
    }
}




// Function to create a response message
// Function to create a response message with typewriter effect
function createResponseMessage(message) {
    const responseContainer = document.createElement("div");
    responseContainer.classList.add("response-container");

    const responseMessage = document.createElement("div");
    responseMessage.classList.add("message");
    responseMessage.classList.add("response");

    const responseEmoji = document.createElement("div");
    responseEmoji.classList.add("emoji");
    responseEmoji.textContent = getEmoji();

    responseContainer.appendChild(responseMessage);
    responseContainer.appendChild(responseEmoji);

    // Add typewriter effect to the message
    let index = 0;
    const typingSpeed = 50; // Adjust typing speed (milliseconds per character)

    function typeWriter() {
        if (index < message.length) {
            responseMessage.textContent += message.charAt(index);
            index++;
            setTimeout(typeWriter, typingSpeed);
        }
    }

    setTimeout(typeWriter, 1000); // Delay the typewriter effect by 1 second

    return responseContainer;
}


// Function to get a random emoji
function getEmoji() {
    const emojis = ["😄", "🐾", "🐶", "🐱", "🐰", "🐢", "🐠", "🌟", "🎉", "💕"];
    const randomIndex = Math.floor(Math.random() * emojis.length);
    return emojis[randomIndex];
}

// Function to generate a random math problem
// Function to generate a random math problem
function generateMathProblem() {
    const operators = ["+", "-", "*", "/"];
    const operator = operators[Math.floor(Math.random() * operators.length)];
    const number1 = Math.floor(Math.random() * 10) + 1; // Generate a random number between 1 and 10
    const number2 = Math.floor(Math.random() * 10) + 1;
    let problem;
    let answer;

    switch (operator) {
        case "+":
            problem = number1 + " " + operator + " " + number2;
            answer = number1 + number2;
            break;
        case "-":
            problem = number1 + " " + operator + " " + number2;
            answer = number1 - number2;
            break;
        case "*":
            problem = number1 + " " + operator + " " + number2;
            answer = number1 * number2;
            break;
        case "/":
            answer = number1;
            const product = number1 * number2;
            problem = product + " " + operator + " " + number2;
            break;
        default:
            problem = "";
            answer = "";
            break;
    }

    localStorage.setItem("mathAnswer", answer.toString()); // Store the correct answer in localStorage

    return problem;
}


// Function to check if the user's message is a math answer
function isMathAnswer(message) {
    const storedAnswer = localStorage.getItem("mathAnswer");
    const answer = parseFloat(storedAnswer);

    return parseFloat(message) === answer; // Compare the user's answer with the correct answer
}








