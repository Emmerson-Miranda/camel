package edu.emmerson.camel3.cdi.eh.routes;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.YAMLLibrary;
import org.yaml.snakeyaml.Yaml;

public class YamlRoute extends RouteBuilder {

	public static final String DIRECT = "direct:" + YamlRoute.class.getSimpleName();

	public static final String ROUTE_ID = YamlRoute.class.getSimpleName();

	@Override
	public void configure() throws Exception {
		from(DIRECT)
			.routeId(ROUTE_ID)
			.log("Route Start :: ${exchangeId} :: ${routeId}")
			// .unmarshal().json(JsonLibrary.Jackson)
			.process(e -> {
				File file = new File("src/main/resources/yaml/YmlRouteData.yaml");
				System.out.println(file.getAbsolutePath());
				FileInputStream fis = new FileInputStream(file);

				Yaml yaml = new Yaml();
				Map<String, Object> data = yaml.load(fis);
				data.put("extraField", "extra value");
				System.out.println(data);

				e.getIn().setBody(data);
			})
			// .marshal().json(JsonLibrary.Jackson) //works
			.marshal().yaml(YAMLLibrary.SnakeYAML)
			.log("Route End :: ${exchangeId} :: ${routeId}");
	}

}
