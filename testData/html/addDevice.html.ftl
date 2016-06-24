<script type="text/javascript">
   var showId = null;
   function showDetail(proId){
   		if (showId){
   			var showObj = document.getElementById(showId);
   			showObj.style.display="none";
   		}
   		
   		showId = proId;
   		var showObj = document.getElementById(showId);
   		showObj.style.display="inline";
   }
</script>
<#list profiles?keys?sort as profile_id>
<#assign profile = profiles[profile_id]/>
<div id="profile_div_${profile_id}">
	<input id="radio_${profile_id}" type="radio" name="profile" value="${profile.ID}" onclick="showDetail('profile_form_${profile_id}')"/>
	<label for="radio_${profile_id}">${profile.ID}</lable>(${profile.displayName})
</div>
</#list>

<div style="border:0px solid black; align:center; background-color:#dddddd; min-width:200px;">
<#list profiles?keys as profile_id>
<#assign profile = profiles[profile_id]/>
<div id="profile_form_${profile_id}" hidden>
	<form action="/devices/create" method="post">
	<label style="width: 200px;display:inline-block; text-align:right;">设备类型</label> ${profile_id}
	<input name="identifier_device_profile" hidden value="${profile_id}" size="40"/>
	<br/>
	<label style="width: 200px;display:inline-block; text-align:right;">设备名称(可选)</label>
	<input name="identifier_device_name" size="40"/>
	<br/>
	<#list profile.identifiers?keys?sort as identifier>
		<label style="width: 200px;display:inline-block; text-align:right;">${identifier}(${profile.identifiers[identifier]})</label>
		<input name="identifier_${identifier}" size="40"/>
		<br/>
	</#list>
	<input type="submit" value="提交"/>
	<input type="reset" value="取消"/>
	</form>
</div>
</#list>
</div>