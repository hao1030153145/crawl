<%@ include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>
<%@ page isErrorPage="true"%>
<%@ page contentType="text/html;charset=GBK"%>
<%--============================================================================
˵��������������������ʱ��ʾ�Ĵ���ҳ��
============================================================================--%>
<html>
<head>
<%@ include file="/WEB-INF/jsp/layout/meta.jsp"%>
<title>QuokkaChat������ʾҳ��</title>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link rel="stylesheet" href="/styles/default.css" type="text/css" >

</head>
<body>
<script language="javascript">
function showhide(layerId){
     obj = document.getElementById(layerId);
     if(obj)
     {   
	     var isVisible = obj.style.display == "none";
	     obj.style.display = isVisible ? "" : "none";
     }
}
</script>
<div align="center" class="error">
   <p />
   <p />
   <p />
   <p />��������${message}<br>
   �鿴<a href="javascript:showhide('detail')">��ϸ���</a>
   <p />
   <div id="detail" align="left"
	  style="display:none;border:1px dashed gray;padding:10px;margin:10px">
      <pre>

      </pre>
   </div>
</div>


</body>
</html>
