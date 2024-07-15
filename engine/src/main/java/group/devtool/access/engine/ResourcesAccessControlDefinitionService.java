/*
 * The Access Access Control Engine,
 * unlike the RBAC model that utilizes static data to implement access control,
 * realizes API access control by evaluating if dynamic data satisfies the access control rules.
 *
 * License: GNU GENERAL PUBLIC LICENSE, Version 3, 29 June 2007
 * See the license.txt file in the root directory or see <http://www.gnu.org/licenses/>.
 */
package group.devtool.access.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link AccessControlDefinitionService} 实现类
 */
public class ResourcesAccessControlDefinitionService implements AccessControlDefinitionService {

	private Map<String, AccessControlDefinition> cache = new HashMap<>();

	public ResourcesAccessControlDefinitionService() {

	}

	@Override
	public AccessControlDefinition load(MetaData metadata) {
		if (!cache.containsKey(metadata.getKey())) {
			try (InputStream stream = ResourcesAccessControlDefinitionService.class.getClassLoader().getResourceAsStream(metadata.getKey())) {
				ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
				cache.put(metadata.getKey(), mapper.readValue(stream, AccessControlDefinitionImpl.class));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return cache.get(metadata.getKey());
	}

}
