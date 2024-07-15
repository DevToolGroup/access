package group.devtool.access.engine;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class ResourcesAccessControlDefinitionServiceTest {

	@Test
	public void loadAccessControlDefinitionFromYaml() throws ExpressionException {
		ResourcesAccessControlDefinitionService service = new ResourcesAccessControlDefinitionService();
		AccessControlDefinition definition = service.load(new FileMetaData());

		assertNotNull(definition);
		assertEquals("access", definition.getKey());
		assertEquals(2, definition.getPrivileges().size());
		assertEquals(2, definition.getFields().size());
		assertEquals(2, definition.getScopes().size());
	}

}
