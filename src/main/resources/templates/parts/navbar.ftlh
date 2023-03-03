
<nav class="navbar navbar-expand-lg navbar-light bt-light">
    <a class="navbar-brand" href="/">Клининг</a>
    <button class="navbar-toggler" type="button"
            data-toggle="collapse" data-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent"
            aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>


    <div class="collapse navbar-collapse" id="navbarSupportedContent">
<#--        <#if isCustomer>-->
            <ul class="navbar-nav mr-auto">
                <li class="nav-item mb-3 mt-2">
                    <a class="btn btn-outline-danger nav-link d-inline" href="/orderPage">Сделать заказ</a>
                </li>
            </ul>
<#--        </#if>-->
        <form action="/logout" method="post">
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button class="btn btn-primary" type="submit">Выйти</button>
        </form>
    </div>
</nav>
