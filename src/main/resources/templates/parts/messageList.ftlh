<#include "security.ftlh">

<#list messages as message>
    <div class="card my-3" style="width: 435px;">
        <#if message.filename??>
            <img src="/img/${message.filename}" class="card-img-top" alt="Image"/>
        </#if>
        <div class="card-body">
            <a href="/user-messages/${message.authorId}"><h5 class="card-title">${message.authorName}</h5></a>
            <p class="card-text">${message.text}</p>
        </div>
        <div class="card-footer text-muted">
            <a href="/main?filter=${message.tag}" class="card-link">#${message.tag}</a>
            <#if message.authorId == currentUserId>
                <a type="button" class="btn btn-primary" href="/user-messages/${message.authorId}?message=${message.id}">Edit</a>
            </#if>
        </div>
    </div>
<#else>
    No messages
</#list>