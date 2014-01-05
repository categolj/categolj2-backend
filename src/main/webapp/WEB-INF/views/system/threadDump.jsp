<p>
    <a href="#system" class="btn btn-info">Get system info</a>
</p>

<h2>Thread dump</h2>

<div class="table-responsive">
    <table
            class="table table-striped table-bordered table-condensed">
        <thead>
        <tr>
            <th>Thread Name</th>
            <th>Thread Id</th>
            <th>Thread Group</th>
            <th>Thread State</th>
            <th>StackTrace</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${threadDumps}" var="threadDump">
            <tr>
                <td>${f:h(threadDump.value.threadName)}</td>
                <td>${f:h(threadDump.value.threadId)}</td>
                <td>${f:h(threadDump.value.threadGroup)}</td>
                <td>${f:h(threadDump.value.threadState)}</td>
                <td><span style="display: none;">${f:h(threadDump.value.stackTrace)}</span></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
