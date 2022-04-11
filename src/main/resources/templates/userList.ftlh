<#import "parts/common.ftlh" as c>

<@c.page>
    List of users
    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Roles</th>
        </tr>
        </thead>
        <tbody>
        <#list users as user>
            <tr>
                <td><small>${user.id}</small></td>
                <td><strong>${user.username}</strong></td>
                <td><#list user.roles as role><i>${role}<#sep>, </i></#list></td>
                <td><a href="/user/${user.id}">Edit</a></td>
            </tr>
        <#else>
            No users
        </#list>
        </tbody>
    </table>
</@c.page>