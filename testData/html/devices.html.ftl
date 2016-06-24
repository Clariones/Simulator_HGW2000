<#assign page_title="设备列表"/>
<#include "common/header.ftl"/>

<body>
<script type="text/javascript">
function changeDimmer(input, devId){
	var value = input.value;
	var imgObj = document.getElementById(devId+"_state_img");
	var lblObj = document.getElementById(devId+"_dimmer_label");
	var formObj = document.getElementById(devId+"_control_form");
	var state = document.getElementById(devId+"_control_state");

	if (value > 0){
		imgObj.src="/images/switchon.png";
		lblObj.innerHTML = value;
		state.value="on";
	}else{
		imgObj.src="/images/switchoff.png";
		lblObj.innerHTML = value;
		state.value="off";
	}
	formObj.submit();
}
function changeHvacTemperature(input, type, devId){
	var value = input.value;
	var lblObj = document.getElementById(devId+"_" + type +"_tmpt_label");
	lblObj.innerHTML = value;
}
</script>
<a href="/">返回首页</a>
<h1>设备列表</h1>

<#if devices?has_content>
<table width="100%">
	<#list devices?keys?sort as deviceId>
		<#assign device=devices[deviceId]/>
		<#assign profile=profiles[device.profileID]/>
		<tr>
			<td colspan="3">
			<hr/>
				${deviceId} (${device.profileID})
			</td>
			</tr><tr>
			<td width="200px">
				<#include "deviceStatus.html.ftl"/>
			</td>
			<td width="200px">
				<#include "deviceIdentify.html.ftl"/>
			</td>
			<td>
				<#include "deviceLogs.html.ftl"/>
			</td>
			</tr>

	</#list>
			</table>
<#else>
    没设备
</#if>
<hr/>
<#if sysLog?has_content>
<textarea style="width:100%;" rows="25" id="text_log_sysLog">
	<#list sysLog as logMsg>
${logMsg}
	</#list>
</textarea>
</#if>
<#include "common/footer.ftl"/>
</body>
</html>
