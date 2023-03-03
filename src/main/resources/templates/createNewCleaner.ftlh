<#import "parts/common.ftlh" as common>

<@common.page>
    <div class="container">
        <form class="form-signin" action="/createNewCleaner" method="post">
            <p>
                <label for="email" class="sr-only">Email</label>
                <input type="text" id="email" required
                       name="email" class="form-control" placeholder="Email">
            </p>
            <p>
                <label for="name" class="sr-only">Name</label>
                <input type="text" id="name" required
                       name="name" class="form-control" placeholder="Name">
            </p>
            <p>
                <label for="surname" class="sr-only">Surname</label>
                <input type="text" id="surname" required
                       name="surname" class="form-control" placeholder="Surname">
            </p>
            <p>
                <label for="phoneNumber" class="sr-only">PhoneNumber</label>
                <input type="text" id="phoneNumber" required
                       name="phoneNumber" class="form-control" placeholder="PhoneNumber">
            </p>

            <input name="_csrf" type="hidden" value="${_csrf.token}">
            <button class="btn btn-lg btn-success btn-lg" autofocus type="submit">
                Создать
            </button>
        </form>
    </div>
</@common.page>