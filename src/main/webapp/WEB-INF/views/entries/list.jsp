<div class="btn-group">
    <a href="#entries?form"
       class="btn btn-default"><span
            class="glyphicon glyphicon-list"></span> 作成</a> <a
        href="#entries/search" class="btn btn-default"><span
        class="glyphicon glyphicon-search"></span> 検索</a>
</div>
<hr>
<table class="table table-striped table-bordered table-condensed">
    <tr>
        <th>Title</th>
        <th>Contents</th>
        <th>Created date</th>
        <th>Last modified date</th>
    </tr>
    <c:forEach items="${page.content}" var="entry">
        <tr>
            <td><a
                    href="#/entries/${f:h(entry.entryId)}">${f:h(entry.title)}</a>
            </td>
            <td>${f:h(entry.contents)}</td>
            <td>${f:h(entry.createdDate)}</td>
            <td>${f:h(entry.lastModifiedDate)}</td>
        </tr>
    </c:forEach>
</table>
<t:pagination page="${page}" outerElementClass="pagination"/>