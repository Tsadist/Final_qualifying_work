<#import "parts/common.ftlh" as common>

<@common.page>
    <a class="btn btn-primary btn-lg mb-4" href="/profileManager">
        Вернуться к списку сотрудников
    </a>
    <h5>
        Новый сотрудник
        <div class="calendar-wrapper">
            <button id="btnPrev" type="button">Предыдущий</button>
            <button id="btnNext" type="button">Следующий</button>
            <div id="divCal"></div>
        </div>
        <script>
            var myDays = [];
        </script>
        <script src="js/index.js"></script>
    </h5>
</@common.page>