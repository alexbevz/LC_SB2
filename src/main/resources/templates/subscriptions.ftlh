<#import "parts/common.ftlh" as c>

<@c.page>
    <h3>Channel: ${userChannel.username}</h3>
    <h2>${type}</h2>
    <ul class="list-group">
        <#list users as user>
            <li class="list-group-item">
                <a href="/user-messages/${user.id}">${user.username}</a>
            </li>
        </#list>
    </ul>
</@c.page>