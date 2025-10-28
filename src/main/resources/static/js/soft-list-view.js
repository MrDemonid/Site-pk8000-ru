// Реакция на выбор файла в выпадающем списке.
// Чтобы браузер не воспринял как переход на новую страницу (тогда существующая моргнет при перерисовке).
document.addEventListener('change', function(event) {
    const target = event.target;
    if (target.classList.contains('archive-select')) {
        const url = target.value;
        if (!url)
            return;

        const a = document.createElement('a');
        a.href = url;
        a.download = url.substring(url.lastIndexOf('/') + 1);
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);

        target.value = ''; // сброс выбора
    }
});



// Реестр действий
const actions = {
    "open-modal-images": handleOpenModal,
    "open-modal-description": handleOpenDescription
};

// Делегирование кликов
document.addEventListener("click", e => {
    const el = e.target.closest("[data-action]");
    if (!el) return;

    const actionName = el.dataset.action;
    if (typeof actions[actionName] === "function") {
        actions[actionName](el);
    }
});


/*
Открытие модального окна галереи картинок.
 */
function handleOpenModal(img) {
    const item = img.closest(".product-item");
    if (!item) return;

    const modal = document.getElementById("softModal");
    const modalOverlay = modal.querySelector(".soft-modal-overlay");
    const modalClose = modal.querySelector(".modal-close-btn");
    const modalTitle = modal.querySelector("#modalTitle");
    const modalMainImage = modal.querySelector("#modalMainImage");
    const modalThumbnails = modal.querySelector(".modal-thumbnails");
    const content = modal.querySelector(".soft-modal-content");

    const softName = item.querySelector(".product-title")?.textContent || "Без названия";
    const images = item.querySelectorAll(".product-image");
    const imageUrls = Array.from(images).map(i => i.src);
    const currentIndex = Array.from(images).indexOf(img);

    modalTitle.textContent = softName;
    modalMainImage.src = imageUrls[currentIndex];

    modalThumbnails.innerHTML = "";
    imageUrls.forEach((url, idx) => {
        const thumb = document.createElement("img");
        thumb.src = url;
        if (idx === currentIndex) thumb.classList.add("active");
        thumb.addEventListener("click", () => {
            modalMainImage.src = url;
            modalThumbnails.querySelectorAll("img").forEach(i => i.classList.remove("active"));
            thumb.classList.add("active");
            adjustImageHeight(); // пересчитать при смене картинки
        });
        modalThumbnails.appendChild(thumb);
    });

    modal.classList.remove("hidden");
    document.body.style.overflow = "hidden";

    /*
    Вычисляем оптимальную высоту для картинки (modalMainImage)
     */
    function adjustImageHeight() {
        const modalMaxHeight = window.innerHeight * 0.9; // 90vh
        const style = getComputedStyle(content);
        const paddingTop = parseFloat(style.paddingTop) || 0;
        const paddingBottom = parseFloat(style.paddingBottom) || 0;
        // const bottomGap = 20; // добавляем визуальный зазор
        const imgStyle = getComputedStyle(modalMainImage);
        const marginTop = parseFloat(imgStyle.marginTop) || 0;
        const marginBottom = parseFloat(imgStyle.marginBottom) || 0;

        const available = modalMaxHeight
            - modalTitle.offsetHeight
            - modalThumbnails.offsetHeight
            - paddingTop
            - paddingBottom
            - marginTop
            - marginBottom;

        modalMainImage.style.maxHeight = available + "px";
    }


    adjustImageHeight();
    window.addEventListener("resize", adjustImageHeight);

    const closeModal = () => {
        modal.classList.add("hidden");
        document.body.style.overflow = "";
        document.removeEventListener("keydown", onEsc);
        window.removeEventListener("resize", adjustImageHeight);
        document.removeEventListener("keydown", onEsc);
    };

    const onEsc = (e) => { if(e.key === "Escape") closeModal(); };
    document.addEventListener("keydown", onEsc);

    modalClose.onclick = closeModal;
    modalOverlay.onclick = closeModal;
}


/*
Открытие модального окна просмотра description.
 */
function handleOpenDescription(el) {
    const item = el.closest(".product-item");
    if (!item) return;

    const modal = document.getElementById("descriptionModal");
    const modalClose = modal.querySelector(".modal-close-btn");
    const modalTitle = modal.querySelector("#descriptionModalTitle");
    const modalBody = modal.querySelector("#descriptionModalBody");

    modalTitle.textContent = item.querySelector(".product-title")?.textContent || "Без названия";
    modalBody.innerHTML = item.querySelector(".product-full-description")?.innerHTML || "Описание отсутствует";


    function adjustModalSize() {
        const modal = document.getElementById("descriptionModal");
        const wrapper = modal.querySelector(".description-modal-wrapper");
        // ограничение по высоте окна
        const viewportHeight = window.innerHeight * 0.9;
        wrapper.style.maxHeight = viewportHeight + "px";
    }

    adjustModalSize();
    window.addEventListener("resize", adjustModalSize);

    modal.classList.remove("hidden");
    document.body.style.overflow = "hidden";

    const closeModal = () => {
        modal.classList.add("hidden");
        document.body.style.overflow = "";
        window.removeEventListener("resize", adjustModalSize);
        document.removeEventListener("keydown", onEsc);
    };

    const onEsc = (e) => { if (e.key === "Escape") closeModal(); };
    document.addEventListener("keydown", onEsc);

    modalClose.onclick = closeModal;
    modal.querySelector(".soft-modal-overlay").onclick = closeModal;
}

