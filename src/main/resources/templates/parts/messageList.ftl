<#include "security.ftl">

<div class="card-columns"> <#--оформление карточками в несколько колонок-->
    <#list messages as message> <#--обход списка. messages - коллекция которую обходим, message - экземпляр-->
        <div class="card my-3">
            <#if message.filename??> <#--?? - приведение к булевому типу-->
                <img src="/img/${message.filename}" class="card-img-top"> <#--class="card-img-top" красиво выводит картинку-->
            </#if>
            <div class="m-2">
                <span>${message.text}</span><br/>
                <i>#${message.tag}</i>
            </div>
            <div class="card-footer text-muted">
                <a href="/user-messages/${message.author.id}">${message.authorName}</a>
                <#if message.author.id == currentUserId> <#--отображаем кнопку только текущего пользователя-->
                    <a class="btn btn-primary" href="/user-messages/${message.author.id}?message=${message.id}">
                        Edit
                    </a>
                </#if>
            </div>
        </div>
    <#else> <#--если коллекция пустая-->
        No message
    </#list>
</div>