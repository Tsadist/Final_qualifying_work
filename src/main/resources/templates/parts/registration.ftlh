<#macro registration path isRegisterForm>
    <#import "common.ftlh" as commom>

    <@commom.page>
        <div class="container">
            <form class="form-signin" action="${path}" method="post">
                <h2 class="form-signin-heading">
                    <#if !isRegisterForm>
                        Войти
                    <#else>
                        Регистрация
                    </#if>
                </h2>

                <#if Session.SPRING_SECURITY_LAST_EXCEPTION??>
                    <div style="margin-bottom: 10px">Вы ввели неправльный пароль или Email</div>
                </#if>

<#--                <#if Session.SPRING_SECURITY_LAST_EXCEPTION?? && Session.SPRING_SECURITY_LAST_EXCEPTION.message?has_content>-->
<#--                    <@spring.message "Вы ввели неправльный логин или пароль"/>-->
<#--                </#if>-->

                <p>
                    <label for="email" class="sr-only">Email</label>
                    <input type="text" id="email" required
                           name="username" class="form-control" placeholder="Email">
                </p>
                <p>
                    <label for="password" class="sr-only">Password</label>
                    <input type="password" id="password" required
                           name="password" class="form-control"
                           placeholder="Password">
                </p>
                <#if isRegisterForm>
                    <p>
                        <label for="fio" class="sr-only">ФИО</label>
                        <input type="text" id="fio" required
                               name="fio" class="form-control"
                               placeholder="ФИО">
                    </p>
                </#if>
                <input name="_csrf" type="hidden" value="${_csrf.token}">
                <#if isRegisterForm>
                    <button class="btn btn-lg btn-success btn-lg" autofocus type="submit">
                        Зарегистрироваться
                    </button>
                <#else>
                    <button class="btn btn-lg btn-success btn-lg" autofocus type="submit">
                        Войти
                    </button>
                    <a class="btn btn-lg btn-primary btn-lg" href="/registration">
                        Зарегистрироваться
                    </a>
                </#if>
            </form>
        </div>
    </@commom.page>
</#macro>

<#macro logout>
    <form action="/logout" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button class="btn btn-primary" type="submit">Выйти</button>
    </form>
</#macro>