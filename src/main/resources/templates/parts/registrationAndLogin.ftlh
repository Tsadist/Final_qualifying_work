<#macro form path isRegisterForm>
    <#import "common.ftlh" as commom>

    <@commom.page>
        <div class="container">
            <form class="form-signin" action="${path}" method="post">
                <h2 class="form-signin-heading">
                    <#if isRegisterForm>
                        Регистрация
                    <#else>
                        Войти
                    </#if>
                </h2>

                <#if !isRegisterForm>
                    <#if Session.SPRING_SECURITY_LAST_EXCEPTION??>
                        <div style="margin-bottom: 10px">Вы ввели неправльный пароль или Email</div>
                    </#if>
                </#if>

                <#--                <#if Session.SPRING_SECURITY_LAST_EXCEPTION?? && Session.SPRING_SECURITY_LAST_EXCEPTION.message?has_content>-->
                <#--                    <@spring.message "Вы ввели неправльный логин или пароль"/>-->
                <#--                </#if>-->

                <p>
                    <label for="email" class="sr-only">Email</label>
                    <input type="text" id="email" required
                            <#if isRegisterForm>
                                name="email"
                            <#else >
                                name="username"
                            </#if>
                           class="form-control" placeholder="Email">
                </p>
                <p>
                    <label for="password" class="sr-only">Password</label>
                    <input type="password" id="password" required
                           name="password" class="form-control"
                           placeholder="Password">
                </p>
                <#if isRegisterForm>
                    <p>
                        <label for="phoneNumber" class="sr-only">PhoneNumber</label>
                        <input type="text" id="phoneNumber" required
                               name="phoneNumber" class="form-control"
                               placeholder="PhoneNumber">
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