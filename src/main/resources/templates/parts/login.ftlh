<#macro login path isRegisterForm>
    <form class="col-4" action="${path}" method="post">
        <div class="mb-3">
            <label class="form-label">User name:</label>
            <input class="form-control ${usernameError???string('is-invalid', '')}"
                   name="username"
                   placeholder="User name"
                   type="text"
                   value="<#if user??>${user.username}</#if>"/>
            <#if usernameError??>
                <div class="invalid-feedback">
                    ${usernameError}
                </div>
            </#if>
        </div>
        <div class="mb-3 sb-3">
            <label class="form-label">Password:</label>
            <input class="form-control ${passwordError???string('is-invalid', '')}"
                   name="password"
                   placeholder="Password"
                   type="password"/>
            <#if passwordError??>
                <div class="invalid-feedback">
                    ${passwordError}
                </div>
            </#if>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <#if isRegisterForm>
            <div class="mb-3 sb-3">
                <label class="form-label">Confirm password:</label>
                <input class="form-control ${confirmPasswordError???string('is-invalid', '')}"
                       name="confirmPassword"
                       placeholder="Confirm password"
                       type="password"/>
                <#if confirmPasswordError??>
                    <div class="invalid-feedback">
                        ${confirmPasswordError}
                    </div>
                </#if>
            </div>
            <div class="mb-3 sb-3">
                <label class="form-label">Email:</label>
                <input class="form-control ${emailError???string('is-invalid', '')}"
                       name="email"
                       placeholder="name@domain.com"
                       type="email"
                       value="<#if user??>${user.email}</#if>"/>
                <#if emailError??>
                    <div class="invalid-feedback">
                        ${emailError}
                    </div>
                </#if>
            </div>
        </#if>
        <#--        <div class="g-recaptcha" data-sitekey="${recaptchaKey}"></div>-->
        <div class="g-recaptcha" data-sitekey="6LcdKH8fAAAAAA6cYNNJHDOtEKsPTJSV6fConIPb"></div>
        <#if captchaError??>
            <div class="alert alert-danger" role="alert">
                ${captchaError}
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
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button class="btn btn-primary" type="submit">Sign Out</button>
    </form>
</#macro>

