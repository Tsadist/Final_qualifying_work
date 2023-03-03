<#import "parts/common.ftlh" as common>

<@common.page>
    <form method="post">
        <div class="form-group">
            <label>
                Укажите площадь помещения
                <input style="width: 100px" type="number" max="275"
                       name="area" id="area" min="1">
            </label>
        </div>
        <div class="form-group">
            <label>
                <select class="custom-select" name="roomType"
                        id="roomType" required="required">
                    <option selected>Выберите тип помещения</option>
                    <option value="Коммерческое">Коммерческое</option>
                    <option value="Жилое">Жилое</option>
                </select>
            </label>
        </div>
        <div class="form-group">
            <label>
                <select class="custom-select" name="cleaningType"
                        id="cleaningType" required="required">
                    <option selected>Выберите тип уборки</option>
                    <option value="Регулярная">Регулярная</option>
                    <option value="Генеральная">Генеральная</option>
                    <option value="После_ремонта">После ремонта</option>
                </select>
            </label>
        </div>
        <div>
            <label>
                Выберите дату
                <input type="date" name="theDate" id="theDate">
            </label>
        </div>
        <div>
            <label>
                Выберите время
                <input style="width: 100px" type="number" min="8" max="22"
                       name="startTime" id="startTime">
            </label>
        </div>
        <input name="_csrf" type="hidden" value="${_csrf.token}">
        <button class="mt-3 btn btn-md btn-primary" autofocus type="submit">
            Оформить заказ
        </button>

    </form>
</@common.page>