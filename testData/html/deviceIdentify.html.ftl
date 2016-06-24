<#list device.identity?keys?sort as idtKey>
	<label style="width: 100px; display:inline-bloack;">${idtKey}</label>:
	${device.identity[idtKey]}
	<br/>
</#list>