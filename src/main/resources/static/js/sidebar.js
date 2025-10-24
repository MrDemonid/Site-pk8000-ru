// ========================== sidebar.js ==========================


/*
Добавляет в адресную строку параметр со строкой из поля поиска (фильтра)
 */
function appendSearchParam(url) {
    const activeLink = document.querySelector('.menu a.active[data-search-id]');
    if (!activeLink) return url;

    const searchId = activeLink.dataset.searchId;
    const searchInput = document.getElementById(searchId);

    if (searchId && searchInput) {
        const query = searchInput.value.trim();
        if (query) {
            url.searchParams.set(searchId, query);
            url.searchParams.set("page", 0);     // сброс номера страницы при новом запросе
        } else {
            url.searchParams.delete(searchId);
        }

    }
    return url;
}


/*
Синхронизация полей из url/хранилища.
 */
function syncSearchInputs() {
    const url = new URL(window.location.href);
    const saved = JSON.parse(sessionStorage.getItem('searchValues') || '{}');

    document.querySelectorAll('.menu a[data-search-id]').forEach(linkEl => {
        const searchId = linkEl.dataset.searchId;
        if (!searchId) return;

        const input = document.getElementById(searchId);
        if (!input) return;

        // Приоритет: URL ? saved ? ''
        const fromUrl = url.searchParams.get(searchId);
        input.value = (fromUrl !== null ? fromUrl : (saved[searchId] ?? ''));
    });
}


function loadContentAjax(anchor) {
    let href = anchor.getAttribute('href');
    const searchId = anchor.dataset.searchId;

    if (!href || href === 'javascript:void(0)') {
        console.warn("loadContentAjax(): нет href");
        return true;
    }
    url = new URL(href, window.location.origin);

    // сбрасываем активность других пунктов меню
    document.querySelectorAll('.menu a.active')
        .forEach(el => el.classList.remove('active'));
    anchor.classList.add('active');

    // сохраняем/восстанавливаем параметры поиска
    const searchValues = JSON.parse(sessionStorage.getItem('searchValues') || '{}');
    if (searchId) {
        const input = document.getElementById(searchId);
        if (input) {
            const q = input.value.trim();
            if (q) {
                url.searchParams.set(searchId, q);
                searchValues[searchId] = q;
            } else {
                url.searchParams.delete(searchId);
                delete searchValues[searchId];
            }
        }
    }
    // добавляем текущие параметры пагинации
    const currentUrl = new URL(window.location.href);
    const currentSize = currentUrl.searchParams.get("size");
    if (currentSize)
        url.searchParams.set("size", currentSize);


    sessionStorage.setItem('searchValues', JSON.stringify(searchValues));
    // сохраняем активный пункт меню
    sessionStorage.setItem('activeMenuUrl', url.pathname + url.search);

    console.log("loadContentAjax() = ", url.pathname + url.search);
    // делаем AJAX-запрос
    fetch(url.pathname + url.search, { headers: { 'X-Requested-With': 'XMLHttpRequest' } })
        .then(r => r.text())
        .then(html => {
            document.getElementById('content').innerHTML = html;
            // !!!!!!
            restoreActiveMenu();
            history.pushState(null, '', url);
            window.scrollTo(0, 0);
        })
        .catch(err => console.error('Ошибка при загрузке страницы: ', err));

    return false;
}


function toggleSubmenu(anchor) {
    const submenu = anchor.nextElementSibling;
    if (submenu && submenu.classList.contains('submenu')) {
        submenu.classList.toggle('open');
        anchor.classList.toggle('expanded');
    }
}


/*
    Меню выбора размера страниц.
 */
function toggleDropdownMenu() {
    const menu = document.getElementById('pageSizeMenu');
    menu.classList.toggle('open');
}

// Закрытие меню при клике вне.
document.addEventListener('click', function (e) {
    const dropdown = document.getElementById('pageSizeDropdown');
    const menu = document.getElementById('pageSizeMenu');

    // если dropdown или menu не существуют — просто выходим
    if (!dropdown || !menu) return;

    if (!dropdown.contains(e.target)) {
        menu.classList.remove('open');
    }
});

function changePageSizeValue(size) {
    let url = new URL(window.location.href);
    url.searchParams.set("size", size);
    url.searchParams.set("page", 0);
    url = appendSearchParam(url); // добавляем поиск
    history.pushState({}, "", url);
    loadPage(url);

    // Закрытие меню после выбора.
    document.getElementById('pageSizeMenu').classList.remove('open');
}

/*
    Переход на страницу.
 */
function goToPage(pageNumber) {
    console.log("goToPage(", pageNumber, ")");
    let url = new URL(window.location.href);
    url.searchParams.set("page", pageNumber);
    url = appendSearchParam(url); // добавляем пои
    history.pushState({}, "", url);
    loadPage(url);
}

function loadPage(url) {
    console.log("loadPage(). url = " + url);
    url = appendSearchParam(url);
    console.log("loadPage(). url.path = " + url.pathname + ", url.search = " + url.search);
    // каждое «логичное» изменение URL — поддерживаем в sessionStorage
    sessionStorage.setItem('activeMenuUrl', url.pathname + url.search);

    fetch(url.pathname + url.search, {
        headers: { 'X-Requested-With': 'XMLHttpRequest' }
    })
        .then(response => response.text())
        .then(html => {
            document.getElementById("content").innerHTML = html;
            restoreActiveMenu();
            window.scrollTo(0, 0);
        });
}


/**
 * Восстановление меню при обновлении (перезагрузке) страницы.
 */
function restoreActiveMenu() {
    syncSearchInputs(); // сначала восстановим значения поисков

    const savedUrl = sessionStorage.getItem('activeMenuUrl') || null;
    if (!savedUrl)
        return;

    console.log("restoreActiveMenu(): do restore menu to: " + savedUrl);

    const saved = new URL(savedUrl, window.location.origin);


    // извлекает из полного url только адрес (pathname)
    const normalizeHref = (href) => {
        try {
            const u = new URL(href, window.location.origin);
            return decodeURIComponent(u.pathname);
        } catch (e) {
            // если href не валидный - возвращаем как есть (попробуем вручную убрать параметры адреса)
            return decodeURIComponent((href || '').split('?')[0].split('#')[0]);
        }
    };

    const savedPathname = normalizeHref(savedUrl);

    // ищем activeMenuUrl в меню
    const links = Array.from(document.querySelectorAll('.menu a'));
    const activeLink = links.find(a => normalizeHref(a.getAttribute('href')) === savedPathname);

    if (!activeLink) {
        console.log("restoreActiveMenu(): no matching menu link found for savedUrl" + savedUrl);
        return;
    }
    console.log("restoreActiveMenu(): matched link:", activeLink);

    // убираем старую "подсветку" и задаем новую
    document.querySelectorAll('.menu a.active').forEach(el => el.classList.remove('active'));
    activeLink.classList.add('active');

    // раскрываем родителей
    let parent = activeLink.parentElement;
    while (parent) {
        if (parent.classList.contains('submenu')) {
            parent.classList.add('open');
            const toggler = parent.previousElementSibling;
            if (toggler && toggler.classList.contains('has-children')) {
                toggler.classList.add('expanded');
            }
        }
        parent = parent.parentElement;
    }
}



/**
 * Отображение ошибки, полученной от ExceptionController
 * @param {Object} errorResponse - объект ErrorResponse
 */
function showError(errorResponse) {
    // Простейший вариант: alert
    alert(`Ошибка ${errorResponse.status}: ${errorResponse.message}`);
    console.error('Ошибка сервера:', errorResponse);
}

// ==================== обработчик ссылок внутри #content ====================

document.getElementById('content').addEventListener('click', function(e) {
    let target = e.target;
    while (target && target.tagName !== 'A') target = target.parentElement;
    if (!target) return;

    const href = target.getAttribute('href');
    if (!href)
        return;

    if (href.startsWith('/api/v1/page')) {
        e.preventDefault();
        // Для ссылок внутри контента вызываем отдельный AJAX, можно переиспользовать loadPage()
        loadPage(new URL(href, window.location.origin));
    }
});

