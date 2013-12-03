<div>
    <table
        class="table table-striped table-bordered table-condensed table-responsive">
        <tr>
            <th>Thread Name</th>
            <th>Thread Id</th>
            <th>Thread Group</th>
            <th>Thread State</th>
            <th>StackTrace</th>
        </tr>
        <c:forEach items="${threadDumps}" var="threadDump">
            <tr>
                <td>${f:h(threadDump.value.threadName)}</td>
                <td>${f:h(threadDump.value.threadId)}</td>
                <td>${f:h(threadDump.value.threadGroup)}</td>
                <td>${f:h(threadDump.value.threadState)}</td>
                <td><span style="display: none;">${f:h(threadDump.value.stackTrace)}</span></td>
            </tr>
        </c:forEach>
    </table>
</div>