document.addEventListener("DOMContentLoaded", function () {
    dragula([document.querySelector('.squad-available-ships-container'),
        document.querySelector('.squad-assigned-ships-container')]);

    leftContainer = document.querySelector(".squad-available-ships-container");
    rightContainer = document.querySelector(".squad-assigned-ships-container");
});


function moveShipsToContainer(ships, container) {
    ships.forEach(function (f) {
        container.appendChild(f);
    })
}

function showRenameInput() {
    var nameInputField = document.querySelector("input.squad-rename-input");
    nameInputField.classList.remove("squad-rename-hidden");

    var nameInputBtn = document.querySelector(".squad-rename-button");
    nameInputBtn.classList.add("squad-rename-hidden")
}

function squadControlMoveAllRight() {
    moveShipsToContainer(leftContainer.querySelectorAll(".hangar-ship-slot"), rightContainer);
}

function squadControlMoveAllLeft() {
    moveShipsToContainer(rightContainer.querySelectorAll(".hangar-ship-slot"), leftContainer);
}

function submitSquadCreationForm() {
    var form = document.querySelector(".squad-create-form");
    var idListField = form.querySelector("input.squad-create-form-input-id-list");
    var commandField = form.querySelector("input.squad-create-form-input-command");
    var nameField = form.querySelector("input.squad-create-form-input-name");

    var nameInputField = document.querySelector("input.squad-rename-input");

    idListField.value = Array.prototype.map.call(rightContainer.querySelectorAll(".hangar-ship-slot"), function (item) {
        return item.dataset.shipId;
    }).join(",");
    commandField.value="edit";

    if (nameInputField.value !== "" && !nameInputField.classList.contains("squad-rename-hidden")){
        if (nameInputField.reportValidity()){
            nameField.value = nameInputField.value;
        } else {
            return;
        }
    }

    form.submit();
}

function submitSquadDisbandForm() {
    var form = document.querySelector(".squad-create-form");
    var commandField = form.querySelector("input.squad-create-form-input-command");
    commandField.value="delete";
    form.submit();
}