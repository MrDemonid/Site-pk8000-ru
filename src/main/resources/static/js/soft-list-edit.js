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
 * Добавить архив
 */
function handleAddArchive(el) {
    const index = el.dataset.index;
    const row = getSoftRow(index);
    console.log("Добавление архива для:", row);
    // TODO: открыть модалку для загрузки архива
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

    fetch("/api/v1/admin/update", {
        method: "POST",
        headers: { "Content-Type": "application/json", "X-Requested-With": "XMLHttpRequest" },

        body: JSON.stringify(data)
    })
        .then(async r => {
            if (!r.ok) {
                const msg = await r.text();
                throw new Error(msg || `Ошибка ${r.status}`);
            }
            // если тело есть, парсим JSON, иначе null
            const text = await r.text();
            return text ? JSON.parse(text) : null;
        })
        .then(resp => {
            console.log("Ответ сервера:", resp);
            showToast("Изменения сохранены", "success");
        })
        .catch(err => {
            console.error("Ошибка при сохранении:", err);
            showToast("Ошибка при сохранении", "error");
        });
}
/*
 * Собирает объект SoftUpdateRequest по индексу строки
 */
function collectSoftRequest(index) {
    const row = document.querySelector(`.soft-item-inline [data-index="${index}"]`) ?.closest(".soft-item-inline");
    if (!row)
        return null;

    // Извлекаем поля
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
 */
function handleDeleteSoft(el) {
    const index = el.dataset.index;
    const data = collectSoftData(index);
    if (!data) return;

    if (!confirm(`Удалить «${data.name || "элемент"}»?`)) return;
    fetch(`/api/v1/soft/${data.id}`, {
        method: "DELETE",
        headers: { "X-Requested-With": "XMLHttpRequest" }
    })
        .then(r => r.ok ? showToast("Удалено", "success") : Promise.reject(r))
        .catch(() => showToast("Ошибка при удалении", "error"));
}

/* ============================ */
/* === Вспомогательные функции === */
/* ============================ */

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

function showToast(message, type = "info") {
    const toast = document.createElement("div");
    toast.textContent = message;
    toast.className = `toast ${type}`;
    Object.assign(toast.style, {
        position: "fixed",
        bottom: "15px",
        right: "15px",
        padding: "10px 15px",
        background: type === "success" ? "#4ade80" :
            type === "error" ? "#f87171" : "#60a5fa",
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
        setTimeout(() => toast.remove(), 300);
    }, 2000);
}


/**
 * Управление изображениями
 */
/**
 * ============================================================================================================
 */
// === Управление изображениями для выбранного софта ===

function handleControlImage(el) {
    const index = el.dataset.index;
    const row = getSoftRow(index);
    const softId = row.dataset.id;
    console.log("Управление изображениями для:", softId);

    const modal = document.getElementById("softimg-modal");
    const container = document.getElementById("softimg-container");

    // Очистка содержимого
    container.innerHTML = "";
    modal.style.display = "flex";

    // Загрузка изображений для выбранного софта
    fetch(`/api/v1/admin/images-manage/${softId}`, {
        headers: {"X-Requested-With": "XMLHttpRequest"}
    })
        .then(resp => {
            if (!resp.ok)
                throw new Error(`HTTP ${resp.status}`);
            return resp.json();
        })
        .then(images => {
            renderImageGrid(images, softId);
        })
        .catch(err => {
            console.error("Ошибка загрузки изображений:", err);
            container.innerHTML = `<p style="color:red;">Ошибка загрузки изображений</p>`;
        });

    // Кнопка закрытия
    modal.querySelector(".softimg-modal-close").onclick = () => closeSoftimgModal();
    // Закрытие по клику вне контента
    modal.onclick = e => { if (e.target === modal) closeSoftimgModal(); };
}

function closeSoftimgModal() {
    const modal = document.getElementById("softimg-modal");
    modal.style.display = "none";
}

// // === Отрисовка сетки изображений ===
// function renderImageGrid(images, softId) {
//     const container = document.getElementById("softimg-container");
//     container.innerHTML = "";
//
//     images.forEach(img => {
//         const div = document.createElement("div");
//         div.classList.add("softimg-item");
//
//         const image = document.createElement("img");
//         // Добавляем query-параметр для обхода кэша
//         image.src = img.url + "?v=" + Date.now();
//         image.alt = img.filename || "image";
//         div.appendChild(image);
//
//         const delBtn = document.createElement("button");
//         delBtn.className = "softimg-btn-delete";
//         delBtn.innerHTML = "×";
//         delBtn.title = "Удалить изображение";
//         delBtn.onclick = () => deleteSoftimgImage(softId, img.filename);
//         div.appendChild(delBtn);
//
//         image.onclick = () => replaceSoftimgImage(softId, img.filename);
//
//         container.appendChild(div);
//     });
//
//     const addSlot = document.createElement("div");
//     addSlot.className = "softimg-item add-slot";
//     addSlot.title = "Добавить изображение";
//     addSlot.innerHTML = "+";
//     addSlot.onclick = () => addSoftimgImage(softId);
//     container.appendChild(addSlot);
// }

function renderImageGrid(images, softId) {
    const container = document.getElementById("softimg-container");
    container.innerHTML = "";

    // Существующие изображения
    images.forEach(img => {
        const div = document.createElement("div");
        div.classList.add("softimg-item");

        const image = document.createElement("img");
        image.src = img.url;
        image.title = "Кликните для замены изображения";
        image.alt = img.filename || "image";
        div.appendChild(image);

        // Кнопка удаления
        const delBtn = document.createElement("button");
        delBtn.className = "softimg-btn-delete";
        delBtn.innerHTML = "×";
        delBtn.title = "Удалить изображение";
        delBtn.onclick = () => deleteSoftimgImage(softId, img.filename);
        div.appendChild(delBtn);

        // Замена по клику
        image.onclick = () => replaceSoftimgImage(softId, img.filename);

        container.appendChild(div);
    });

    // Пустой слот для добавления
    const addSlot = document.createElement("div");
    addSlot.className = "softimg-item add-slot";
    addSlot.title = "Добавить изображение";
    addSlot.innerHTML = "+";
    addSlot.onclick = () => addSoftimgImage(softId);
    container.appendChild(addSlot);
}

// === Добавление нового изображения ===
function addSoftimgImage(softId) {
    const input = document.createElement("input");
    input.type = "file";
    input.accept = "image/*";
    input.onchange = () => {
        if (!input.files || input.files.length === 0) return;

        const formData = new FormData();
        formData.append("file", input.files[0]);

        fetch(`/api/v1/admin/images-manage/${softId}`, {
            method: "POST",
            body: formData,
            headers: {
                "X-Requested-With": "XMLHttpRequest"
            }
        })
            .then(resp => {
                if (!resp.ok)
                    throw new Error(`Ошибка загрузки: ${resp.status}`);
                // Тело отсутствует, просто обновляем список изображений
                handleControlImage({ dataset: { index: getRowIndexById(softId) } });
            })
            .catch(err => console.error("Ошибка добавления изображения:", err));
    };
    input.click();
}


// === Замена существующего изображения ===
function replaceSoftimgImage(softId, imageName) {
    const input = document.createElement("input");
    input.type = "file";
    input.accept = "image/*";
    input.onchange = () => {
        if (!input.files || input.files.length === 0) return;

        const formData = new FormData();
        formData.append("file", input.files[0]);

        fetch(`/api/v1/admin/images-manage/${softId}/${imageName}`, {
            method: "PUT",
            body: formData,
            headers: { "X-Requested-With": "XMLHttpRequest" }
        })
            .then(resp => {
                if (!resp.ok)
                    throw new Error(`Ошибка замены изображения: ${resp.status}`);
                // Тело отсутствует, просто обновляем список изображений
                handleControlImage({ dataset: { index: getRowIndexById(softId) } });
            })
            .catch(err => console.error("Ошибка замены изображения:", err));
    };
    input.click();
}

// === Удаление изображения ===
function deleteSoftimgImage(softId, imageName) {
    if (!confirm("Удалить изображение?")) return;

    fetch(`/api/v1/admin/images-manage/${softId}/${imageName}`, {
        method: "DELETE",
        headers: { "X-Requested-With": "XMLHttpRequest" }
    })
        .then(resp => {
            if (!resp.ok) throw new Error(`HTTP ${resp.status}`);
            // Удаляем элемент из DOM
            const imageDiv = document.querySelector(`.softimg-item img[src*="${imageName}"]`)?.closest(".softimg-item");
            if (imageDiv) imageDiv.remove();
        })
        .catch(err => console.error("Ошибка удаления изображения:", err));
}


function reloadImage(imageName) {

}

// === Вспомогательная функция для поиска индекса строки по ID ===
function getRowIndexById(softId) {
    const rows = document.querySelectorAll(".soft-item-inline");
    for (let i = 0; i < rows.length; i++) {
        if (rows[i].dataset.id === softId) return i;
    }
    return null;
}




// ------------------------
/*
Вариант B: обновлять только конкретный <img> без перерисовки сетки

Если хочешь не перерисовывать весь список:

const image = document.querySelector(`.softimg-item img[src*="${imageName}"]`);
if (image) image.src = image.src.split("?")[0] + "?v=" + Date.now()


-----------------------
Когда ты в JS после замены вызываешь, например:

fetch(`/api/v1/admin/images-manage/${softId}`, {
    headers: { "X-Requested-With": "XMLHttpRequest" }
})
    .then(r => r.json())
    .then(images => renderImageGrid(images, softId));
    
 */