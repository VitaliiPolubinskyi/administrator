document.addEventListener("DOMContentLoaded", function() {
    var today = new Date();
    var formattedDate = today.toISOString().slice(0, 10);

    var dateIn = document.getElementById("in");
    var dateOut = document.getElementById("out");

    // Устанавливаем атрибут "min" для dateIn на сегодняшнюю дату
    dateIn.setAttribute("min", formattedDate);

    dateIn.addEventListener("input", function() {
        // При выборе даты в dateIn, устанавливаем минимальную дату для dateOut
        var selectedDate1 = new Date(dateIn.value);
        var minDate2 = new Date(selectedDate1);
        minDate2.setDate(selectedDate1.getDate()); // Добавляем один день
        dateOut.setAttribute("min", minDate2.toISOString().slice(0, 10));
    });

    // Создаем новую дату
    var tomorrow = new Date(today);
    tomorrow.setDate(today.getDate());

    // Преобразуем дату в строку и устанавливаем атрибут "min" для dateOut
    var formattedTomorrow = tomorrow.toISOString().slice(0, 10);
    dateOut.setAttribute("min", formattedTomorrow);

    dateOut.addEventListener("input", function() {
         // При выборе даты в dateOut, устанавливаем максимальную дату для dateIn
         var selectedDate2 = new Date(dateOut.value);
         var maxDate1 = new Date(selectedDate2);
         maxDate1.setDate(selectedDate2.getDate()); // Вычитаем один день
         dateIn.setAttribute("max", maxDate1.toISOString().slice(0, 10));
    });

// не працює
    // Добавляем обработчик события change для dateIn
    dateIn.addEventListener("change", function() {
        // Если значение dateIn пусто, восстанавливаем минимальную дату
        if (!dateIn.value) {
            dateIn.setAttribute("min", formattedDate);
            dateOut.setAttribute("min", formattedTomorrow);
        }
    });

    // Добавляем обработчик события change для dateOut
    dateOut.addEventListener("change", function() {
        // Если значение dateOut пусто, восстанавливаем минимальную дату
        if (!dateOut.value) {
            dateOut.setAttribute("min", formattedTomorrow);
            dateIn.setAttribute("min", formattedDate);
        }
    });

});


