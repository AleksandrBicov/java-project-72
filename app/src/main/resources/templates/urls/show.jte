@import hexlet.code.dto.url.UrlPage
@import hexlet.code.model.UrlCheck
@import hexlet.code.utils.DateUtils
@param UrlPage page
@template.layout.page(
page = page,
    content = @`
    <main class="flex-grow-1">
        <section>
            <div class="container-lg mt-5">
                <h1>Сайт: ${page.getUrl().getName()}</h1>

                <table class="table table-bordered table-hover mt-3">
                    <tbody>
                        <tr>
                            <td>ID</td>
                            <td>${page.getUrl().getId()}</td>
                        </tr>
                        <tr>
                            <td>Name</td>
                            <td>${page.getUrl().getName()}</td>
                        </tr>
                        <tr>
                            <td>Дата создания</td>
                            <td>${DateUtils.getFormattedCreatedAt(page.getUrl().getCreatedAt())}</td>
                        </tr>
                    </tbody>
                </table>

            </div>

        <h2 class="mt-5">Проверки</h2>
        <form method="post" action="/urls/${page.getUrl().getId()}/checks">
            <button type="submit" class="btn btn-primary">Запустить проверку</button>
        </form>

        <table class="table table-bordered table-hover mt-3">
            <thead>
                <th class="col-1">ID</th>
                <th class="col-1">Код ответа</th>
                <th>title</th>
                <th>h1</th>
                <th>description</th>
                <th class="col-2">Дата проверки</th>
            </thead>
            <tbody>
            @if (page.getUrlCheck() != null)
            @for (UrlCheck check : page.getUrlCheck())
            <tr>
                <td>${check.getId()}</td>
                <td>${check.getStatusCode()}</td>
                <td>${check.getTitle()}</td>
                <td>${check.getH1()}</td>
                <td>${check.getDescription()}</td>
                <td>${DateUtils.getFormattedCreatedAt(check.getCreatedAt())}</td>
            </tr>
            @endfor
            @endif
            </tbody>

        </table>
        </section>
    </main>
    `
)