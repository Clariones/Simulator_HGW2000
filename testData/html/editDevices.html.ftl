<div style="border: 1px solid gray;">
<form action="/updateDevice" method="get">
	<input type="hidden" name="device_org_id" value="${deviceId}"/>
	<input type="hidden" name="identifier_device_profile" value="${device.profileID}"/>
	<label style="width: 200px;display:inline-block; text-align:right;">设备类型</label> ${device.profileID}
	<br/>
	<label style="width: 200px;display:inline-block; text-align:right;">设备名称</label>
	<input name="identifier_device_name" size="40" value="${deviceId}"/>
	<br/>
	<#list device.identity?keys?sort as identifier>
		<label style="width: 200px;display:inline-block; text-align:right;">${identifier}</label>
		<input name="identifier_${identifier}" size="40" value="${device.identity[identifier]}"/>
		<br/>
	</#list>
	<button name="edit_action" type="submit" value="save">保存</button>
	<button name="edit_action" type="submit" value="delete">删除</button>
	<button type="reset" value="cancel">取消</button>
</form>
</div>
