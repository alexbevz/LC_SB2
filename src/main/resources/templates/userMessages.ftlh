<#import "parts/common.ftlh" as c>

<@c.page>
    <h3>Channel: ${userChannel.username}</h3>
    <#if !isCurrentUser>
        <div class="mb-3 sb-3">
            <#if isSubscriber>
                <a class="btn btn-danger mb-3 sb-3" href="/user/unsubscribe/${userChannel.id}">Unsubscribe</a>
                <#if messageSubscribe??><div class="alert alert-success">${messageSubscribe}</div></#if>
            <#else>
                <a class="btn btn-info mb-3 sb-3" href="/user/subscribe/${userChannel.id}">Subscribe</a>
                <#if messageUnsubscribe??><div class="alert alert-danger">${messageUnsubscribe}</div></#if>
            </#if>
        </div>
    </#if>
    <div class="mb-3 sb-3">
        <div class="row">
            <div class="col">
                <div class="card">
                    <div class="card-body">
                        <div class="card-title">Subscriptions</div>
                        <h3 class="card-text">
                            <a href="/user/subscriptions/${userChannel.id}/list">${countSubscriptions}</a>
                        </h3>
                    </div>
                </div>
            </div>
            <div class="col">
                <div class="card">
                    <div class="card-body">
                        <div class="card-title">Subscribers</div>
                        <h3 class="card-text">
                            <a href="/user/subscribers/${userChannel.id}/list">${countSubscribers}</a>
                        </h3>
                    </div>
                </div>
            </div>
        </div>

    </div>
    <#if isCurrentUser && message??>
        <#include "parts/messageEdit.ftlh" />
    </#if>
    <#include  "parts/messageList.ftlh" />
</@c.page>