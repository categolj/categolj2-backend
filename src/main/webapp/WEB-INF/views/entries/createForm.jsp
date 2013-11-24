<!doctype html>
<html>
<head>
<title>Create Entry</title>
<style type="text/css">
.form-horizontal input {
    display: block;
    float: left;
}

.form-horizontal label {
    display: block;
    float: left;
    text-align: right;
    float: left;
}

.form-horizontal br {
    clear: left;
}

.error-label {
    color: #b94a48;
}

.error-input {
    border-color: #b94a48;
    margin-left: 5px;
}

.error-messages {
    color: #b94a48;
    display: block;
    padding-left: 5px;
    overflow-x: auto;
}
</style>
</head>

<body>

    <form:form modelAttribute="entryForm" method="post"
        class="form-horizontal"
        action="${pageContext.request.contextPath}/entries">
        <form:label path="title" cssErrorClass="error-label">Title:</form:label>
        <form:input path="title" cssErrorClass="error-input" />
        <form:errors path="title" cssClass="error-messages" />
        <br>
        <form:label path="categoryString" cssErrorClass="error-label">Category:</form:label>
        <form:input path="categoryString" cssErrorClass="error-input" />
        <form:errors path="categoryString" cssClass="error-messages" />
        <br>
        <form:label path="contents" cssErrorClass="error-label">Contents:</form:label>
        <form:input path="contents" cssErrorClass="error-input" />
        <form:errors path="contents" cssClass="error-messages" />
        <br>
        <form:label path="format" cssErrorClass="error-label">Format:</form:label>
        <form:input path="format" cssErrorClass="error-input" />
        <form:errors path="format" cssClass="error-messages" />
        <br>
        <form:label path="published" cssErrorClass="error-label">Publish?:</form:label>
        <form:checkbox path="published" cssErrorClass="error-input" />
        <form:errors path="published" cssClass="error-messages" />
        <br>
        <form:button>Create</form:button>
    </form:form>
</body>
</html>