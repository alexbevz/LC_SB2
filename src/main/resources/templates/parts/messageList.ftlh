<#include "security.ftlh">
<div class="mb-3 sb-3" id="messageList">
    <#list messages as message>
        <div class="card my-3" style="width: 435px;" data-id="${message.id}">
            <#if message.filename??>
                <img src="/img/${message.filename}" class="card-img-top" alt="Image"/>
            </#if>
            <div class="card-body">
                <a class="card-title" href="/user-messages/${message.authorId}"><h5>${message.authorName}</h5></a>
                <p class="card-text">${message.text}</p>
            </div>
            <div class="card-footer text-muted">
                <a href="/main?filter=${message.tag}" class="card-link">#${message.tag}</a>
                <#if message.authorId == currentUserId>
                    <a type="button" class="btn btn-primary"
                       href="/user-messages/${message.authorId}?message=${message.id}">Edit</a>
                </#if>
            </div>
        </div>
    <#else>
        <h3>No messages</h3>
    </#list>
</div>