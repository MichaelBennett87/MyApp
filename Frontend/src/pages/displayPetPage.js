import PetDashboardClient from '../api/petDashboardClient';

/**
 * Displays the adopted pet on the page.
 * @param {string} petName - The name of the adopted pet.
 * @param {string} petId - The ID of the adopted pet.
 * @param {function(): string} name - The user's name.
 * @param {object} attributes - The pet attributes.
 * @param overallStatus
 * @param statusColorClass
 */
function displayAdoptedPet(petName, petId, name, attributes, overallStatus, statusColorClass) {
    const userEmail = name(); // Retrieve user's email from local storage
    const userName = extractNameFromEmail(userEmail); // Extract the name from the email
    const linkElement = document.createElement('link');
    linkElement.rel = 'stylesheet';
    linkElement.href = 'css/style2.css'; // Replace 'path/to/style2.css' with the actual path to the style2.css file
    document.head.appendChild(linkElement);


    const petGallery = document.getElementById("pet-gallery");
    // Update the pet gallery with the adopted pet
    petGallery.innerHTML = `
      <div class="monster-wrap">
         <div class="desarapen-monster">
        <div class="desarapen-monster-inner">
            <div class="desarapen-monster-horn">
                <div class="horn -left"></div>
                <div class="horn -right"></div>
            </div>

            <div class="desarapen-monster-body">
                <span></span>
            </div>
            <div class="desarapen-monster-feet"></div>
            <div class="desarapen-monster-hands">
                <div class="hands -left">
                    <span></span>
                </div>
                <div class="hands -right">
                    <span></span>
                </div>
            </div>
            <div class="desarapen-monster-eyes">
                <div class="eye -left">
                    <span></span>
                </div>
                <div class="eye -right">
                    <span></span>
                </div>
                <!--       <div class="eye -right"></div> -->
            </div>
            <div class="desarapen-monster-mouth">
                <span></span>
                <div class="tounge"></div>
                <div class="teeth">
                    <span></span>
                    <span></span>
                    <span></span>
                    <span></span>
                    <span></span>
                    <span></span>
                    <span></span>
                </div>
            </div>

            <div class="desarapen-monster-freckles"></div>
        </div>
    </div>
  <div class="adopted-pet-info">
            <h3>Congratulations, ${userName}! You've adopted ${petName}!</h3>
            <p>Pet ID: ${petId}</p>
   <div class="pet-stats">
    <h4>Pet Stats:</h4>
    <ul>
        <li><span><i class="fas fa-heart"></i> Health Level:</span> <span id="health-level">${attributes.healthLevel}</span></li>
        <li><span><i class="fas fa-smile"></i> Happiness Level:</span> <span id="happiness-level">${attributes.happinessLevel}</span></li>
        <li><span><i class="fas fa-bath"></i> Cleanliness Level:</span> <span id="cleanliness-level">${attributes.cleanlinessLevel}</span></li>
        <li><span><i class="fas fa-check"></i> Obedience Level:</span> <span id="obedience-level">${attributes.obedienceLevel}</span></li>
        <li><span><i class="fas fa-bolt"></i> Energy Level:</span> <span id="energy-level">${attributes.energyLevel}</span></li>
        <li><span><i class="fas fa-utensils"></i> Hunger Level:</span> <span id="hunger-level">${attributes.hungerLevel}</span></li>
        <li><span><i class="fas fa-chart-line"></i> Overall Status:</span> <span id="overall-status">${overallStatus}%</span></li>
    </ul>
</div>


    <p class="overall-status-label">Overall Status: ${overallStatus}%</p>
    <div class="status-bar ${statusColorClass}" style="width: ${overallStatus}%"></div>
</div>
  `;

    updatePetStats(attributes, overallStatus, statusColorClass);
}

/**
 * Extracts the name from the email.
 * @param {string} email - The user's email.
 * @returns {string} The extracted name.
 */
function extractNameFromEmail(email) {
    // Assuming the email format is "name@example.com"
    const atIndex = email.indexOf("@");
    if (atIndex !== -1) {
        return email.substring(0, atIndex);
    }
    return email;
}

/**
 * Calculates the overall status based on the pet attributes.
 * @param {object} attributes - The pet attributes.
 * @returns {number} The overall status.
 */
function calculateOverallStatus(attributes) {
    const {
        healthLevel,
        happinessLevel,
        cleanlinessLevel,
        obedienceLevel,
        energyLevel,
        hungerLevel
    } = attributes;

    return Math.floor(
        (healthLevel +
            happinessLevel +
            cleanlinessLevel +
            obedienceLevel +
            energyLevel +
            hungerLevel) / 6
    );
}

/**
 * Determines the CSS class for the status bar based on the overall status.
 * @param {number} overallStatus - The overall status.
 * @returns {string} The CSS class for the status bar.
 */
function determineStatusColorClass(overallStatus) {
    let statusColorClass;
    if (overallStatus >= 70) {
        statusColorClass = 'high-status';
    } else if (overallStatus >= 30) {
        statusColorClass = 'medium-status';
    } else {
        statusColorClass = 'low-status';
    }
    return statusColorClass;
}

/**
 * Updates the pet stats on the page.
 * @param {object} attributes - The pet attributes.
 * @param {number} overallStatus - The overall status.
 * @param {string} statusColorClass - The CSS class for the status bar.
 */
function updatePetStats(attributes, overallStatus, statusColorClass) {
    document.getElementById('health-level').textContent = attributes.healthLevel;
    document.getElementById('happiness-level').textContent = attributes.happinessLevel;
    document.getElementById('cleanliness-level').textContent = attributes.cleanlinessLevel;
    document.getElementById('obedience-level').textContent = attributes.obedienceLevel;
    document.getElementById('energy-level').textContent = attributes.energyLevel;
    document.getElementById('hunger-level').textContent = attributes.hungerLevel;
    document.getElementById('overall-status').textContent = `${overallStatus}%`;

    const statusBar = document.querySelector('.status-bar');
    statusBar.classList.remove('high-status', 'medium-status', 'low-status');
    statusBar.classList.add(statusColorClass);
    statusBar.style.width = `${overallStatus}%`;
}

/**
 * Checks if the user is logged in and has the required information.
 * @returns {boolean} True if the user is logged in and has the required information, otherwise false.
 */
function checkLoggedIn() {
    const userEmail = localStorage.getItem('email');
    const petId = localStorage.getItem('petId');
    return !!(userEmail && petId);
}

/**
 * Retrieves the pet attributes from the API.
 * @param {string} petId - The ID of the pet.
 * @returns {Promise<object>} A promise that resolves to the pet attributes.
 */
async function getPetAttributes(petId) {
    const petDashboardClient = new PetDashboardClient();
    return await petDashboardClient.getPetStats(petId);
}

/**
 * Updates the pet stats periodically.
 */
async function updatePetStatsPeriodically() {
    const updatedAttributes = await getPetAttributes(localStorage.getItem('petId'));
    const adoptedPetAttributes = {
        healthLevel: updatedAttributes.healthLevel,
        happinessLevel: updatedAttributes.happinessLevel,
        cleanlinessLevel: updatedAttributes.cleanlinessLevel,
        obedienceLevel: updatedAttributes.obedienceLevel,
        energyLevel: updatedAttributes.energyLevel,
        hungerLevel: updatedAttributes.hungerLevel
    };
    const overallStatus = calculateOverallStatus(adoptedPetAttributes);
    const statusColorClass = determineStatusColorClass(overallStatus);

    updatePetStats(adoptedPetAttributes, overallStatus, statusColorClass);
    displayWarningMessages(adoptedPetAttributes);
}

/**
 * Displays warning messages based on the pet attributes.
 * @param {object} attributes - The pet attributes.
 */
function displayWarningMessages(attributes) {
    const statThreshold = 50;
    const warningElement = document.getElementById('warning-message');
    warningElement.style.display = 'none';

    if (attributes.healthLevel < statThreshold) {
        displayWarning('Health level is low! Please take care of your pet.');
    }

    if (attributes.happinessLevel < statThreshold) {
        displayWarning('Happiness level is low! Spend some quality time with your pet.');
    }

    if (attributes.cleanlinessLevel < statThreshold) {
        displayWarning('Cleanliness level is low! Give your pet a bath.');
    }

    if (attributes.obedienceLevel < statThreshold) {
        displayWarning('Obedience level is low! Train your pet and reinforce good behavior.');
    }

    if (attributes.energyLevel < statThreshold) {
        displayWarning('Energy level is low! Make sure your pet gets enough rest and sleep.');
    }

    if (attributes.hungerLevel > statThreshold) {
        displayWarning('Hunger level is low! Feed your pet a nutritious meal.');
    }

    function displayWarning(message) {
        warningElement.textContent = message;
        warningElement.style.display = 'block';
    }
}

function userEmail() {
    return localStorage.getItem('email');

}

// Initialize the pet dashboard
async function initializePetDashboard() {
    const loggedIn = checkLoggedIn();
    if (loggedIn) {
        const adoptedPetName = "Desarapen";
        const adoptedPetId = localStorage.getItem('petId');
        const attributes = await getPetAttributes(adoptedPetId);
        const adoptedPetAttributes = {
            healthLevel: attributes.healthLevel,
            happinessLevel: attributes.happinessLevel,
            cleanlinessLevel: attributes.cleanlinessLevel,
            obedienceLevel: attributes.obedienceLevel,
            energyLevel: attributes.energyLevel,
            hungerLevel: attributes.hungerLevel
        };
        const overallStatus = calculateOverallStatus(adoptedPetAttributes);
        const statusColorClass = determineStatusColorClass(overallStatus);
        displayAdoptedPet(adoptedPetName, adoptedPetId, userEmail, adoptedPetAttributes, overallStatus, statusColorClass);
        setInterval(updatePetStatsPeriodically, 100);
    } else {
        console.log('User not logged in or missing pet ID or attributes');
    }
}

initializePetDashboard().then(() => console.log('Pet dashboard initialized'));
