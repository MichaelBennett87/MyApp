import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import PetClient from "../api/petClient";

/**
 * Logic needed for the view pet page of the website.
 */
class PetPage extends BaseClass {
    /**
     * Creates an instance of PetPage.
     */
    constructor() {
        super();
        this.bindClassMethods(['onGet', 'onAdopt', 'renderPet'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Mounts the PetPage component.
     * Sets up event handlers and fetches pet information.
     * @returns {Promise<void>}
     */
    async mount() {
        document.getElementById('adopt-form').addEventListener('submit', this.onAdopt);
        this.client = new PetClient();

        // Set the value of the adopt-pet-id input field
        document.getElementById('adopt-pet-id').value = localStorage.getItem('petId');

        this.dataStore.addChangeListener(this.renderPet);
    }

    /**
     * Renders the pet information on the page.
     */
    async renderPet() {
        let resultArea = document.getElementById("result-info");

        const pet = this.dataStore.get("pet");

        if (pet) {
            resultArea.innerHTML = `
          <div>ID: ${pet.id}</div>
      `;
        } else {
            resultArea.innerHTML = "No Pet";
        }
    }

    /**
     * Event handler for the "Get Pet" button click event.
     * Fetches the pet information from the server.
     * @param {Event} event - The click event.
     */
    async onGet(event) {
        event.preventDefault();

        let id = document.getElementById("adopt-pet-id").value;
        this.dataStore.set("pet", null);

        let result = await this.client.getPetById(id, this.errorHandler);
        this.dataStore.set("pet", result);
        if (result) {
            this.showMessage(`Got ${result.name}!`);
        } else {
            this.errorHandler("Error fetching pet! Please try again.");
        }
    }

    /**
     * Event handler for the "Adopt" button click event.
     * Initiates the pet adoption process.
     * @param {Event} event - The click event.
     */
    async onAdopt(event) {
        event.preventDefault();

        const petId = localStorage.getItem('petId'); // Retrieve petId from local storage

        await this.client.adoptPet(petId);
        localStorage.setItem('adopt-Pet-Id', petId); // Set adoptedPetId to petId
        this.showMessage('Pet adoption successful!');

        // Set pet stats to realistic values
        const stats = {
            hungerLevel: 0,
            happinessLevel: 100,
            healthLevel: 100,
            cleanlinessLevel: 100,
            energyLevel: 100,
            obedienceLevel: 100,
            overallStatus: 100,
        };

        // Store pet stats in local storage
        localStorage.setItem('petStats', JSON.stringify(stats));

        await this.client.setPetStats(petId, stats);
        console.log('Pet stats updated!', stats);

        // Redirect to petDashboard.html
        window.location.href = 'petDashboard.html';
    }
}

/**
 * The main function to run when the page contents have loaded.
 * @returns {Promise<void>}
 */
const main = async () => {
    const petPage = new PetPage();
    await petPage.mount();
};

window.addEventListener('DOMContentLoaded', main);
