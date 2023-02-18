
<#import "parts/common.ftlh" as common>

<@common.page>
    <div class="card-columns">
        <#if orders?size=0>
            <p>У вас еще нет заказов</p>
        <#else>
            <#list orders as order>
                <div class="card">
                    <div class="card-body">
                        <p><b>Площадь:</b> ${order.area!} м<sub>2</sub></p>
                        <p><b>Тип помещения: </b>${order.roomType!}</p>
                        <p><b>Тип уборки: </b>${order.cleaningType!}</p>
                        <p><b>Дата: </b>${order.theDate!}</p>
                        <p><b>Время: </b>${order.startTime!}:00</p>
                        <p><b>Сотрудник: </b>
                            <span>
                                <#if !(order.employee.name)??>
                                    <text class="text-danger"> Измените дату или время заказа</text>
                                <#else>
                                    ${order.employee.name}, ${order.employee.phoneNumber}
                                </#if>
                            </span>
                        </p>
                        <p><b>Стоимость: </b> ${order.cost!} р</p>
                        <p><b>Предполагаемая длительность уборки: </b>${time.numberToTime(order.duration)!}</p>
                        <p><b>Статус заказа:</b>
                            <span>
                            <#if (order.orderStatus) == "Оплачен">
                                <b><text class="text-success"> ${order.orderStatus}</text></b>
                            <#else>
                                ${order.orderStatus!}
                            </#if>
                            </span>
                        </p>
                        <input name="_csrf" type="hidden" value="${_csrf.token}">
                        <#if (order.orderStatus) == "Ждет_оплаты">
                            <a class="btn btn-md btn-success" href="/paymentForm?id=${order.id}">
                                Оплатить
                            </a>
                        </#if>
                        <#if !(order.employee.name)??>
                            <a class="btn btn-md btn-danger" href="/editDateAndTime?id=${order.id}">
                                Изменить заказ
                            </a>
                        </#if>
                    </div>
                </div>
            </#list>
        </#if>
    </div>
</@common.page>