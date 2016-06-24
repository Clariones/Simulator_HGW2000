<textarea style="width:100%;" rows="12" id="text_log_${deviceId}">
<#if deviceLog[deviceId]?has_content>
		<#list deviceLog[deviceId] as logMsg>
${logMsg}
		</#list>
</#if>
</textarea>