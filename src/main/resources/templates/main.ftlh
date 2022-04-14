<#import "parts/common.ftlh" as c>

<@c.page>
    <div>
        <h3>Find by a tag</h3>
        <form class="row g-2" method="get" action="/main">
            <div class="col-auto">
                <input class="form-control" type="text" name="filter" value="${filter!""}" placeholder="Filter">
            </div>
            <div class="col-auto">
                <button class="btn btn-primary mb-3" type="submit">Search</button>
            </div>
        </form>
    </div>

    <a class="btn btn-primary" data-bs-toggle="collapse" href="#addMessage" role="button" aria-expanded="false"
       aria-controls="addMessage">
        Add New Message
    </a>
    <div id="addMessage" class="collapse ${message???string('show', '')}">
        <div class="form-group py-1 col-4">
            <form method="post" enctype="multipart/form-data">
                <div class="form-group py-2">
                    <input class="form-control ${(textError??)?string('is-invalid', '')}"
                           name="text"
                           placeholder="Type a message"
                           type="text"
                           value="<#if message??>${message.text}</#if>"/>
                    <#if textError??>
                        <div class="invalid-feedback">
                            ${textError}
                        </div>
                    </#if>
                </div>
                <div class="form-group py-2">
                    <input class="form-control ${(tagError??)?string('is-invalid', '')}"
                           name="tag"
                           placeholder="Tag"
                           type="text"
                           value="<#if message??>${message.tag}</#if>"/>
                    <#if tagError??>
                        <div class="invalid-feedback">
                            ${tagError}
                        </div>
                    </#if>
                </div>
                <div class="form-group py-2">
                    <input class="form-control ${(fileError??)?string('is-invalid', '')}"
                           multiple
                           name="file"
                           type="file">
                    <#if fileError??>
                        <div class="invalid-feedback">
                            ${fileError}
                        </div>
                    </#if>
                </div>
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <div class="form-group py-2">
                    <button class="btn btn-primary" type="submit">Add</button>
                </div>
            </form>
        </div>
    </div>

    <div>
        <#list messages as message>
            <div class="card my-3" style="width: 435px;">
                <#if message.filename??>
                    <img src="/img/${message.filename}" class="card-img-top" alt="Image"/>
                </#if>
                <div class="card-body">
                    <h5 class="card-title">${message.authorName}</h5>
                    <p class="card-text">${message.text}</p>
                    <a href="/main?filter=${message.tag}" class="card-link">${message.tag}</a>
                </div>
            </div>
        <#else>
            No messages
        </#list>
    </div>
</@c.page>