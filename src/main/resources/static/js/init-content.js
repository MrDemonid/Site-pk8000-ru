document.addEventListener('DOMContentLoaded', () => {
    const nav = performance.getEntriesByType("navigation")[0];
    const isReload = nav && nav.type === "reload"; // true только если F5/Ctrl+R

    const contentDiv = document.getElementById('content');
    const initialPath = contentDiv.dataset.initialPath; // например: /api/v1/soft/system
    const savedUrl = sessionStorage.getItem('activeMenuUrl') || null; // например: /api/v1/soft/system?soft-search=asss

    let urlToLoad = initialPath;

    if (savedUrl) {
        const uSaved = new URL(savedUrl, window.location.origin);
        const uInit  = new URL(initialPath, window.location.origin);
        if (uSaved.pathname === uInit.pathname) {
            urlToLoad = uSaved.pathname + uSaved.search; // берём сохранённый query
        }
    }

    if (urlToLoad) {
        fetch(urlToLoad, { headers: { 'X-Requested-With': 'XMLHttpRequest' } })
            .then(res => res.text())
            .then(html => {
                contentDiv.innerHTML = html;
                history.replaceState(null, '', urlToLoad); // НЕ теряем query
                if (isReload) {             // восстановление только при перезагрузке страницы
                    restoreActiveMenu();
                }
            })
            .catch(err => {
                contentDiv.innerHTML = '<p style="color:red;">Ошибка загрузки страницы.</p>';
                console.error(err);
            });
    } else {
        // На всякий случай
        if (isReload) {
            restoreActiveMenu();            // восстановление только при перезагрузке страницы
        }
    }
});

window.addEventListener('popstate', () => {
    // синхронизируем инпуты с текущим URL
    syncSearchInputs();
    // сохраняем актуальный URL (например, если пришли из истории)
    const u = new URL(window.location.href);
    sessionStorage.setItem('activeMenuUrl', u.pathname + u.search);

    // подкрашиваем меню и раскрываем родителей
    restoreActiveMenu();

    // и подгружаем контент
    const urlWithSearch = appendSearchParam(u);
    loadPage(urlWithSearch);
});
