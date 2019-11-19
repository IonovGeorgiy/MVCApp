<#import "parts/common.ftl" as c>

<@c.page>
    <#if isCurrentUser> <#--редактор сообщений будет отображаться только на собственной странице пользователя-->
        <#include "parts/messageEdit.ftl" />
    </#if>

    <#include "parts/messageList.ftl" />
</@c.page>