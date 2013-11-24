<!doctype html>
<html>
<head>
<title>Entries</title>
</head>

<body>
    <p>
        <a href="${pageContext.request.contextPath}/entries?form">create</a>
    </p>
    <table>
        <tr>
            <th>Title</th>
            <th>Contents</th>
            <th>Created date</th>
            <th>Last modified date</th>
        </tr>
        <c:forEach items="${page.content}" var="entry">
            <tr>
                <td><a
                    href="${pageContext.request.contextPath}/entries/${f:h(entry.entryId)}?form">${f:h(entry.title)}</a></td>
                <td>${f:h(entry.contents)}</td>
                <td>${f:h(entry.createdDate)}</td>
                <td>${f:h(entry.lastModifiedDate)}</td>
            </tr>
        </c:forEach>

    </table>
    <t:pagination page="${page}" />
</body>
</html>