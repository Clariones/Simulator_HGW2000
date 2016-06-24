<#macro showStatus devID profID picName action>
	<form action="/controlDevice" method="get">
		<input name="control_device_id" type="hidden" value="${devID}"/>
		<input name="control_device_profile" type="hidden" value="${profID}"/>
		<input name="control_status_state" type="hidden" value="${action}"/>
		<input type="image" src="images/${picName}" alt="Submit Form" />
	</form>
</#macro>

<#if device.status?has_content>
	<#assign state = device.status.state>
	<#if state == "on">
		<@showStatus devID=deviceId profID=device.profileID picName="switchon.png" action="off"/>
	<#else>
		<@showStatus devID=deviceId profID=device.profileID picName="switchoff.png" action="on"/>
	</#if>
<#else>
	<@showStatus devID=deviceId profID=device.profileID picName="unknown.png" action="on"/>
</#if>
