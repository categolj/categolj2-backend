<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Business Error!</title>
<link rel="stylesheet"
  href="${pageContext.request.contextPath}/resources/app/css/styles.css">
</head>
<body>
  <div id="wrapper">
    <h1>Business Error!</h1>
    <div class="error">
      <c:choose>
        <c:when test="${empty exceptionCode}">
          <spring:message code="e.xx.fw.8001" />
        </c:when>
        <c:otherwise>
      [${f:h(exceptionCode)}] <spring:message code="${exceptionCode}" />
        </c:otherwise>
      </c:choose>
      <t:messagesPanel />
    </div>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
  </div>
</body>
</html>