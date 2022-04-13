<#macro login path isRegisterForm>
    <form class="col-4" action="${path}" method="post">
        <div class="mb-3">
            <label class="form-label">User Name:</label>
            <input class="form-control" type="text" name="username" placeholder="User name"/>
        </div>
        <div class="mb-3 sb-3">
            <label class="form-label">Password:</label>
            <input class="form-control" type="password" name="password" placeholder="Password"/>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <#if isRegisterForm>
            <div class="mb-3 sb-3">
                <label class="form-label">Email:</label>
                <input class="form-control" type="email" name="email" placeholder="name@domain.com"/>
            </div>
        </#if>

        <div>
            <#if !isRegisterForm>
                <div>
                    <button class="btn btn-primary" type="submit">Log In</button>
                </div>
                <a href="/registration">Sign Up</a>
            <#else>
                <div>
                    <button class="btn btn-primary" type="submit">Sign Up</button>
                </div>
                <a href="/login">Log In</a>
            </#if>
        </div>
    </form>
</#macro>

<#macro logout>
    <form action="/logout" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <button class="btn btn-primary" type="submit">Sign Out</button>
    </form>
</#macro>

