import axios from 'axios';
import BaseClass from '../util/baseClass';
import UserClient from './userClient'; // Update the import statement to use consistent casing



class PetClient extends BaseClass {
    constructor(props = {}) {
        super();
        const methodsToBind = ['clientLoaded', 'getPetById', 'adoptPet'];
        this.bindClassMethods(methodsToBind, this);
        this.props = props;
        this.clientLoaded(axios);
        this.userClient = new UserClient(); // Create an instance of the UserClient class
    }

    clientLoaded(client) {
        this.client = client;
        if (this.props.hasOwnProperty('onReady')) {
            this.props.onReady();
        }
    }

    async getPetById(id, errorCallback) {
        try {
            const response = await this.client.get(`http://localhost:5001/pets/get/${id}`);
            return response.data;
        } catch (error) {
            this.handleError('getPetById', error, errorCallback);
        }
    }


    async adoptPet(petId) {

        try {
            const email = localStorage.getItem('email');
            const password = localStorage.getItem('password');

            console.log(email, password)
            const response = await this.client.post(`http://localhost:5001/pets/adopt/${petId}`, {
                email: email,
                password: password
            });
            console.log('Pet adoption successful!');
        } catch (error) {
            console.error('Pet adoption failed:', error);
        }
    }

    async setPetStats(petId, stats, errorCallback) {
        try {
            const response = await this.client.post(`http://localhost:5001/pets/setStats/${petId}`, stats);
            return response.data;
        } catch (error) {
            this.handleError('setPetStats', error, errorCallback);
        }
    }

    async getPetStats(petId) {
        try {
            const response = await this.client.get(`http://localhost:5001/pets/getStats/${petId}`);
            return response.data;
        } catch (error) {
            this.handleError('getPetStats', error);
            return null;
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
}

export default PetClient;