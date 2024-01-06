import BaseClass from "../util/baseClass";
import axios from 'axios';

class PetClient extends BaseClass {
    constructor(props = {}) {
        super();
        const methodsToBind = ['clientLoaded', 'getPetById', 'adoptPet'];
        this.bindClassMethods(methodsToBind, this);
        this.props = props;
        this.clientLoaded(axios);
    }

    clientLoaded(client) {
        this.client = client;
        if (this.props.hasOwnProperty("onReady")) {
            this.props.onReady();
        }
    }

    async getPetById(id, errorCallback) {
        try {
            const response = await this.client.get(`/pets/${id}`);
            return response.data;
        } catch (error) {
            this.handleError('getPetById', error, errorCallback);
        }
    }

    async adoptPet(email, petId, errorCallback) {
        try {
            const response = await this.client.post('/pets/adopt', null, {
                params: {
                    email: email,
                    petId: petId,
                },
            });
            return response.data;
        } catch (error) {
            this.handleError('adoptPet', error, errorCallback);
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
}

export default PetClient;
