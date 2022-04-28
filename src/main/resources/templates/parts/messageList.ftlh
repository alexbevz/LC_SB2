<#include "security.ftlh">
<#import "pager.ftlh" as p>

<#if pageMessages.getNumberOfElements() != 0>

    <@p.pager url pageMessages />

    <#list pageMessages.content as message>
        <div class="mb-3 sb-3" id="messageList">
            <div class="card my-3" style="width: 435px;" data-id="${message.id}">
                <#if message.filename??>
                    <img src="/img/${message.filename}" class="card-img-top" alt="Image"/>
                </#if>
                <div class="card-body">
                    <a class="card-title" href="/user-messages/${message.author.id}"><h5>${message.getAuthorName()}</h5>
                    </a>
                    <p class="card-text">${message.text}</p>
                </div>
                <div class="card-footer text-muted container">
                    <div class="row">

                        <a class="card-link col align-self-center"
                           href="/main?filter=${message.tag}">#${message.tag}</a>
                        <a class="col align-self-center" href="/messages/${message.id}/like">
                            <#if message.meLiked>
                                <i class="fas fa-heart"></i>
                            <#else>
                                <i class="far fa-heart"></i>
                            </#if>
                            ${message.likes}
                        </a>
                        <#if message.author.id == currentUserId>
                            <a class="col btn btn-primary"
                               href="/user-messages/${message.author.id}?message=${message.id}"
                               type="button">Edit</a>
                        </#if>
                    </div>
                </div>
            </div>
        </div>
    </#list>
    <@p.pager url pageMessages />
<#else>
    <h3>No messages</h3>
</#if>