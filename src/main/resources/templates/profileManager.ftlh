<#import "parts/common.ftlh" as common>

<@common.page>

    <h2 class="text-center">Список сотрдников</h2>

    <a class="btn btn-secondary btn-lg" href="/createNewCleaner">
        Добавить нового сотрудника
    </a>

    <table class="table table-striped mt-4">
        <tbody>
        <#list cleanerList as cleaner>
            <tr>

                <th scope="row">
                    <a href="/cleaner?id=${cleaner.id}">
                        <h5>${cleaner.name} ${cleaner.surname}</h5>
                    </a>
                </th>
            </tr>
        </#list>
        </tbody>
    </table>


</@common.page>

