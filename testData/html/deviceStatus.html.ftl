<#assign deviceStatusTemplateFileName = "/device/" + device.profileID?replace(" ","_") + ".html.ftl"/>
<!-- ${deviceStatusTemplateFileName} -->
<#if device.status?has_content>
	<div>
	<#list device.status?keys?sort as statusKey>
		<label style="width: 100px; display:inline-bloack;">${statusKey}</label>:
		${device.status[statusKey]}
	<br/>
	</#list>
	</div>
</#if>
<div style="color: white; font-style: italic; padding: 10px; background-color: gray;">
	<#include deviceStatusTemplateFileName/>
</div>
	