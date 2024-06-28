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
console.log("GG AmUU")