<#assign deviceStatusTemplateFileName = "/device/" + device.profileID?replace(" ","_") + ".html.ftl"/>
<!-- ${deviceStatusTemplateFileName} -->
<div style="color: white; font-style: italic; padding: 10px; background-color: gray;">
	<#include deviceStatusTemplateFileName/>
</div>
	