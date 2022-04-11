<#import "parts/common.ftlh" as c>
<#import "parts/login.ftlh" as l>

<@c.page>
    <div>
        <@l.logout/>
        <span><a href="/user">List of users</a></span>
    </div>
    <div>
        <form method="post">
            <label>
                <input type="text" name="text" placeholder="Type a message"/>
            </label>
            <label>
                <input type="text" name="tag" placeholder="Tag">
            </label>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button type="submit">Add</button>
        </form>
    </div>
    <div>The list of messages</div>
    <form method="get" action="/main">
        <label>
            <input type="text" name="filter" value="${filter}">
        </label>
        <button type="submit">Find</button>
    </form>
    <#list messages as message>
        <div>
            <b>${message.id}</b>
            <span>${message.text}</span>
            <i>${message.tag}</i>
            <strong>${message.authorName}</strong>
        </div>
    <#else>
        No messages
    </#list>
</@c.page>