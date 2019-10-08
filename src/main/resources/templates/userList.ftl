<#import "parts/common.ftl" as c>

<@c.page>
    List of users

<table>
    <thead>
    <tr>
        <th>Name</th>
        <th>Role</th>
        <th></th>
    </tr>
    </thead>
    <tbody>
    <#list users as user>
        <tr>
            <td>${user.username}</td>
            <td><#list user.roles as role>${role}<#sep>, </#list></td> <#--ролей у пользователя может быть много, так что выводим их с помошью списка. "<#sep>, "- все элементы из списка будут разделены запятой-->
            <td><a href="/user/${user.id}">edit</a> </td>
        </tr>
    </#list>
    </tbody>
</table>
</@c.page>