<#assign state="on"/>
<#assign roomTmpt=16.0/>
<#assign setTemp=16.0/>

<#if device.status?has_content>
	<#assign state=device.status.state!runningMode/>
	<#assign roomTmpt=device.status.roomTemperature!roomTmpt/>
	<#assign setTemp=device.status.temperatureSetting!setTemp/>
</#if>
<form action="/controlDevice" method="get">
	<input name="control_device_id" type="hidden" value="${deviceId}"/>
	<input name="control_device_profile" type="hidden" value="${device.profileID}"/>
	<label>运行状态</label>
	<select name="control_status_state" autocomplete="off">
		<option value="off" <#if state=="off">selected="selected"</#if>>关</option>
		<option value="on" <#if state=="on">selected="selected"</#if>>开</option>
	</select>
	<br/>
	<label>室内温度</label> <label id="${deviceId}_room_tmpt_label">${roomTmpt}</label>
	<input name="control_status_roomTemperature" type="range" 
			min="${profile.spec.roomTemperatureRange[0]}"
			max="${profile.spec.roomTemperatureRange[1]}"
			value="${roomTmpt}"
			onChange="changeHvacTemperature(this, 'room', '${deviceId}')"/>
	<br/>
	<label>设定温度</label> <label id="${deviceId}_set_tmpt_label">${setTemp}</label>
	<input name="control_status_temperatureSetting" type="range" 
			min="${profile.spec.temperatureSettingRange[0]}"
			max="${profile.spec.temperatureSettingRange[1]}"
			value="${setTemp}"
			onChange="changeHvacTemperature(this, 'set', '${deviceId}')"/>
	<br/>
	
	<input type="submit" value="提交"/>
	<input type="reset" value="取消"/>
</form>