<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>

<@c.page>
<div>
    <@l.logout />
    <span><a href="/user">User list</a></span>
</div>
<div>
    <form method="post" enctype="multipart/form-data">
        <input type="text" name="text" placeholder="Введите сообщение" />
        <input type="text" name="tag" placeholder="Тэг">
        <input type="file" name="file">
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <button type="submit">Добавить</button>
    </form>
</div>
<div>Список сообщений</div>
<form method="get" action="/main"> <#--_csrf токен нужен только для post запросов, поэтому его тут нет-->
    <input type="text" name="filter" value="${filter?ifExists}"> <#--value="${filter!""}" нужно для того что бы отображились в форме введенные фильтры / Позже заменил на value="${filter?ifExists}">-->
    <button type="submit">Найти</button>
</form>
<#list messages as message> <#--обход списка. messages - коллекция которую обходим, message - экземпляр-->
<div>
    <b>${message.id}</b>
    <span>${message.text}</span>
    <i>${message.tag}</i>
    <strong>${message.authorName}</strong>
    <div>
        <#if message.filename??> <#--?? - приведение к булевому типу-->
            <img src="/img/${message.filename}">
        </#if>
    </div>
</div>
<#else> <#--если коллекция пустая-->
No message
</#list>
</@c.page>
