import customizePetClient from "../api/customizePetClient";

class CustomizePetPage {
    constructor() {
        this.onCustomize = this.onCustomize.bind(this);
        this.client = new customizePetClient();
    }

    async mount() {
        document.addEventListener("DOMContentLoaded", () => {
            const partsDiv = document.getElementById("partsDiv");
            const monsterDiv = document.getElementById("monsterDiv");

            partsDiv.addEventListener("dragstart", (event) => {
                event.dataTransfer.setData("text/plain", event.target.id);
            });

            monsterDiv.addEventListener("dragover", (event) => {
                event.preventDefault();
            });

            monsterDiv.addEventListener("drop", (event) => {
                event.preventDefault();
                const itemId = event.dataTransfer.getData("text/plain");
                const clonedItem = document.getElementById(itemId).cloneNode(true);
                clonedItem.className = "itemMove";
                monsterDiv.appendChild(clonedItem);
            });

            const adoptForm = document.getElementById("adopt-form2");
            adoptForm.addEventListener("submit", this.onCustomize);
        });
    }

    async onCustomize(event) {
        event.preventDefault();
        const petId = document.getElementById("adopt-pet-id").value;
        const attireInput = document.getElementById("attire");
        const attire = Array.from(
            document.getElementsByClassName("itemMove")
        ).map((item) => item.id);

        try {
            await this.client.customizePet(petId, attire);
            localStorage.setItem("petId", 69);
            localStorage.setItem("attire", attireInput.value);
            console.log("Pet customization successful!");

            const stats = {
                hungerLevel: 0,
                happinessLevel: 100,
                healthLevel: 100,
                cleanlinessLevel: 100,
                energyLevel: 100,
                obedienceLevel: 100,
                overallStatus: 100,
            };

            localStorage.setItem("petStats", JSON.stringify(stats));
            await this.petClient.setPetStats(petId, stats);
            console.log("Pet stats updated!", stats);
        } catch (error) {
            console.error("Pet customization failed:", error);
        }
    }
}

const main = async () => {
    const customizePetPage = new CustomizePetPage();
    await customizePetPage.mount();
};

window.addEventListener("DOMContentLoaded", main);




document.body.onload = function() {
    var monsterDiv = document.getElementById("monsterDiv");
    var partsDiv = document.getElementById("partsDiv");
    var parts = partsDiv.getElementsByTagName("img");
    var monsters = monsterDiv.getElementsByTagName("img");
    var itemIndex = 0;
    var xDistance = 0;
    var yDistance = 0;
    var foo = null;

    for (var i = 0; i < parts.length; i++) {
        parts[i].addEventListener("dragstart", dragstartFx, false);
    }

    function dragstartFx(event) {
        event.dataTransfer.setData("text", event.target.id);
        event.dataTransfer.effectAllow = "move";

        if (event.target.style.top === "") {
            partsDiv.style.backgroundColor = "rgba(255,255,255,0.04)";
        }

        xDistance = event.clientX - event.target.offsetLeft;
        yDistance = event.clientY - event.target.offsetTop;
    }

    for (i = 0; i < monsters.length; i++) {
        monsters[i].addEventListener("dragenter", dragenterFx, false);
    }
    function dragenterFx(event) {
        event.target.classList.toggle("active");
    }

    document.body.ondragover = function(event) {
        event.preventDefault();
        event.dataTransfer.dropEffect = "move";
    };

    document.body.ondrop = function(event) {
        event.preventDefault();
        if (event.target.parentNode === monsterDiv) {
            event.target.classList.toggle("active");
        }
        var data = event.dataTransfer.getData("text");
        var itemMove = document.getElementById(data);
        partsDiv.style.backgroundColor = "rgba(255,255,255,0.31)";
        itemMove.className = "itemMove";

        itemMove.style.top = event.clientY - yDistance + "px";
        itemMove.style.left = event.clientX - xDistance + "px";

        itemMove.style.zIndex = itemIndex + 1;
        itemIndex = Number(itemMove.style.zIndex);

        // Update the value of the attire input field with the id of the dropped item
        const attireInput = document.getElementById('attire');
        const attireValue = attireInput.value;
        if (attireValue) {
            attireInput.value = `${attireValue},${itemMove.id}`;
        } else {
            attireInput.value = itemMove.id;
        }
    };


    var heightPartsDiv = window.getComputedStyle(partsDiv).height;
    partsDiv.style.height = heightPartsDiv;
};

document.getElementsByClassName("btn")[0].onclick = function(e) {
    history.go(0);
};
