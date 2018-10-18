function createSquad() {
    var hangarShipSlots = document.querySelector(".hangar-slots-container").querySelectorAll(".hangar-ship-slot");
    hangarShipSlots.forEach(function (slot) {
            if (slot.dataset.squadId != null) {
                highlightSlot("in-squad", slot);
            }
        }
    )
}

function resetSlot(slot){
    slot.classList.remove("hangar-slot-in-squad");
    slot.classList.remove("hangar-slot-selected");
}



function highlightSlot(selectionType, slot) {
    if (selectionType === "in-squad"){
        slot.classList.add("hangar-slot-in-squad")
    }
}