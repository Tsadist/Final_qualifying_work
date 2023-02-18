
<#import "parts/common.ftlh" as common>

<@common.page>
    <form method="post">
        <p>Итоговая стоимость: ${cost} р</p>
        <div class="form-group">
            <label>
                Введите номер карты
                <input style="width: 200px" type="number" max="9999999999999999"
                       name="cardNumber" id="cardNumber">
            </label>
        </div>
        <div class="form-group">
            <label>
                Укажите срок действия
                <input type="month" name="monthAndYear" id="monthAndYear">
            </label>
        </div>
        <input name="_csrf" type="hidden" value="${_csrf.token}">
        <button class="mt-3 btn btn-md btn-primary" autofocus type="submit">
            Оплатить
        </button>
    </form>
</@common.page>