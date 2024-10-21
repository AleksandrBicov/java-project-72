@import hexlet.code.dto.url.UrlsPage
@param UrlsPage page

@template.layout.page(
    content = @`
    <H1>Адреса</H1>
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