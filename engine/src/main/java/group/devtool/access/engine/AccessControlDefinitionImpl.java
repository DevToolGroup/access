package group.devtool.access.engine;

import java.util.List;

/**
 * {@link AccessControlDefinition} 默认实现
 */
public class AccessControlDefinitionImpl implements AccessControlDefinition {

	private String key;

	private List<PrivilegeImpl> privileges;

	private List<ScopeImpl> scopes;

	private List<ViewImpl> fields;

	public AccessControlDefinitionImpl() {

	}

	public AccessControlDefinitionImpl(String key,
																		 List<PrivilegeImpl> privileges,
																		 List<ScopeImpl> scopes,
																		 List<ViewImpl> fields) {
		this.key = key;
		this.privileges = privileges;
		this.scopes = scopes;
		this.fields = fields;
	}

	@Override
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public List<PrivilegeImpl> getPrivileges() {
		return privileges;
	}

	@Override
	public List<ScopeImpl> getScopes() {
		return scopes;
	}

	@Override
	public List<ViewImpl> getFields() {
		return fields;
	}

	public void setPrivileges(List<PrivilegeImpl> privileges) {
		this.privileges = privileges;
	}

	public void setScopes(List<ScopeImpl> scopes) {
		this.scopes = scopes;
	}

	public void setFields(List<ViewImpl> fields) {
		this.fields = fields;
	}

}
