<#import "parts/common.ftlh" as c>
<#import "parts/login.ftlh" as l>

<@c.page>
    Add New User
    ${message!""}
    <@l.registration/>
    <a href="/login">Log In</a>
</@c.page>
