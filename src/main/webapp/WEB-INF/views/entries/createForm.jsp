<form:form modelAttribute="entryForm" method="post"
           class="form-horizontal">
    <fieldset>
        <legend>Create a new entry</legend>
        <div class="form-group">
            <form:label path="title" cssErrorClass="error-label"
                        cssClass="col col-sm-2 control-label">Title:</form:label>
            <div class="col col-sm-10">
                <form:input path="title" cssErrorClass="error-input" cssClass="form-control"/>
                <form:errors path="title" cssClass="error-messages"/>
            </div>
        </div>
        <div class="form-group">
            <form:label path="categoryString" cssErrorClass="error-label"
                        cssClass="col col-sm-2 control-label">Category:</form:label>
            <div class="col col-sm-10">
                <form:input path="categoryString" cssErrorClass="error-input" cssClass="form-control"/>
                <form:errors path="categoryString" cssClass="error-messages"/>
            </div>
        </div>
        <div class="form-group">
            <form:label path="contents" cssErrorClass="error-label"
                        cssClass="col col-sm-2 control-label">Contents:</form:label>
            <div class="col col-sm-10">
                <form:textarea path="contents" cssErrorClass="error-input" cssClass="form-control"/>
                <form:errors path="contents" cssClass="error-messages"/>
            </div>
        </div>
        <div class="form-group">
            <form:label path="format" cssErrorClass="error-label"
                        cssClass="col col-sm-2 control-label">Format:</form:label>
            <div class="col col-sm-10">
                <form:radiobuttons path="format" items="${CL_FORMAT}"
                                   cssErrorClass="error-input"/>
                <form:errors path="format" cssClass="error-messages"/>
            </div>
        </div>
        <div class="form-group">
            <div class="col col-sm-10 col-sm-offset-2">
                <div class="checkbox">
                    <label>
                        <form:checkbox path="published"/> Publish?
                        <form:errors path="published" cssClass="error-messages"/>
                    </label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="col col-sm-10 col-sm-offset-2">
                <a href="#entries" class="btn btn-default">Back</a>
                <form:button id="btn-entry-confirm" class="btn btn-primary">Confirm</form:button>
            </div>
        </div>
    </fieldset>
</form:form>