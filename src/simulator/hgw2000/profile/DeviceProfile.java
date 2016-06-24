package simulator.hgw2000.profile;

import java.util.Map;

public class DeviceProfile {
	protected String displayName;
	protected Map<String, Object> extParams;
	protected String ID;
	protected Map<String, String> identifiers;
	protected Map<String, Object> spec;
	
	protected String standard;
	public String getDisplayName() {
		return displayName;
	}
	public Map<String, Object> getExtParams() {
		return extParams;
	}
	public String getID() {
		return ID;
	}
	public Map<String, String> getIdentifiers() {
		return identifiers;
	}
	public Map<String, Object> getSpec() {
		return spec;
	}
	public String getStandard() {
		return standard;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public void setExtParams(Map<String, Object> extParams) {
		this.extParams = extParams;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public void setIdentifiers(Map<String, String> identifiers) {
		this.identifiers = identifiers;
	}
	public void setSpec(Map<String, Object> spec) {
		this.spec = spec;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
}
