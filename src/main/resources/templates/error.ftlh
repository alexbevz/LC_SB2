<#import "parts/common.ftlh" as c>

<@c.page>
    <div>
        <h1>Ошибка</h1>
        <p>
            <#if infoError??>
                ${infoError}
            <#else>
                Ошибок не выявлено, вы сами перешли на страницу!
                Переадресация на главную страницу будет произведена через 5 секунд!
                <meta http-equiv="refresh" content="5;/"/>
            </#if>
        </p>
    </div>
</@c.page>