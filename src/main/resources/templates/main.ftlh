<#import "parts/common.ftlh" as c>

<@c.page>
    <div>
        <h3>Find by a tag</h3>
        <form class="row g-2" method="get" action="/main">
            <div class="col-auto">
                <input class="form-control" type="text" name="filter" value="${filter!""}" placeholder="Filter" />
            </div>
            <div class="col-auto">
                <button class="btn btn-primary mb-3" type="submit">Search</button>
            </div>
        </form>
    </div>

    <#include "parts/messageEdit.ftlh" />
    <#include "parts/messageList.ftlh" />

</@c.page>