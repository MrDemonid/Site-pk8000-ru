// ========================== init-content.js ==========================
document.addEventListener('DOMContentLoaded', () => {
    const nav = performance.getEntriesByType("navigation")[0];
    const isReload = nav && nav.type === "reload"; // true только если F5/Ctrl+R

    if (!isReload) {
        // это новый визит на сайт, удаляем возможные сохраненные сессии с предыдущего визита
        console.log("init-content(). new start, clean sessionStorage");
        sessionStorage.removeItem('activeMenuUrl');
        sessionStorage.removeItem('searchValues');
    }

    const contentDiv = document.getElementById('content');
    const initialPath = contentDiv.dataset.initialPath;
    const savedUrl = sessionStorage.getItem('activeMenuUrl') || null;

    let urlToLoad = initialPath;

    console.log("init-content(): initialPath: " + urlToLoad);

    if (savedUrl) {
        const uSaved = new URL(savedUrl, window.location.origin);
        const uInit  = new URL(initialPath, window.location.origin);
        if (uSaved.pathname === uInit.pathname) {
            urlToLoad = uSaved.pathname + uSaved.search; // берём сохранённый query
        }
    }
    console.log("init-content(): urlToLoad: " + urlToLoad);

    if (urlToLoad) {
        fetch(urlToLoad, { headers: { 'X-Requested-With': 'XMLHttpRequest' } })
            .then(res => res.text())
            .then(html => {
                contentDiv.innerHTML = html;
                history.replaceState(null, '', urlToLoad); // НЕ теряем query

                syncSearchInputs();

                if (!sessionStorage.getItem('activeMenuUrl')) {
                    const u = new URL(urlToLoad, window.location.origin);
                    sessionStorage.setItem('activeMenuUrl', u.pathname + u.search);
                }

                restoreActiveMenu();
            })
            .catch(err => {
                contentDiv.innerHTML = '<p style="color:red;">Ошибка загрузки страницы.</p>';
                console.error(err);
            });
    } else {
        // На всякий случай
        restoreActiveMenu();
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
