/* ============================ soft-list-edit.js ============================ */

// Расширяем глобальный объект actions
document.addEventListener("DOMContentLoaded", () => {
    if (window.actions) {
        Object.assign(window.actions, {
            "add-image": handleControlImage,
            "add-archive": handleAddArchive,
            "add-description": handleAddDescription,
            "save-soft": handleSaveSoft,
            "delete-soft": handleDeleteSoft
        });
    } else {
        console.warn("window.actions не найден — soft-list-edit.js загружен слишком рано");
    }
});


/* =============================================== */
/* === Универсальный fetch с обработкой ошибок === */
/* =============================================== */

function fetchWithErrorHandling(url, options = {}) {
    const headers = new Headers(options.headers || {});

    if (!headers.has("X-Requested-With")) {
        headers.set("X-Requested-With", "XMLHttpRequest");
    }

    const finalOptions = {...options, headers};

    return fetch(url, finalOptions)
        .then(async resp => {
            if (!resp.ok) {
                let errMsg = `HTTP ${resp.status}`;
                try {
                    const data = await resp.json();
                    errMsg = data.message || data.error || data.detail || errMsg;
                } catch {
                    try {
                        const text = await resp.text();
                        if (text) errMsg = text;
                    } catch {
                    }
                }
                showToast(errMsg, "error");
                throw new Error(errMsg);
            }
            return resp;
        })
        .catch(err => {
            console.error("Ошибка запроса:", err);
            if (!err.handled) showToast(err.message || "Неизвестная ошибка", "error");
            throw err;
        });
}



/**
 * Добавить описание
 */
function handleAddDescription(el) {
    const index = el.dataset.index;
    const row = getSoftRow(index);
    console.log("Редактирование описания для:", row);
    // TODO: открыть модалку редактирования
}


/**
 * Сохранить изменения в элементе ПО
 */
function handleSaveSoft(el) {
    const index = el.dataset.index;
    const data = collectSoftRequest(index);
    if (!data) return;

    console.log("Отправка обновлённых данных на сервер:", data);

    fetchWithErrorHandling("/api/v1/admin/update", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(data)
    })
        .then(async r => {
            const text = await r.text();
            return text ? JSON.parse(text) : null;
        })
        .then(() => showToast("Изменения сохранены", "success"));
}


/*
 * Собирает объект SoftUpdateRequest по индексу строки
 */
function collectSoftRequest(index) {
    const row = document.querySelector(`.soft-item-inline [data-index="${index}"]`)?.closest(".soft-item-inline");
    if (!row) return null;

    const idAttr = row.dataset.id;
    const category = row.querySelector(".soft-field.category")?.value;
    const name = row.querySelector(".soft-field.name")?.value.trim();

    if (!name) {
        showToast("Название не может быть пустым", "error");
        return null;
    }

    return {
        id: idAttr ? Number(idAttr) : null,
        name: name,
        category: category ? Number(category) : null,
        shortDescription: (row.querySelector(".soft-field.short-desc")?.value.trim()) || ""
    };
}


/**
 * Удалить
 * TODO: еще не проверен
 */
function handleDeleteSoft(el) {
    const index = el.dataset.index;
    const data = collectSoftData(index);
    if (!data)
        return;

    if (!confirm(`Удалить «${data.name || "элемент"}»?`))
        return;

    fetchWithErrorHandling(`/api/v1/soft/${data.id}`, {
        method: "DELETE"
    })
        .then(() => showToast("Удалено", "success"));
}


/* =============================== */
/* === Вспомогательные функции === */
/* =============================== */

function getSoftRow(index) {
    return document.querySelector(`.soft-item-inline [data-index="${index}"]`)?.closest(".soft-item-inline");
}

function collectSoftData(index) {
    const row = getSoftRow(index);
    if (!row) return null;
    return {
        id: row.dataset.id,
        name: row.querySelector(".soft-field.name")?.value.trim(),
        category: row.querySelector(".soft-field.category")?.value,
        shortDescription: row.querySelector(".soft-field.short-desc")?.value.trim()
    };
}


/**
 * Вывод сообщений.
 * @param message Строка сообщения.
 * @param type    Тип сообщения (error, info, success)
 */
function showToast(message, type = "info") {
    const toast = document.createElement("div");
    toast.textContent = message;
    toast.className = `toast ${type}`;

    Object.assign(toast.style, {
        position: "fixed",
        bottom: "15px",
        right: "15px",
        padding: "10px 15px",
        background: type === "success" ? "#4ade80" : type === "error" ? "#f87171" : "#60a5fa",
        color: "#fff",
        borderRadius: "6px",
        boxShadow: "0 2px 6px rgba(0,0,0,0.2)",
        fontSize: "14px",
        zIndex: 9999,
        opacity: 0,
        transition: "opacity 0.3s ease"
    });
    document.body.appendChild(toast);
    requestAnimationFrame(() => toast.style.opacity = 1);
    setTimeout(() => {
        toast.style.opacity = 0;
        setTimeout(() => toast.remove(), 5000);
    }, 5000);
}


/**
 * =======================================================================
 * Управление изображениями
 * =======================================================================
 */

/**
 * Открывает модальное окно управления изображениями.
 */
function handleControlImage(el) {
    const index = el.dataset.index;
    const row = getSoftRow(index);
    const softId = row.dataset.id;
    console.log("Управление изображениями для:", softId);

    const modal = document.getElementById("softimg-modal");
    const container = document.getElementById("softimg-container");

    container.innerHTML = "";
    modal.style.display = "flex";

    // Загрузка изображений для выбранного софта
    fetchWithErrorHandling(`/api/v1/admin/images-manage/${softId}`)
        .then(resp => resp.json())
        .then(images => renderImageGrid(images, softId));

    modal.querySelector(".softimg-modal-close").onclick = () => closeSoftimgModal();
    modal.onclick = e => {
        if (e.target === modal)
            closeSoftimgModal();
    };
}

/**
 * Закрытие модального окна
 */
function closeSoftimgModal() {
    const modal = document.getElementById("softimg-modal");
    modal.style.display = "none";
}

/**
 * Отрисовка галереи изображений.
 */
function renderImageGrid(images, softId) {
    const container = document.getElementById("softimg-container");
    container.innerHTML = "";

    images.forEach(img => {
        const div = document.createElement("div");
        div.classList.add("softimg-item");

        const image = document.createElement("img");
        image.src = img.url;
        image.title = "Кликните для замены изображения";
        image.alt = img.filename || "image";
        div.appendChild(image);

        const delBtn = document.createElement("button");
        delBtn.className = "softimg-btn-delete";
        delBtn.innerHTML = "×";
        delBtn.title = "Удалить изображение";
        delBtn.onclick = () => deleteSoftimgImage(softId, img.filename);
        div.appendChild(delBtn);

        image.onclick = () => replaceSoftimgImage(softId, img.filename);
        container.appendChild(div);
    });

    const addSlot = document.createElement("div");
    addSlot.className = "softimg-item add-slot";
    addSlot.title = "Добавить изображение";
    addSlot.innerHTML = "+";
    addSlot.onclick = () => addSoftimgImage(softId);
    container.appendChild(addSlot);
}


/**
 * Добавление нового изображения.
 */
function addSoftimgImage(softId) {
    const input = document.createElement("input");
    input.type = "file";
    input.accept = "image/*";
    input.onchange = () => {
        if (!input.files?.length) return;

        const formData = new FormData();
        formData.append("file", input.files[0]);

        fetchWithErrorHandling(`/api/v1/admin/images-manage/${softId}`, {
            method: "POST",
            body: formData
        }).then(() => handleControlImage({dataset: {index: getRowIndexById(softId)}}));
    };
    input.click();
}


// ===  ===
/**
 * Замена существующего изображения.
 */
function replaceSoftimgImage(softId, imageName) {
    const input = document.createElement("input");
    input.type = "file";
    input.accept = "image/*";
    input.onchange = () => {
        if (!input.files?.length)
            return;

        const formData = new FormData();
        formData.append("file", input.files[0]);

        fetchWithErrorHandling(`/api/v1/admin/images-manage/${softId}/${imageName}`, {
            method: "PUT",
            body: formData
        }).then(() => handleControlImage({dataset: {index: getRowIndexById(softId)}}));
    };
    input.click();
}


/**
 * Удаление изображения
 */
function deleteSoftimgImage(softId, imageName) {
    if (!confirm("Удалить изображение?"))
        return;

    fetchWithErrorHandling(`/api/v1/admin/images-manage/${softId}/${imageName}`, {
        method: "DELETE"
    }).then(() => {
        const imageDiv = document.querySelector(`.softimg-item img[src*="${imageName}"]`)?.closest(".softimg-item");
        if (imageDiv)
            imageDiv.remove();
    });
}

function getRowIndexById(softId) {
    const rows = document.querySelectorAll(".soft-item-inline");
    for (let i = 0; i < rows.length; i++) {
        if (rows[i].dataset.id === softId) return i;
    }
    return null;
}


/**
 * =======================================================================
 * Управление аттачами
 * =======================================================================
 */

/**
 * Открывает модальное окно управления архивами.
 */
function handleAddArchive(el) {
    const index = el.dataset.index;
    const row = getSoftRowAttache(index);
    const softId = row.dataset.id;

    const modal = document.getElementById("softfile-modal");
    const container = document.getElementById("softfile-container");

    container.innerHTML = "";
    modal.style.display = "flex";

    fetchWithErrorHandling(`/api/v1/admin/archives-manage/${softId}`)
        .then(resp => resp.json())
        .then(files => renderFileList(files, softId));

    modal.querySelector(".softfile-modal-close").onclick = () => closeSoftfileModal();
    modal.onclick = e => {
        if (e.target === modal)
            closeSoftfileModal();
    };

    const addBtn = document.getElementById("softfile-add-btn");
    addBtn.onclick = () => addSoftfileArchive(softId);
}


/**
 * Закрытие модального окна.
 */
function closeSoftfileModal() {
    const modal = document.getElementById("softfile-modal");
    modal.style.display = "none";
}


/**
 * Отрисовка списка файлов.
 */
function renderFileList(files, softId) {
    const container = document.getElementById("softfile-container");
    container.innerHTML = "";

    if (!files || files.length === 0) {
        container.innerHTML = `<p style="color:#777;">Файлы отсутствуют</p>`;
        return;
    }

    files.forEach(file => {
        const item = document.createElement("div");
        item.className = "softfile-item";

        const nameSpan = document.createElement("span");
        nameSpan.className = "softfile-name";
        nameSpan.textContent = file.filename;

        const actions = document.createElement("div");
        actions.className = "softfile-actions";

        const replaceBtn = document.createElement("button");
        replaceBtn.title = "Заменить файл";
        replaceBtn.innerHTML = `<img src="/menu/icons/replace.png" alt="replace"/>`;
        replaceBtn.onclick = () => replaceSoftfileArchive(softId, file.filename);

        const deleteBtn = document.createElement("button");
        deleteBtn.title = "Удалить файл";
        deleteBtn.innerHTML = `<img src="/menu/icons/delete.png" alt="delete"/>`;
        deleteBtn.onclick = () => deleteSoftfileArchive(softId, file.filename, item);

        actions.appendChild(replaceBtn);
        actions.appendChild(deleteBtn);

        item.appendChild(nameSpan);
        item.appendChild(actions);
        container.appendChild(item);
    });
}


/**
 * Добавление нового файла.
 */
function addSoftfileArchive(softId) {
    const input = document.createElement("input");
    input.type = "file";
    input.onchange = () => {
        if (!input.files?.length)
            return;

        const formData = new FormData();
        formData.append("file", input.files[0]);

        fetchWithErrorHandling(`/api/v1/admin/archives-manage/${softId}`, {
            method: "POST",
            body: formData
        }).then(() => handleAddArchive({dataset: {index: getRowIndexByIdArchive(softId)}}));
    };
    input.click();
}


/**
 * Замена существующего файла.
 */
function replaceSoftfileArchive(softId, filename) {
    const input = document.createElement("input");
    input.type = "file";
    input.onchange = () => {
        if (!input.files?.length)
            return;

        const formData = new FormData();
        formData.append("file", input.files[0]);

        fetchWithErrorHandling(`/api/v1/admin/archives-manage/${softId}/${filename}`, {
            method: "PUT",
            body: formData
        }).then(() => handleAddArchive({dataset: {index: getRowIndexByIdArchive(softId)}}));
    };
    input.click();
}


/**
 * Удаление файла.
 */
function deleteSoftfileArchive(softId, filename, itemElement) {
    if (!confirm(`Удалить файл "${filename}"?`))
        return;

    fetchWithErrorHandling(`/api/v1/admin/archives-manage/${softId}/${filename}`, {
        method: "DELETE"
    }).then(() => itemElement.remove());
}


/**
 * Получение строки по индексу (используется для повторного открытия).
 */
function getSoftRowAttache(index) {
    return document.querySelectorAll(".soft-item-inline")[index];
}

function getRowIndexByIdArchive(softId) {
    const rows = document.querySelectorAll(".soft-item-inline");
    for (let i = 0; i < rows.length; i++) {
        if (rows[i].dataset.id === softId) return i;
    }
    return null;
}
