const mealAjaxUrl = "profile/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl,
    updateTable: function () {
        $.ajax({
            type: "GET",
            url: mealAjaxUrl + "filter",
            data: $("#filter").serialize()
        }).done(updateTableByData);
    }
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            language: {
                // Интернационализация
                // https://datatables.net/manual/i18n

                // Вариант 1. Прямо здесь по ключевым фразам:
                // search: "Поиск:",

                // Вариант 2. По ссылке:
                // https://datatables.net/plug-ins/i18n/Russian.html
                url: i18n['common.i18nForDataTables']
            },
            "ajax": {
                "url": mealAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (date, type) {
                        if (type === "display") {
                            return date.substring(0, 10) + " " + date.substring(11, 16);
                        }
                        return date;
                    }
                    /*
                    , // Была ещё такая попытка:
                    "render": DataTable.render.datetime('YYYY-MM-DD HH:MI')
                    */
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "",
                    "orderable": false,
                    "render": renderEditBtn
                },
                {
                    "defaultContent": "",
                    "orderable": false,
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            // При формировании <tr> нужно дополнить его
            // так: <tr data-meal-excess="true">
            // или так: <tr data-meal-excess="false">
            "createdRow": function (row, data) {
                $(row).attr("data-meal-excess", data.excess);
            }

        })
    );
});