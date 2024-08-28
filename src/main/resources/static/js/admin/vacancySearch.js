function filterID() {
    var input, filter, table, tr, td, i, txtValue;
    input = document.getElementById("searchId");
    filter = input.value.toUpperCase();
    table = document.getElementById("table");
    tr = table.getElementsByTagName("tr");

    // Проходим по всем строкам таблицы и скрываем те, которые не соответствуют поисковому запросу
    for (i = 1; i < tr.length; i++) { // начинаем с 1, чтобы пропустить заголовок таблицы
        td = tr[i].getElementsByTagName("th")[0];
        if (td) {
            txtValue = td.textContent || td.innerText;
            if (txtValue.toUpperCase().indexOf(filter) > -1) {
                tr[i].style.display = "";
            } else {
                tr[i].style.display = "none";
            }
        }
    }
}

function filterCity() {
    var input, filter, table, tr, td, i, txtValue;
    input = document.getElementById("searchCity");
    filter = input.value.toUpperCase();
    table = document.getElementById("table");
    tr = table.getElementsByTagName("tr");

    // Проходим по всем строкам таблицы и скрываем те, которые не соответствуют поисковому запросу
    for (i = 1; i < tr.length; i++) { // начинаем с 1, чтобы пропустить заголовок таблицы
        td = tr[i].getElementsByTagName("td")[0];
        if (td) {
            txtValue = td.textContent || td.innerText;
            if (txtValue.toUpperCase().indexOf(filter) > -1) {
                tr[i].style.display = "";
            } else {
                tr[i].style.display = "none";
            }
        }
    }
}

function filterCompany() {
    var input, filter, table, tr, td, i, txtValue;
    input = document.getElementById("searchCompany");
    filter = input.value.toUpperCase();
    table = document.getElementById("table");
    tr = table.getElementsByTagName("tr");

    // Проходим по всем строкам таблицы и скрываем те, которые не соответствуют поисковому запросу
    for (i = 1; i < tr.length; i++) { // начинаем с 1, чтобы пропустить заголовок таблицы
        td = tr[i].getElementsByTagName("td")[1];
        if (td) {
            txtValue = td.textContent || td.innerText;
            if (txtValue.toUpperCase().indexOf(filter) > -1) {
                tr[i].style.display = "";
            } else {
                tr[i].style.display = "none";
            }
        }
    }
}

function filterWage() {
    var input, filter, table, tr, td, i, txtValue;
    input = document.getElementById("searchWage");
    filter = input.value.toUpperCase();
    table = document.getElementById("table");
    tr = table.getElementsByTagName("tr");

    // Проходим по всем строкам таблицы и скрываем те, которые не соответствуют поисковому запросу
    for (i = 1; i < tr.length; i++) { // начинаем с 1, чтобы пропустить заголовок таблицы
        td = tr[i].getElementsByTagName("td")[2];
        if (td) {
            txtValue = td.textContent || td.innerText;
            if (txtValue.toUpperCase().indexOf(filter) > -1) {
                tr[i].style.display = "";
            } else {
                tr[i].style.display = "none";
            }
        }
    }
}

function filterProfessions() {
    var input, filter, table, tr, td, i, txtValue;
    input = document.getElementById("searchProfession");
    filter = input.value.toUpperCase();
    table = document.getElementById("table");
    tr = table.getElementsByTagName("tr");

    // Проходим по всем строкам таблицы и скрываем те, которые не соответствуют поисковому запросу
    for (i = 1; i < tr.length; i++) { // начинаем с 1, чтобы пропустить заголовок таблицы
        td = tr[i].getElementsByTagName("td")[3];
        if (td) {
            txtValue = td.textContent || td.innerText;
            if (txtValue.toUpperCase().indexOf(filter) > -1) {
                tr[i].style.display = "";
            } else {
                tr[i].style.display = "none";
            }
        }
    }
}

function filterTitle() {
    var input, filter, table, tr, td, i, txtValue;
    input = document.getElementById("searchTitle");
    filter = input.value.toUpperCase();
    table = document.getElementById("table");
    tr = table.getElementsByTagName("tr");

    // Проходим по всем строкам таблицы и скрываем те, которые не соответствуют поисковому запросу
    for (i = 1; i < tr.length; i++) { // начинаем с 1, чтобы пропустить заголовок таблицы
        td = tr[i].getElementsByTagName("td")[4];
        if (td) {
            txtValue = td.textContent || td.innerText;
            if (txtValue.toUpperCase().indexOf(filter) > -1) {
                tr[i].style.display = "";
            } else {
                tr[i].style.display = "none";
            }
        }
    }
}

function filterSkill() {
    var input, filter, table, tr, td, i, txtValue;
    input = document.getElementById("searchSkill");
    filter = input.value.toUpperCase();
    table = document.getElementById("table");
    tr = table.getElementsByTagName("tr");

    // Проходим по всем строкам таблицы и скрываем те, которые не соответствуют поисковому запросу
    for (i = 1; i < tr.length; i++) { // начинаем с 1, чтобы пропустить заголовок таблицы
        td = tr[i].getElementsByTagName("td")[6];
        if (td) {
            txtValue = td.textContent || td.innerText;
            if (txtValue.toUpperCase().indexOf(filter) > -1) {
                tr[i].style.display = "";
            } else {
                tr[i].style.display = "none";
            }
        }
    }
}


