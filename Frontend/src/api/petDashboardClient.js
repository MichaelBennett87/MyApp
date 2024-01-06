import axios from 'axios';
import BaseClass from '../util/baseClass';

class PetDashboardClient extends BaseClass {
    constructor(props = {}) {
        super();
        const methodsToBind = ['clientLoaded', 'feedPet', 'playWithPet', 'groomPet', 'chatWithPet','deletePet',];
        this.bindClassMethods(methodsToBind, this);
        this.props = props;
        this.clientLoaded(axios);
    }
    clientLoaded(client) {
        this.client = client;
        if (this.props.hasOwnProperty('onReady')) {
            this.props.onReady();
        }
    }
    async feedPet(petId, interactionRequest, errorCallback) {
        try {
            console.log('Feeding pet with ID:', petId);
            const response = await this.client.post(`http://localhost:5001/pets/feed/${petId}`, interactionRequest, {
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            console.log('Feed response:', response);
            return response.data;
        } catch (error) {
            console.error('Error feeding pet:', error);
            this.handleError('feedPet', error, errorCallback);
            console.log('Error details:', error);
        }
    }






    async playWithPet(petId, interactionRequest, errorCallback) {
        try {
            console.log('Playing with pet with ID:', petId);
            const response = await this.client.post(`http://localhost:5001/pets/play/${petId}`, interactionRequest, {
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            console.log('Play response:', response);
            return response.data;
        } catch (error) {
            console.error('Error playing with pet:', error);
            this.handleError('playWithPet', error, errorCallback);
        }
    }

    async groomPet(petId, interactionRequest, errorCallback) {
        try {
            console.log('Grooming pet with ID:', petId);
            const response = await this.client.post(`http://localhost:5001/pets/groom/${petId}`, interactionRequest, {
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            console.log('Groom response:', response);
            return response.data;
        } catch (error) {
            console.error('Error grooming pet:', error);
            this.handleError('groomPet', error, errorCallback);
        }
    }



    handleError(method, error, errorCallback) {
        console.error(`${method} failed - ${error}`);
        if (error.response.data.message !== undefined) {
            console.error(error.response.data.message);
        }
        if (errorCallback) {
            errorCallback(`${method} failed - ${error}`);
        }
    }



    async chatWithPet(petId, message) {
        try {
            const response = await this.client.post(`http://localhost:5001/pets/chat/${petId}`, { message });
            return response.data;
        } catch (error) {
            this.handleError('chatWithPet', error);
        }
    }


    async getPetStats() {
        try {
            const petId = localStorage.getItem("petId");

            const response = await this.client.get(`http://localhost:5001/pets/getStats/${petId}`);
            const { healthLevel, happinessLevel, cleanlinessLevel, obedienceLevel, energyLevel, hungerLevel, overallStatus } = response.data;

            // Store the pet stats in the localStorage
            localStorage.setItem("healthLevel", healthLevel);
            localStorage.setItem("happinessLevel", happinessLevel);
            localStorage.setItem("cleanlinessLevel", cleanlinessLevel);
            localStorage.setItem("obedienceLevel", obedienceLevel);
            localStorage.setItem("energyLevel", energyLevel);
            localStorage.setItem("hungerLevel", hungerLevel);
            localStorage.setItem("overallStatus", overallStatus);

            return {
                healthLevel,
                happinessLevel,
                cleanlinessLevel,
                obedienceLevel,
                energyLevel,
                hungerLevel,
                overallStatus,
            };
        } catch (error) {
            this.handleError("getPetStats", error);
            // Add any necessary error handling logic
            return {
                healthLevel: undefined,
                happinessLevel: undefined,
                cleanlinessLevel: undefined,
                obedienceLevel: undefined,
                energyLevel: undefined,
                hungerLevel: undefined,
                overallStatus: undefined,
            };
        }
    }



    async deletePet(petId, email, errorCallback) {
        try {
            console.log('Deleting pet from the backend...');
            const response = await this.client.delete(`http://localhost:5001/pets/delete/${petId}`, {
                params: { email: email }
            });
            console.log('Pet deleted successfully from the backend.');
            return response.data;
        } catch (error) {
            this.handleError('deletePet', error, errorCallback);
            console.error('Error deleting pet:', error);
        }
    }
}

export default PetDashboardClient;

