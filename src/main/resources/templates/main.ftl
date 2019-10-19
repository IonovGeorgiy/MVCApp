<#import "parts/common.ftl" as c>

<@c.page>
    <div class="form-row">
        <div class="form-group col-md-6">
            <form method="get" action="/main" class="form-inline"> <#--_csrf токен нужен только для post запросов, поэтому его тут нет-->
                <input type="text" name="filter" class="form-control" value="${filter?ifExists}" placeholder="Search by tag"> <#--value="${filter!""}" нужно для того что бы отображились в форме введенные фильтры / Позже заменил на value="${filter?ifExists}">-->
                <button type="submit" class="btn btn-primary ml-2">Search</button>
            </form>
        </div>
    </div>

    <a class="btn btn-primary" data-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false" aria-controls="collapseExample"> <#--разворачивающийся элемент-->
        Add new Message
    </a>
    <div class="collapse" id="collapseExample">
        <div class="form-group mt-3">
            <form method="post" enctype="multipart/form-data">
                <div class="form-group">
                    <input type="text" class="form-control" name="text" placeholder="Введите сообщение" />
                </div>
                <div class="form-group">
                    <input type="text" class="form-control" name="tag" placeholder="Тэг">
                </div>
                <div class="form-group">
                    <div class="custom-file">
                        <input type="file" name="file" id="customFile">
                        <label class="custom-file-label" for="customFile">Choose file</label>
                    </div>
                </div>
                <input type="hidden" name="_csrf" value="${_csrf.token}" />
                <div class="form-group">
                    <button type="submit" class="btn btn-primary">Добавить</button>
                </div>
            </form>
        </div>
    </div>

    <div class="card-columns"> <#--оформление карточками в несколько колонок-->
        <#list messages as message> <#--обход списка. messages - коллекция которую обходим, message - экземпляр-->
            <div class="card my-3">
                <#if message.filename??> <#--?? - приведение к булевому типу-->
                    <img src="/img/${message.filename}" class="card-img-top"> <#--class="card-img-top" красиво выводит картинку-->
                </#if>
                <div class="m-2">
                    <span>${message.text}</span>
                    <i>${message.tag}</i>
                </div>
                <div class="card-footer text-muted">
                    ${message.authorName}
                </div>
            </div>
        <#else> <#--если коллекция пустая-->
            No message
        </#list>
    </div>
</@c.page>
