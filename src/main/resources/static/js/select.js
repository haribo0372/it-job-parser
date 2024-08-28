document.addEventListener('DOMContentLoaded', function() {
    var selectElement = document.getElementById('selectSkill');
    selectElement.addEventListener('mousedown', function(e) {
        e.preventDefault();
        var option = e.target;
        if (option.tagName === 'OPTION') {
            if (option.selected) {
                option.selected = false;
            } else {
                option.selected = true;
            }
        }
    });
});

function addToFavorites(button) {
    var vacancyId = button.getAttribute("data-vacancy-id");
    let url = `/favorite/add/${vacancyId}`;
    console.log(url);
    $.ajax({
        url: url,
        type: "GET",
        data: { vacancyId: vacancyId },
        success: function(response) {
            console.log("Вакансия добавлена в избранное {" + vacancyId + "}");
            $(button).removeClass('add-to-favorites').addClass('remove-from-favorites').text('Удалить из избранного');
            $(button).attr('onclick', 'removeFromFavorites(this)');
        },
        error: function(xhr, status, error) {
            console.error("Ошибка при добавлении в избранное: " + error);
        }
    });
}

function removeFromFavorites(button) {
    var vacancyId = button.getAttribute("data-vacancy-id");
    let url = `/favorite/remove/${vacancyId}`;
    console.log(url);
    $.ajax({
        url: url,
        type: "GET",
        data: { vacancyId: vacancyId },
        success: function(response) {
            console.log("Вакансия удалена из избранного {" + vacancyId + "}");
            $(button).removeClass('remove-from-favorites').addClass('add-to-favorites').text('Добавить в избранное');
            $(button).attr('onclick', 'addToFavorites(this)');
        },
        error: function(xhr, status, error) {
            console.error("Ошибка при удалении из избранного: " + error);
        }
    });
}

function searchSkills() {
    var input, filter, select, options, option, i, txtValue;
    input = document.getElementById('skillSearch');
    filter = input.value.toUpperCase();
    select = document.getElementById('selectSkill');
    options = select.getElementsByTagName('option');
    for (i = 0; i < options.length; i++) {
        option = options[i];
        txtValue = option.textContent || option.innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
            option.style.display = "";
        } else {
            option.style.display = "none";
        }
    }
}

