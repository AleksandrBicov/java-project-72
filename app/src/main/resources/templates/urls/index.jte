@import hexlet.code.dto.url.UrlsPage
@param UsersPage page

@template.layout.page(
    content = @`
       <table class="table table-striped">
            @for(var url : page.getUrls())
                <tr>
                    <td>
                        ${url.getId()}
                    </td>
                    <td>
                    </td>
                </tr>
            @endfor
        </table>
    `
)