import BaseClass from "../util/baseClass";
import axios from 'axios';

/**
 * Client to call the User Service.
 */
export default class UserClient extends BaseClass {
    constructor(props = {}) {
        super();
        const methodsToBind = ['clientLoaded', 'registerUser', 'loginUser','getLoggedInEmail'];
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
     * Registers a new user.
     * @param createRequest User create a request object.
     * @returns The user response.
     */
    async registerUser(createRequest) {
        try {
            const response = await this.client.post("http://localhost:5001/users/register", createRequest);
            return response.data;
        } catch (error) {
            this.handleError("registerUser", error);
        }
    }

    /**
     * Logs in a user.
     * @param email User's email.
     * @param password User's password.
     * @returns The user response.
     */
    async loginUser(email, password) {

        try {
            const response = await this.client.post("http://localhost:5001/users/login", null, {
                params: {
                    email: email,
                    password: password,
                    }
            });
            return response.data;
        } catch (error) {
            this.handleError("loginUser", error);
        }
    }


    async getLoggedInEmail() {
        try {
            const response = await this.client.get("http://localhost:5001/users/logged-in-user/${email}");
            return response.data.email;
        } catch (error) {
            this.handleError("getLoggedInEmail", error);
        }
    }

    /**
     * Logs out the current user.
     * @returns A boolean indicating whether the logout was successful.
     */
    async logoutUser(email) {
        try {
            const url = `http://localhost:5001/users/logout/${email}`;
            const response = await this.client.delete(url);
            return response.data;
        } catch (error) {
            this.handleError("logoutUser", error);
            throw error;
        }
    }





    /**
     * Helper method to log the error and run any error functions.
     * @param method
     * @param error The error received from the server.
     */
    handleError(method, error) {
        console.error(method + " failed - " + error);
        if (error.response.data.message !== undefined) {
            console.error(error.response.data.message);
        }
    }

    updateUser(userRecord) {
        this.client.post("http://localhost:5001/users/update", userRecord);
    }
}
