import BaseClass from "../util/baseClass";
import UserClient from "../api/userClient";
import PetDashboardClient from "../api/petDashboardClient";
import {v4 as uuidv4} from 'uuid';

class UserPage extends BaseClass {
    /**
     * Creates an instance of UserPage.
     */
    constructor() {
        super();
        this.bindClassMethods(['onRegister', 'onLogin', 'onLogout'], this);
    }

    /**
     * Mounts the UserPage component.
     * Sets up event listeners and initializes client instances.
     * @returns {Promise<void>}
     */
    async mount() {
        const loginForm = document.getElementById('login-form');
        if (loginForm) {
            loginForm.addEventListener('submit', this.onLogin);
        }

        const registerForm = document.getElementById('register-form');
        if (registerForm) {
            registerForm.addEventListener('submit', this.onRegister);
        }

        const logoutButton = document.getElementById('logout-button');
        if (logoutButton) {
            logoutButton.addEventListener('click', this.onLogout);
        }

        const exampleEmail = document.getElementById('example-email');
        if (exampleEmail) {
            exampleEmail.addEventListener('click', () => {
                this.copyToClipboard(exampleEmail.textContent);
                this.showAlert('Example email copied to clipboard');
            });
        }

        const examplePassword = document.getElementById('example-password');
        if (examplePassword) {
            examplePassword.addEventListener('click', () => {
                this.copyToClipboard(examplePassword.textContent);
                this.showAlert('Example password copied to clipboard');
            });
        }

        const exampleName = document.getElementById('example-name');
        if (exampleName) {
            exampleName.addEventListener('click', () => {
                this.copyToClipboard(exampleName.textContent);
                this.showAlert('Example name copied to clipboard');
            });
        }

        this.client = new UserClient();
        this.petDashboardClient = new PetDashboardClient();
    }
    copyToClipboard(text) {
        const input = document.createElement('input');
        input.value = text;
        document.body.appendChild(input);
        input.select();
        document.execCommand('copy');
        document.body.removeChild(input);
    }
    showAlert(message) {
        const alertContainer = document.getElementById('alert-container');
        if (alertContainer) {
            alertContainer.textContent = message;
            alertContainer.classList.add('show');
            setTimeout(() => {
                alertContainer.classList.remove('show');
            }, 3000); // Hide the alert after 3 seconds (adjust the delay as needed)
        }
    }


    /**
     * Event handler for the "Register" button click event.
     * Registers a new user and redirects to the login page.
     * @param {Event} event - The click event.
     */
    async onRegister(event) {
        event.preventDefault();
        const createRequest = {
            name: document.getElementById("register-name-field").value,
            email: document.getElementById("register-email-field").value,
            password: document.getElementById("register-password-field").value,
        };

        const userResponse = await this.client.registerUser(createRequest);
        if (userResponse) {
            console.log("User registered:", userResponse);
            // Store the name in local storage
            localStorage.setItem('name', createRequest.name);
            // Redirect to login page
            window.location.href = "login.html";
        } else {
            console.error("Error registering user!");
        }
    }


    /**
     * Sets the email and petId in local storage.
     * @param {string} email - The user's email.
     * @param {string} petId - The pet's ID.
     */
    async setItem(email, petId) {
        console.log(email, petId);
        localStorage.setItem('email', email);
        localStorage.setItem('petId', petId);
        localStorage.setItem('adopt-Pet-Id', petId); // Set adoptedPetId to the petId
    }

    /**
     * Event handler for the "Login" button click event.
     * Logs in the user, sets the necessary local storage items, and redirects to the adoption page.
     * @param {Event} event - The click event.
     */
    async onLogin(event) {
        event.preventDefault();
        const email = document.getElementById("login-email-field").value;
        const password = document.getElementById("login-password-field").value;

        const userResponse = await this.client.loginUser(email, password);
        let petId = localStorage.getItem('petId');

        if (!petId) {
            petId = uuidv4(); // Generate a new petId if it doesn't exist in local storage
            localStorage.setItem('petId', petId);
        }

        await this.setItem(email, petId); // Pass email and petId

        if (userResponse) {
            console.log("User logged in:", userResponse);

            // Retrieve pet stats using petId
            const petId = userResponse.petId;
            const storedPetStats = localStorage.getItem("petStats");

            let petStats = {};

            if (storedPetStats) {
                // Parse the stored pet stats from local storage
                petStats = JSON.parse(storedPetStats);

                // Check if the petId in local storage matches the petId from the login response
                if (petStats.petId !== petId) {
                    // If the petIds don't match, set the pet stats to blank values
                    petStats = {
                        ...petStats,
                        healthLevel: '',
                        happinessLevel: '',
                        cleanlinessLevel: '',
                        obedienceLevel: '',
                        energyLevel: '',
                        hungerLevel: '',
                        overallStatus: '',
                        petId: petId, // Update the petId in case it has changed
                    };
                }
            } else {
                // If no pet stats are stored, create a new object with values
                petStats = {
                    healthLevel: Math.floor(Math.random() * 101),
                    happinessLevel: Math.floor(Math.random() * 101),
                    cleanlinessLevel: Math.floor(Math.random() * 101),
                    obedienceLevel: Math.floor(Math.random() * 101),
                    energyLevel: Math.floor(Math.random() * 101),
                    hungerLevel: Math.floor(Math.random() * 101),
                    overallStatus: Math.floor(Math.random() * 101),
                    petId: petId, // Set the petId from the login response
                };
            }

            console.log("Pet stats:", petStats);

            // Redirect to adoption page
            window.location.href = "adoption.html";
        } else {
            console.error("Error logging in!");
        }
    }

    /**
     * Event handler for the "Logout" button click event.
     * Logs out the user and redirects to the login page.
     */
    async onLogout() {
        console.log("Logging out...");

        const email = localStorage.getItem("email"); // Get the email from local storage

        if (email) {
            // Remove local storage items
            localStorage.removeItem("email");

            const logoutResponse = await this.client.logoutUser(email);
            if (logoutResponse) {
                console.log("User logged out");
                // Redirect to login page
                window.location.href = "login.html";
            } else {
                console.error("Error logging out!");
            }
        } else {
            console.error("Email not found in local storage");
        }
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const userPage = new UserPage();
    await userPage.mount();
};

main().catch(error => console.error(error));
