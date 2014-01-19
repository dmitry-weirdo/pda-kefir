<%@ page pageEncoding="UTF-8" language="java" %>
<html>
<head>
	<title>Kefir sample application</title>
	<%@ include file="./extInclude.jspf" %>
	<%@ include file="./application.jspf" %>
</head>
<body>
<script type="text/javascript">
	Kefir.contextPath = '<%=request.getContextPath()%>';

	Ext.onReady(function() {
		Ext.QuickTips.init();

		su.opencode.kefir.sampleSrv.mainMenu.MainMenu.init();
	});
</script>
</body>
</html>