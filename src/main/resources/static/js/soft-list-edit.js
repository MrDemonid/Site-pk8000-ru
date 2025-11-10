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


/**
 * Универсальный fetch с обработкой ошибок.
 */
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


/**
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
 * Удалить продукт, вместе с аттачами.
 */
function handleDeleteSoft(el) {
    const index = el.dataset.index;
    const data = collectSoftData(index);
    if (!data)
        return;

    if (!confirm(`Удалить «${data.name || "элемент"}»?`))
        return;

    fetchWithErrorHandling(`/api/v1/admin/delete/${data.id}`, {
        method: "DELETE"
    })
        .then(() => {
                showToast("Удалено", "success");
            const url = new URL(window.location.href);
            loadPage(url);
        });
}


/* ============================================================================================= */
/* ================================== Вспомогательные функции ================================== */
/* ============================================================================================= */

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


/**
 * =======================================================================
 * Управление описаниями.
 * =======================================================================
 */

// Глобальное состояние модалки
let currentDescriptionState = {
    attachments: [],          // локально добавленные файлы
    removedAttachments: [],   // удалённые оригинальные файлы
    replacedAttachments: {},  // заменённые оригинальные { oldName: newFile }
    originalAttachments: []   // файлы, пришедшие с сервера
};


/**
 * Открытие модалки управления описанием
 */
function handleAddDescription(el) {
    const index = el.dataset.index;
    const row = getSoftRowAttacheDescription(index);
    const softId = row.dataset.id;

    const modal = document.getElementById("softdesc-modal");
    const titleEl = document.getElementById("softdesc-title");
    const attachmentsContainer = document.getElementById("softdesc-attachments");
    const addFileBtn = document.getElementById("softdesc-attach-add-file");
    const addFolderBtn = document.getElementById("softdesc-attach-add-folder");
    const clearBtn = document.getElementById("softdesc-attach-clear");
    const applyBtn = document.getElementById("softdesc-apply-btn");
    const downloadBtn = document.getElementById("softdesc-download-btn");
    const deleteBtn = document.getElementById("softdesc-delete-btn");

    // показать модалку через существующую функцию
    showModalSoftdesc();

    // заголовок
    const nameInput = row.querySelector(".soft-field.name") || row.querySelector(".name");
    const productName = nameInput ? (nameInput.value || nameInput.textContent || `#${softId}`) : `#${softId}`;
    titleEl.textContent = `Описание — ${productName}`;

    // закрытие модалки
    modal.querySelector(".softdesc-modal-close").onclick = () => closeSoftdescModal();
    modal.onclick = e => {
        if (e.target === modal) closeSoftdescModal();
    };

    // очистка контейнера
    attachmentsContainer.innerHTML = `<p style="color:#777;">Загрузка...</p>`;

    // сброс локального состояния
    currentDescriptionState = {
        attachments: [],
        removedAttachments: [],
        replacedAttachments: {},
        originalAttachments: []
    };

    // Загрузка с сервера
    fetchWithErrorHandling(`/api/v1/admin/description-manage/${softId}`)
        .then(resp => resp.json())
        .then(data => renderDescriptionData(data, softId))
        .catch(err => {
            console.error(err);
            attachmentsContainer.innerHTML = `<p style="color:#777;">Не удалось загрузить файлы</p>`;
        });

    // добавление одиночных файлов
    addFileBtn.onclick = () => selectFiles(softId, attachmentsContainer, false);

    // добавление папки
    addFolderBtn.onclick = () => selectFiles(softId, attachmentsContainer, true);

    // очистка локальных файлов
    clearBtn.onclick = () => clearAttachmentFiles(attachmentsContainer);

    // сохранение изменений
    applyBtn.onclick = () => applyDescriptionChanges(softId);

    // скачивание
    downloadBtn.onclick = () => downloadDescriptionFile(softId);

    // удаление
    deleteBtn.onclick = () => deleteDescriptionFile(softId);
}

function showModalSoftdesc() {
    const modal = document.getElementById("softdesc-modal");
    modal.style.display = "flex";
}

function closeSoftdescModal() {
    const modal = document.getElementById("softdesc-modal");
    modal.style.display = "none";
}

/**
 * Скачать zip-файл описания продукта через AJAX.
 */
function downloadDescriptionFile(softId) {

    fetchWithErrorHandling(`/api/v1/admin/description-manage/zip/${softId}`, {
        method: 'GET',
        headers: {
            'Accept': 'application/zip'
        }
    })
        .then(async resp => {
            // Проверяем Content-Type на случай, если сервер вернул ошибку JSON
            const contentType = resp.headers.get('Content-Type') || '';
            if (contentType.includes('application/json')) {
                // Ошибка пришла как JSON, считываем и выбрасываем
                const errData = await resp.json();
                throw new Error(errData.message || 'Ошибка при скачивании файла');
            }

            // Считаем файл как Blob
            const blob = await resp.blob();

            // Пытаемся взять имя файла из заголовка Content-Disposition
            let filename = `description_${softId}.zip`;
            const disposition = resp.headers.get('Content-Disposition');
            if (disposition) {
                const match = disposition.match(/filename="?([^"]+)"?/);
                if (match && match[1])
                    filename = match[1];
            }

            // Создаем ссылку и инициируем скачивание
            const downloadUrl = URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = downloadUrl;
            a.download = filename;
            document.body.appendChild(a);
            a.click();
            a.remove();
            URL.revokeObjectURL(downloadUrl);
        })
        .catch(err => {
            console.error('Ошибка при скачивании файла:', err);
        });
}


/**
 * Удалить описание.
 */
function deleteDescriptionFile(softId) {
    fetchWithErrorHandling(`/api/v1/admin/description-manage/${softId}`, {
        method: "DELETE"
    })
        .then(() => {
            showToast("Описание удалено", "success");
            return fetchWithErrorHandling(`/api/v1/admin/description-manage/${softId}`);
        })
        .then(resp => resp.json())
        .then(description => {
            // сброс локального состояния
            currentDescriptionState.attachments = [];
            currentDescriptionState.removedAttachments = [];
            currentDescriptionState.replacedAttachments = {};
            currentDescriptionState.originalAttachments = description.files || [];

            renderDescriptionData(description, softId);
        })
        .catch(err => console.error(err));
}


/**
 * Создание элемента attachment.
 */
function createAttachmentItem(name, isOriginal, softId, fileObj = null) {
    const item = document.createElement("div");
    item.className = "softdesc-attach-item";

    const nameSpan = document.createElement("span");
    nameSpan.className = "softdesc-attach-name";
    nameSpan.textContent = fileObj?.relativePath || name;

    const actions = document.createElement("div");
    actions.className = "softdesc-actions";

    const replaceBtn = document.createElement("button");
    replaceBtn.title = "Заменить";
    replaceBtn.innerHTML = `<img src="/menu/icons/replace.png" alt="replace"/>`;
    replaceBtn.onclick = () => replaceAttachment(name, isOriginal, softId, item);

    const delBtn = document.createElement("button");
    delBtn.title = "Удалить";
    delBtn.innerHTML = `<img src="/menu/icons/delete.png" alt="delete"/>`;
    delBtn.onclick = () => deleteAttachment(name, isOriginal, softId, item);

    actions.appendChild(replaceBtn);
    actions.appendChild(delBtn);

    item.appendChild(nameSpan);
    item.appendChild(actions);

    return item;
}


/**
 * Заменить файл.
 */
function replaceAttachment(name, isOriginal, softId, itemEl) {
    const input = document.createElement("input");
    input.type = "file";
    input.onchange = () => {
        const file = input.files[0];
        if (!file) return;

        // старый путь (чтобы показать пользователю, где этот файл лежал)
        const oldRelativePath = name;

        // отображаем имя с сохранённым путём
        itemEl.querySelector(".softdesc-attach-name").textContent =
            `${oldRelativePath} → ${file.name}`;

        if (isOriginal) {
            // сохраняем файл с сохранением старого пути
            currentDescriptionState.replacedAttachments[oldRelativePath] = {
                file: file,
                oldPath: oldRelativePath
            };
        } else {
            // для локально добавленных замен
            const index = currentDescriptionState.attachments.findIndex(f => f.relativePath === oldRelativePath);
            if (index >= 0) currentDescriptionState.attachments[index] = {file, relativePath: oldRelativePath};
        }
    };
    input.click();
}


/**
 * Удалить файл.
 */
function deleteAttachment(name, isOriginal, softId, itemEl) {
    if (!confirm(`Удалить файл "${name}"?`)) return;

    if (isOriginal) {
        currentDescriptionState.removedAttachments.push(name);
        currentDescriptionState.originalAttachments =
            currentDescriptionState.originalAttachments.filter(n => n !== name);
        delete currentDescriptionState.replacedAttachments[name];
    } else {
        currentDescriptionState.attachments =
            currentDescriptionState.attachments.filter(f => f.relativePath !== name);
    }

    itemEl.remove();
    updateEmptyPlaceholder(document.getElementById("softdesc-attachments"));
}


/**
 * Добавление локального файла (папки) в список аттачей.
 */
function selectFiles(softId, attachmentsContainer, isFolder) {
    const input = document.createElement("input");
    input.type = "file";
    input.multiple = true;
    if (isFolder)
        input.webkitdirectory = true;
    input.style.display = "none";

    input.onchange = () => processFiles(input.files, attachmentsContainer);
    document.body.appendChild(input);
    input.click();
    input.remove();
}

// обработка выбранных файлов
function processFiles(fileList, attachmentsContainer) {
    for (const file of fileList) {
        const relativePath = file.webkitRelativePath || file.name;

        const exists = currentDescriptionState.attachments.some(f => f.relativePath === relativePath) ||
            currentDescriptionState.originalAttachments.includes(relativePath);

        if (exists) {
            showToast(`Файл "${relativePath}" уже присутствует`, "error");
            continue;
        }

        currentDescriptionState.attachments.push({file, relativePath});
        attachmentsContainer.appendChild(createAttachmentItem(relativePath, false, null, {file, relativePath}));
        updateEmptyPlaceholder(attachmentsContainer);
    }
}


/**
 * Очистка локальных аттачей.
 */
function clearAttachmentFiles(attachmentsContainer) {
    if (!confirm("Очистить список всех файлов?"))
        return;

    currentDescriptionState.attachments = [];
    currentDescriptionState.removedAttachments = [];
    currentDescriptionState.replacedAttachments = {};
    currentDescriptionState.originalAttachments = [];

    // Очистка DOM
    attachmentsContainer.innerHTML = "";
    updateEmptyPlaceholder(attachmentsContainer);
}


/**
 * Перерисовка списка файлов.
 */
function renderDescriptionData(description, softId) {
    const attachmentsContainer = document.getElementById("softdesc-attachments");
    attachmentsContainer.innerHTML = "";

    const files = (description && Array.isArray(description.files)) ? description.files : [];
    currentDescriptionState.originalAttachments = [...files];

    const allFiles = [];

    // Оригинальные файлы
    files.forEach(f => {
        const replaced = currentDescriptionState.replacedAttachments[f];
        allFiles.push({
            name: f,
            isOriginal: true,
            replacedObj: replaced || null
        });
    });

    // Локально добавленные
    currentDescriptionState.attachments.forEach(f => {
        const name = f.relativePath || f.file?.name;
        allFiles.push({name, isOriginal: false, fileObj: f});
    });

    // Отрисовка
    allFiles.forEach(f => {
        const el = createAttachmentItem(f.name, f.isOriginal, softId, f.fileObj);

        // визуальное различие для заменённых файлов
        if (f.replacedObj) {
            const newName = f.replacedObj.file.name;
            const oldPath = f.replacedObj.oldPath;
            const span = el.querySelector(".softdesc-attach-name");
            span.textContent = `${oldPath} → ${newName}`;
            span.style.color = "#0066cc";
        }

        attachmentsContainer.appendChild(el);
    });

    updateEmptyPlaceholder(attachmentsContainer);
}


/**
 * Сохранение изменений на сервер.
 */
function applyDescriptionChanges(softId) {
    console.log("applyDescriptionChanges():", JSON.stringify(currentDescriptionState, null, 2));

    const formData = new FormData();

    // Метаданные изменений
    const replacedMap = {};
    for (const [oldName, obj] of Object.entries(currentDescriptionState.replacedAttachments)) {
        replacedMap[oldName] = obj.file.name; // только имя нового файла
        formData.append('attachments', obj.file, obj.file.name);
    }
    const meta = {
        removed: currentDescriptionState.removedAttachments,
        replaced: replacedMap
    };

    formData.append('meta', new Blob([JSON.stringify(meta)], {type: 'application/json'}));
    // Добавленные файлы
    currentDescriptionState.attachments.forEach(obj => {
        if (obj && obj.file) {
            formData.append('attachments', obj.file, obj.relativePath || obj.file.name);
        }
    });

    fetchWithErrorHandling(`/api/v1/admin/description-manage/${softId}`, {
        method: 'PUT',
        body: formData
    })
        .then(() => {
            showToast("Изменения сохранены", "success");
            return fetchWithErrorHandling(`/api/v1/admin/description-manage/${softId}`);
        })
        .then(resp => resp.json())
        .then(description => {
            currentDescriptionState.attachments = [];
            currentDescriptionState.removedAttachments = [];
            currentDescriptionState.replacedAttachments = {};
            currentDescriptionState.originalAttachments = description.files || [];

            renderDescriptionData(description, softId);
        })
        .catch(err => console.error(err));
}


function getSoftRowAttacheDescription(index) {
    return document.querySelectorAll(".soft-item-inline")[index];
}

// ================================================================================================
// ======================= Управление placeholder-ом ("Файлы отсутствуют") ========================
// ================================================================================================

function updateEmptyPlaceholder(container) {
    const totalCount =
        currentDescriptionState.originalAttachments.length +
        currentDescriptionState.attachments.length;

    const placeholder = container.querySelector(".softdesc-placeholder");

    if (totalCount === 0) {
        if (!placeholder) {
            const p = document.createElement("p");
            p.className = "softdesc-placeholder";
            p.style.color = "#777";
            p.textContent = "Файлы отсутствуют";
            container.appendChild(p);
        }
    } else {
        if (placeholder) placeholder.remove();
    }
}

// Вывод для отладки.
function logFormData(fd) {
    for (let [k, v] of fd.entries()) {
        if (v instanceof File)
            console.log(`${k}: FILE ${v.name} (${v.type}, ${v.size} байт)`);
        else if (v instanceof Blob)
            v.text().then(t => console.log(`${k}: BLOB JSON = ${t}`));
        else
            console.log(`${k}: ${v}`);
    }
}
