<a aria-controls="addMessage"
   aria-expanded="false"
   class="btn btn-primary mb-3 sb-3"
   data-bs-toggle="collapse"
   href="#addMessage"
   role="button">
    Message edit
</a>
<div id="addMessage" class="collapse mb-3 sb-3 ${message???string('show', '')}">
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
            <input type="hidden" name="id" value="<#if message??>${message.id}</#if>"/>
            <div class="form-group py-2">
                <button class="btn btn-primary" type="submit">Save</button>
            </div>
        </form>
    </div>
</div>