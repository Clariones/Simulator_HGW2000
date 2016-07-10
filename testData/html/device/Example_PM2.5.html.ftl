<#assign imgId=deviceId+"_state_img"/>
<#assign lblId=deviceId+"_dimmer_label"/>
<#assign value=0/>
<#if device.status?has_content>
	<#assign value = device.status.value/>
</#if>

	<image id="${imgId}" src="${img}" width="96px"/>
	<br/>
	<form action="/controlDevice" method="get" id="${deviceId}_control_form">
		<input name="control_device_id" type="hidden" value="${deviceId}"/>
		<input name="control_device_profile" type="hidden" value="${device.profileID}"/>
		<input name="control_status_state" id="${deviceId}_control_state" type="hidden" />
		<input name="control_status_value" type="range" min="0" max="1200" value="${value}" onChange="changeDimmer(this, '${deviceId}')"/>
		<br/>
		亮度：
		<label id="${lblId}">${value}</label>
	</form>
