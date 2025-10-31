/* ============================ soft-list-edit.js ============================ */

// Расширяем глобальный объект actions
document.addEventListener("DOMContentLoaded", () => {
    if (window.actions) {
        Object.assign(window.actions, {
            "add-image": handleAddImage,
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
 * Добавить изображения
 */
function handleAddImage(el) {
    const index = el.dataset.index;
    const row = getSoftRow(index);
    console.log("Добавление изображений для:", row);
    // TODO: открыть модальное окно
}

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
        headers: { "Content-Type": "application/json" },
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
    fetch(`/api/v1/soft/${data.id}`, { method: "DELETE" })
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
