<#assign page_title="控制台"/>
<#include "common/header.ftl"/>

<body>
<a href="/">返回首页</a>
<h1>控制台</h1>
<h2>添加设备</h2>
<#include "addDevice.html.ftl"/></td>
<hr/>
<h2>已有设备</h2>
<#if devices?has_content>
	<#list devices?keys?sort as deviceId>
		<#assign device=devices[deviceId]/>
		<#include "editDevices.html.ftl"/>
	</#list>
<#else>
	没有设备可编辑
</#if>

<#include "common/footer.ftl"/>
</body>
</html>