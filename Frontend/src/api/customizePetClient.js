import axios from 'axios';
import BaseClass from '../util/baseClass';
import UserClient from './userClient';
import PetClient from './petClient';



class customizePetClient extends BaseClass {
    constructor(props = {}) {
        super();
        const methodsToBind = ['clientLoaded', 'getPetById', 'customizePet', 'getCustomizedPetById'];
        this.bindClassMethods(methodsToBind, this);
        this.props = props;
        this.clientLoaded(axios);
        this.petClient = new PetClient(); // Create an instance of the PetClient class
        this.addFormListeners();
    }

    clientLoaded(client) {
        this.client = client;
        if (this.props.hasOwnProperty('onReady')) {
            this.props.onReady();
        }
    }


    async getPetById(id, errorCallback) {
        try {
            const response = await this.client.get(`http://localhost:5001/customize/get/${id}`);
            return response.data;
        } catch (error) {
            this.handleError('getPetById', error, errorCallback);
        }
    }


    async customizePet() {
        try {
            const petId = localStorage.getItem('petId');
            const attire = document.querySelector("#attire").value.split(','); // splits the string into an array
            const email = localStorage.getItem('email');
            const password = localStorage.getItem('password');
            console.log(email, password)
            const response = await this.client.post(`http://localhost:5001/customize/create/${petId}`, {
                email: email,
                attire: attire
            });
            console.log('Pet customization successful!');
            return response;
        } catch (error) {
            this.handleError('customizePet', error);
        }
}


    async getCustomizedPetById(petId, errorCallback) {
        try {
            const response = await this.client.get(`http://localhost:5001/customizedPets/get/${petId}`);
            return response.data;
        } catch (error) {
            this.handleError('getCustomizedPetById', error, errorCallback);
        }
    }

    handleError(method, error, errorCallback) {
        console.error(`${method} failed - ${error}`);
        if (error.response && error.response.data && error.response.data.message !== undefined) {
            console.error(error.response.data.message);
        }
        if (errorCallback) {
            errorCallback(`${method} failed - ${error}`);
        }
    }

    addFormListeners() {
        const form = document.querySelector("#adopt-form2");
        form.addEventListener("submit", async (e) => {
            e.preventDefault();

            try {
                await this.customizePet();
                console.log("Pet adoption successful!");
            } catch (err) {
                console.error(err);
            }
        });

        const resetButton = document.querySelector("#reset-btn");
        resetButton.addEventListener("click", () => {
            // Add your reset logic here.
            console.log("Reset clicked!");
        });
    }
}

export default customizePetClient;