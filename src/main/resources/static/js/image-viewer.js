document.addEventListener("DOMContentLoaded", () => {

    // helper: вычисляет отображаемую ширину (точнее, чем clientWidth)
    const displayedWidth = (img) => img.getBoundingClientRect().width;

    // принимает img DOM-элемент, выставляет cursor в зависимости от naturalWidth
    const updateCursorForImg = (img) => {
        // если у изображения ещё нет naturalWidth (не загружено) — ничего не делаем
        if (!img.naturalWidth) {
            img.style.cursor = "default";
            return;
        }
        const natural = img.naturalWidth;
        const shown = displayedWidth(img);
        img.style.cursor = (natural > shown + 1) ? "zoom-in" : "default";
    };

    // инициализация конкретного изображения: навесить load (если нужно) и наблюдатели
    const initImage = (img, resizeObserver, resizeFallbackList) => {
        // не трогаем, если картинка внутри <a> (она может уже быть кликабельной навсегда)
        if (img.closest("a")) {
            // но всё равно можно обновить курсор (если нужно) — обычно ссылки сами управляют стилем
            if (img.complete) updateCursorForImg(img);
            else img.addEventListener("load", () => updateCursorForImg(img), { once: true });
            return;
        }

        if (img.complete) {
            updateCursorForImg(img);
        } else {
            img.addEventListener("load", () => updateCursorForImg(img), { once: true });
        }

        // ResizeObserver для отслеживания переразмеривания элемента
        if (resizeObserver) {
            resizeObserver.observe(img);
        } else {
            // fallback: добавим в список, который будем пересчитывать при window.resize
            resizeFallbackList.push(img);
        }
    };

    // Создаём ResizeObserver, если поддерживается
    let resizeObserver = null;
    const resizeFallbackList = [];
    if ("ResizeObserver" in window) {
        resizeObserver = new ResizeObserver(entries => {
            for (const entry of entries) {
                const img = entry.target;
                updateCursorForImg(img);
            }
        });
    } else {
        // fallback: при изменении окна пересчитываем курсоры для зарегистрированных картинок
        let resizeTimer = null;
        window.addEventListener("resize", () => {
            clearTimeout(resizeTimer);
            resizeTimer = setTimeout(() => {
                resizeFallbackList.forEach(img => updateCursorForImg(img));
            }, 120);
        });
    }

    // Инициализация всех текущих картинок в .doc-body
    const initAllDocBodyImages = (root = document) => {
        root.querySelectorAll(".doc-body img").forEach(img => {
            // избегаем двойной инициализации: пометим элемент атрибутом
            if (img.dataset.cursorInit) return;
            img.dataset.cursorInit = "1";
            initImage(img, resizeObserver, resizeFallbackList);
        });
    };

    initAllDocBodyImages();

    // MutationObserver — подхват новых подгруженных фрагментов (AJAX)
    const mo = new MutationObserver(mutations => {
        for (const m of mutations) {
            for (const node of m.addedNodes) {
                if (node.nodeType !== 1) continue;
                // если добавлен сам .doc-body или внутри него появились img — инициализируем
                if (node.matches && node.matches(".doc-body")) {
                    initAllDocBodyImages(node);
                } else {
                    const imgs = node.querySelectorAll && node.querySelectorAll(".doc-body img");
                    if (imgs && imgs.length) initAllDocBodyImages(node);
                }
            }
        }
    });
    mo.observe(document.body, { childList: true, subtree: true });

    // --- Поведение клика: увеличиваем только если действительно сжато ---
    // Создаём общий viewer (оверлей) один раз
    const viewer = document.createElement("div");
    viewer.className = "img-viewer";
    viewer.innerHTML = "<img>";
    document.body.appendChild(viewer);
    const viewerImg = viewer.querySelector("img");

    document.body.addEventListener("click", (e) => {
        const img = e.target.closest(".doc-body img");
        if (!img) return;

        // если img внутри <a>, лучше позволить <a> обработать клик (например, ссылка)
        if (img.closest("a")) return;

        // откроем viewer только если изображение реально уменьшено
        if (img.naturalWidth > displayedWidth(img) + 1) {
            viewerImg.src = img.src;
            viewer.style.display = "flex";
        }
    });

    // закрытие viewer
    viewer.addEventListener("click", () => viewer.style.display = "none");
    document.addEventListener("keydown", (e) => {
        if (e.key === "Escape") viewer.style.display = "none";
    });

    // для safety: при загрузке изображений, которые были отмечены в fallback list, убедимся в их учёте
    // (если ResizeObserver не поддерживается, добавляем уже существующие .doc-body img в fallbackList)
    if (!resizeObserver) {
        document.querySelectorAll(".doc-body img").forEach(img => {
            if (!resizeFallbackList.includes(img)) resizeFallbackList.push(img);
        });
    }
});


/*
fallback для :has() (для более старых браузеров)
 */
document.addEventListener("DOMContentLoaded", () => {
    // Проверка поддержки :has() в CSS
    let hasSupport = false;
    try {
        hasSupport = CSS.supports("selector(:has(*))");
    } catch {
        hasSupport = false;
    }

    if (!hasSupport) {
        console.warn("[image-layout] :has() не поддерживается — используется fallback на JS.");

        document.querySelectorAll(".doc-body p").forEach(p => {
            const imgs = p.querySelectorAll("img");
            if (imgs.length >= 2) {
                p.classList.add("image-row");
            }
        });
    }
});
