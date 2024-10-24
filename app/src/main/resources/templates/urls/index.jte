@import hexlet.code.dto.url.UrlsPage
@param UrlsPage page

@template.layout.page(
page = page,
    content = @`
    <H1>Сайты</H1>
       <table class="table table-striped">
            @for(var url : page.getUrls())
                <tr>
                    <td>
                        ${url.getId()}
                    </td>
                    <td>
                        <a href="/urls/${url.getId()}" target="_blank">${url.getName()}</a>
                    </td>
                </tr>
            @endfor
        </table>
    `
)