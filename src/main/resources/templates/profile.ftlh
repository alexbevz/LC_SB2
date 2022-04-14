<#import "parts/common.ftlh" as c>

<@c.page>
    <h5>${username}</h5>
    ${message!""}
    <form class="col-4" method="post">
        <div class="mb-3 sb-3">
            <label class="form-label">Password:</label>
            <input class="form-control" type="password" name="password" placeholder="Password"/>
        </div>
        <div class="mb-3 sb-3">
            <label class="form-label">Email:</label>
            <input class="form-control" type="email" name="email" placeholder="name@domain.com" value="${email!""}"/>
        </div>
        <div>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button class="btn btn-primary" type="submit">Save</button>
        </div>
    </form>
</@c.page>
