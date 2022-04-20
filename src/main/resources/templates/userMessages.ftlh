<#import "parts/common.ftlh" as c>

<@c.page>
    <#if isCurrentUser && message??>
        <#include "parts/messageEdit.ftlh" />
    </#if>
    <#include  "parts/messageList.ftlh" />
</@c.page>