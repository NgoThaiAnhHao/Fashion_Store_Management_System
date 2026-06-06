const updateProfileBtn = document.querySelector("#update-profile-btn")
const cancelUpdateProfileBtn = document.querySelector(".cancel-update-profile-btn")
const updateModal = document.querySelector("#crud-modal")

updateProfileBtn.addEventListener("click", (e) => {
    e.preventDefault();

    updateModal.classList.remove("hidden")
})

cancelUpdateProfileBtn.addEventListener("click", (e) => {
    e.preventDefault();

    updateModal.classList.add("hidden")
})