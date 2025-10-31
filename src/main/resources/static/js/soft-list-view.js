/* ======================== soft-list-view.js ======================== */

// Реакция на выбор файла в выпадающем списке.
// Чтобы браузер не воспринял как переход на новую страницу (тогда существующая моргнет при перерисовке).
document.addEventListener('change', function (event) {
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
window.actions = {
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
 * Открытие модального окна галереи картинок.
 */
async function handleOpenModal(img) {
    const item = img.closest(".product-item");
    if (!item)
        return;

    const modal = document.getElementById("softModal");
    const modalOverlay = modal.querySelector(".soft-modal-overlay");
    const modalClose = modal.querySelector(".modal-close-btn");
    const modalTitle = modal.querySelector("#modalTitle");
    const modalMainImage = modal.querySelector("#modalMainImage");
    const modalThumbnails = modal.querySelector(".modal-thumbnails");
    const content = modal.querySelector(".soft-modal-content");

    const softName = item.dataset.name || "Без названия";
    const mainUrl = img.src;
    let imageUrls = [];

    // Берем все дополнительные URL из data-images (разделитель |)
    const dataImages = item.querySelector(".product-image").dataset.images;
    if (dataImages) {
        imageUrls = dataImages.split('|').map(s => s.trim()).filter(Boolean);
    }

    modalTitle.textContent = softName;
    modalMainImage.src = mainUrl;

    modalThumbnails.innerHTML = "";

    // Асинхронная подгрузка превью
    imageUrls.forEach((url, idx) => {
        const thumb = document.createElement("img");
        thumb.classList.add("modal-thumbnail");

        if (url === mainUrl)
            thumb.classList.add("active");

        thumb.src = "";
        setTimeout(() => { thumb.src = url; }, 0);

        thumb.addEventListener("click", () => {
            modalMainImage.src = url;
            modalThumbnails.querySelectorAll("img").forEach(i => i.classList.remove("active"));
            thumb.classList.add("active");
            adjustImageHeight();
        });
        modalThumbnails.appendChild(thumb);
    });

    modal.classList.remove("hidden");
    document.body.style.overflow = "hidden";

    /*
     * Вычисляем оптимальную высоту для картинки (modalMainImage)
     */
    function adjustImageHeight() {
        const modalMaxHeight = window.innerHeight * 0.9;
        const style = getComputedStyle(content);
        const paddingTop = parseFloat(style.paddingTop) || 0;
        const paddingBottom = parseFloat(style.paddingBottom) || 0;
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
        window.removeEventListener("resize", adjustImageHeight);
        document.removeEventListener("keydown", onEsc);
    };
    const onEsc = (e) => { if(e.key === "Escape") closeModal(); };
    document.addEventListener("keydown", onEsc);

    modalClose.onclick = closeModal;
    modalOverlay.onclick = closeModal;
}



/*
 * Открытие модального окна для просмотра описания программы.
 */
const descriptionCache = new Map(); // кэш загруженных описаний


async function handleOpenDescription(el) {
    const item = el.closest(".product-item");
    if (!item) return;

    const productId = item.dataset.id;
    const productName = item.dataset.name || "Без названия";

    const modal = document.getElementById("descriptionModal");
    const modalClose = modal.querySelector(".modal-close-btn");
    const modalBody = modal.querySelector("#descriptionModalBody");

    modalBody.innerHTML = "<p class='loading'>Загрузка описания...</p>";

    // показываем окно сразу, чтобы не было задержки
    modal.classList.remove("hidden");
    document.body.style.overflow = "hidden";

    // подгружаем описание (если не в кэше)
    let description = descriptionCache.get(productId);
    if (!description) {
        try {
            const response = await fetch(`/api/v1/soft/games/description/${productId}`);
            if (response.ok) {
                description = await response.text();
                descriptionCache.set(productId, description);
            } else if (response.status === 404) {
                description = "<p>Описание отсутствует.</p>";
            } else {
                description = `<p>Ошибка загрузки: ${response.status}</p>`;
            }
        } catch (err) {
            description = "<p>Не удалось загрузить описание (ошибка сети).</p>";
            console.error(err);
        }
    }

    modalBody.innerHTML = description;

    // Подгоняем размер окна
    function adjustModalSize() {
        const wrapper = modal.querySelector(".description-modal-wrapper");
        const viewportHeight = window.innerHeight * 0.9;
        wrapper.style.maxHeight = viewportHeight + "px";
    }

    adjustModalSize();
    window.addEventListener("resize", adjustModalSize);

    const closeModal = () => {
        modal.classList.add("hidden");
        document.body.style.overflow = "";
        window.removeEventListener("resize", adjustModalSize);
        document.removeEventListener("keydown", onEsc);
    };
    const onEsc = (e) => {
        if (e.key === "Escape") closeModal();
    };
    document.addEventListener("keydown", onEsc);

    modalClose.onclick = closeModal;
    modal.querySelector(".soft-modal-overlay").onclick = closeModal;
}
