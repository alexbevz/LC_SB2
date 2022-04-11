<#import "parts/common.ftlh" as c>

<@c.page>
    <form method="post" action="/user">
        <label>
            Username:
            <input name="username" type="text" value="${user.username}"/>
        </label>
        <br/>
        <label>
            Role(s):
            <#list roles as role>
                <input name="${role}"
                       type="checkbox" ${user.roles?seq_contains(role)?string("checked", "")}/>
                ${role}
            </#list>
        </label>
        <br/>
        <input name="userId" type="hidden" value="${user.id}"/>
        <input name="_csrf" type="hidden" value="${_csrf.token}"/>
        <button type="submit">Update</button>
    </form>
</@c.page>