import BaseClass from "../util/baseClass";
import axios from 'axios';

/**
 * Client to call the Review Service.
 */
export default class ReviewClient extends BaseClass {
    constructor(props = {}) {
        super();
        const methodsToBind = ['clientLoaded', 'addReview', 'getReview'];
        this.bindClassMethods(methodsToBind, this);
        this.props = props;
        this.clientLoaded(axios);
    }

    /**
     * Run any functions that are supposed to be called once the client has loaded successfully.
     * @param client The client that has been successfully loaded.
     */
    clientLoaded(client) {
        this.client = client;
        if (this.props.hasOwnProperty("onReady")) {
            this.props.onReady();
        }
    }

    /**
     * Adds a review for a pet.
     * @param reviewRequest Review request object.
     * @returns The review response.
     */
    async addReview(reviewRequest) {
        try {
            const response = await this.client.post("http://localhost:5001/pets/review/add", reviewRequest);
            return response.data;
        } catch (error) {
            this.handleError("addReview", error);
        }
    }

    /**
     * Retrieves reviews for a pet by its ID.
     * @param id The ID of the pet.
     * @returns The list of review responses.
     */
    async getReview(id) {
        try {
            const response = await this.client.get(`http://localhost:5001/pets/review/get/${id}`);
            return response.data;
        } catch (error) {
            this.handleError("getReview", error);
        }
    }

    /**
     * Helper method to log the error and run any error functions.
     * @param method The name of the method that encountered an error.
     * @param error The error received from the server.
     */
    handleError(method, error) {
        console.error(method + " failed - " + error);
        if (error.response.data.message !== undefined) {
            console.error(error.response.data.message);
        }
    }
}
