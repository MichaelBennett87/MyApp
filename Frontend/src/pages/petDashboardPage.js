import BaseClass from '../util/baseClass';
import PetDashboardClient from '../api/petDashboardClient';
/**
 * Represents the pet dashboard page.
 */
class PetDashboardPage extends BaseClass {
    /**
     * Creates an instance of PetDashboardPage.
     */
    constructor() {
        super();
        this.petDashboardClient = new PetDashboardClient();

        // Bind event handlers
        this.onFeed = this.onFeed.bind(this);
        this.onPlay = this.onPlay.bind(this);
        this.onGroom = this.onGroom.bind(this);
        this.onChat = this.onChat.bind(this);
        this.deletePet = this.deletePet.bind(this);
    }
    /**
     * Mounts the pet dashboard page by attaching event listeners and displaying pet stats.
     */
    async mount() {
        document.getElementById('feed-btn').addEventListener('click', this.onFeed);
        document.getElementById('play-btn').addEventListener('click', this.onPlay);
        document.getElementById('groom-btn').addEventListener('click', this.onGroom);
        document.getElementById('delete-btn').addEventListener('click', this.deletePet);

        await this.displayPetStats();
    }

    /**
     * Displays the pet statistics on the frontend.
     */
    async displayPetStats() {
        const petId = localStorage.getItem('adopt-Pet-Id');
        console.log('Pet ID:', petId);

        try {
            let petStats = {};

            // Retrieve pet stats from local storage
            const storedPetStats = localStorage.getItem('petStats');

            if (storedPetStats) {
                // Parse the stored pet stats from local storage
                petStats = JSON.parse(storedPetStats);
            }

            // Check if the stored pet stats have the same petId as the current logged-in petId
            if (petStats.petId === petId) {
                console.log('Pet Stats:', petStats);

                const { healthLevel, happinessLevel, cleanlinessLevel, obedienceLevel, energyLevel, hungerLevel, overallStatus } = petStats;

                console.log('Health Level:', healthLevel);
                console.log('Happiness Level:', happinessLevel);
                console.log('Cleanliness Level:', cleanlinessLevel);
                console.log('Obedience Level:', obedienceLevel);
                console.log('Energy Level:', energyLevel);
                console.log('Hunger Level:', hungerLevel);
                console.log('Overall Status:', overallStatus);

                // Display the pet stats on the frontend
                document.getElementById('health-level').textContent = healthLevel;
                document.getElementById('happiness-level').textContent = happinessLevel;
                document.getElementById('cleanliness-level').textContent = cleanlinessLevel;
                document.getElementById('obedience-level').textContent = obedienceLevel;
                document.getElementById('energy-level').textContent = energyLevel;
                document.getElementById('hunger-level').textContent = hungerLevel;
                document.getElementById('overall-status').textContent = overallStatus;
            } else {
                // If pet stats were not found in local storage or the petId does not match,
                // proceed to make an API call to fetch the pet stats
                const petResponse = await this.petDashboardClient.getPetStats(petId);
                console.log('Pet Response:', petResponse);

                const { healthLevel, happinessLevel, cleanlinessLevel, obedienceLevel, energyLevel, hungerLevel, overallStatus } = petResponse;

                console.log('Health Level:', healthLevel);
                console.log('Happiness Level:', happinessLevel);
                console.log('Cleanliness Level:', cleanlinessLevel);
                console.log('Obedience Level:', obedienceLevel);
                console.log('Energy Level:', energyLevel);
                console.log('Hunger Level:', hungerLevel);
                console.log('Overall Status:', overallStatus);

                // Update the pet stats in local storage
                petStats = {
                    healthLevel,
                    happinessLevel,
                    cleanlinessLevel,
                    obedienceLevel,
                    energyLevel,
                    hungerLevel,
                    overallStatus,
                    petId,
                };

                localStorage.setItem('petStats', JSON.stringify(petStats));

                // Display the pet stats on the frontend
                document.getElementById('health-level').textContent = healthLevel;
                document.getElementById('happiness-level').textContent = happinessLevel;
                document.getElementById('cleanliness-level').textContent = cleanlinessLevel;
                document.getElementById('obedience-level').textContent = obedienceLevel;
                document.getElementById('energy-level').textContent = energyLevel;
                document.getElementById('hunger-level').textContent = hungerLevel;
                document.getElementById('overall-status').textContent = overallStatus;
            }
        } catch (error) {
            console.error('Failed to get pet stats:', error);
            this.showStatusMessage('Failed to get pet stats', 'error');
        }
    }

    /**
     * Handles the feed action for the pet.
     * @param {Event} event - The click event object.
     */
    async onFeed(event) {
        event.preventDefault();
        const petId = localStorage.getItem('adopt-Pet-Id');

        // Create an interactionRequest object with the necessary data
        const interactionRequest = {
            petId: petId,
            action: 'feed',
            interactionType: 'pet',
            interactionSubType: 'feed'
        };

        try {
            const petResponse = await this.petDashboardClient.feedPet(petId, interactionRequest);

            this.showStatusMessage('Pet fed successfully', 'success');
            // Update the displayed stats after feeding
            await this.displayPetStats();
        } catch (error) {
            console.error('Failed to feed pet:', error);
            this.showStatusMessage('Failed to feed pet', 'error');
        }
    }

    /**
     * Handles the play action for the pet.
     * @param {Event} event - The click event object.
     */
    async onPlay(event) {
        event.preventDefault();
        const petId = localStorage.getItem('adopt-Pet-Id');

        // Create an interactionRequest object with the necessary data
        const interactionRequest = {
            petId: petId,
            action: 'play',
            interactionType: 'pet',
            interactionSubType: 'play'
        };

        try {
            const petResponse = await this.petDashboardClient.playWithPet(petId, interactionRequest);
            this.showStatusMessage('Played with pet successfully', 'success');
            // Update the displayed stats after playing
            await this.displayPetStats();
        } catch (error) {
            console.error('Failed to play with pet:', error);
            this.showStatusMessage('Failed to play with pet', 'error');
        }
    }

    /**
     * Handles the groom action for the pet.
     * @param {Event} event - The click event object.
     */
    async onGroom(event) {
        event.preventDefault();
        const petId = localStorage.getItem('adopt-Pet-Id');

        // Create an interactionRequest object with the necessary data
        const interactionRequest = {
            petId: petId,
            action: 'groom',
            interactionType: 'pet',
            interactionSubType: 'groom'
        };

        try {
            const petResponse = await this.petDashboardClient.groomPet(petId, interactionRequest);
            this.showStatusMessage('Pet groomed successfully', 'success');
            // Update the displayed stats after grooming
            await this.displayPetStats();
        } catch (error) {
            console.error('Failed to groom pet:', error);
            this.showStatusMessage('Failed to groom pet', 'error');
        }
    }

    /**
     * Handles the chat action for the pet.
     * @param {Event} event - The click event object.
     */
    async onChat(event) {
        event.preventDefault();
        const petId = localStorage.getItem('petId');
        const message = document.getElementById('chat-message').value;
        try {
            const petResponse = await this.petDashboardClient.chatWithPet(petId, message);
            console.log(petResponse);
            this.showStatusMessage('Chat successful', 'success');
            // Update the displayed stats after chatting
            await this.displayPetStats();
        } catch (error) {
            console.error('Failed to chat with pet:', error);
            this.showStatusMessage('Failed to chat with pet', 'error');
        }
    }

    /**
     * Deletes the pet and performs necessary cleanup.
     */
    async deletePet() {
        const confirmed = confirm('Are you sure you want to delete your pet?');
        if (!confirmed) {
            return;
        }

        try {
            const petId = localStorage.getItem('adopt-Pet-Id');
            const email = localStorage.getItem('email');

            if (!petId || !email) {
                throw new Error('Missing petId or email in localStorage.');
            }

            const petDashboardClient = new PetDashboardClient(email);

            // Delete the pet
            const petResponse = await petDashboardClient.deletePet(petId);
            console.log('Pet response:', petResponse);

            // Update the displayed stats after deleting
            await this.displayPetStats();

            // Clear pet-related data from localStorage

            localStorage.removeItem('');

            console.log('Pet data removed from localStorage.');

            this.showStatusMessage('Pet deleted successfully','success');

            // Redirect to a different page or perform any other actions,
            // For example, redirect to the adoption page
            window.location.href = 'adoption.html';


            // Clear pet-related data from localStorage

            localStorage.removeItem('healthLevel');
            localStorage.removeItem('happinessLevel');
            localStorage.removeItem('cleanlinessLevel');
            localStorage.removeItem('obedienceLevel');
            localStorage.removeItem('energyLevel');
            localStorage.removeItem('hungerLevel');
            localStorage.removeItem('overallStatus');
            localStorage.removeItem('reviewId');
            localStorage.removeItem('petStats');


            console.log('Pet data removed from localStorage.');

            this.showStatusMessage('Pet deleted successfully', 'success');

            // Redirect to a different page or perform any other actions,
            // For example, redirect to the adoption page
            window.location.href = 'adoption.html';
        } catch (error) {
            console.error('Failed to delete pet:', error);
            this.showStatusMessage('Failed to delete pet', 'error');
        }
    }

    /**
     * Shows a status message in the UI.
     * @param {string} message - The message to display.
     * @param {string} type - The type of the message ('success', 'error', etc.).
     */
    showStatusMessage(message, type) {
        const statusBar = document.getElementById('status-bar');
        statusBar.innerHTML = `<i class="fas fa-info-circle"></i> ${message}`;
        statusBar.classList.add(type);

        setTimeout(() => {
            statusBar.innerHTML = '';
            statusBar.classList.remove(type);
        }, 5000);
    }
}

window.addEventListener('DOMContentLoaded', () => {
    const petDashboardPage = new PetDashboardPage();
    petDashboardPage.mount().then(() => {
        console.log('Pet dashboard mounted');
    });
});
