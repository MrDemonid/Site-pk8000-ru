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

