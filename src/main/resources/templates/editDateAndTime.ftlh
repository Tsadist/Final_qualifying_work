
<#import "parts/common.ftlh" as common>

<@common.page>
<form method="post">
    <div>
        <label>
            Выберите дату
            <input type="date" name="theDate" id="theDate">
        </label>
    </div>
    <div>
        <label>
            Выберите время
            <input style="width: 100px" type="number" min="8" max="22" name="startTime" id="startTime">
        </label>
    </div>
    <input name="_csrf" type="hidden" value="${_csrf.token}">
    <input name="order_id" type="hidden" value="${order.id}">
    <button class="mt-3 btn btn-md btn-primary" autofocus type="submit">Внести изменения</button>
</form>
</@common.page>