package de.draegerit.microarduinoser;

import java.util.EnumSet;


import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;

import org.eclipse.jetty.servlets.CrossOriginFilter;

import de.draegerit.microarduinoser.configuration.MicroArduinoSerConfiguration;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class MicroArduinoSerApplication extends Application<MicroArduinoSerConfiguration> {

	public static void main(String... args) throws Exception {
		new MicroArduinoSerApplication().run(args);
	}

	@Override
	public void run(MicroArduinoSerConfiguration configuration, Environment environment) throws Exception {
		final Resource resource = new Resource(
								configuration.getPortname(),
								configuration.getBaudrate(),
								configuration.getDatabits(),
								configuration.getStopbits(),
								configuration.getParity(),
								configuration.getSleep(),
								configuration.getFirstCharacter(),
								configuration.getLastCharacter()
								);
		environment.jersey().register(resource);
		addCors(environment);
	}

	private void addCors(Environment environment){
		final FilterRegistration.Dynamic cors = environment.servlets().addFilter("CORS", (Class<? extends Filter>) CrossOriginFilter.class);

		// Configure CORS parameters
		cors.setInitParameter("allowedOrigins", "*");
		cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
		cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

		// Add URL mapping
		cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
	}

	@Override
	public String getName() {
		return "AdditionApplication";
	}

	@Override
	public void initialize(Bootstrap<MicroArduinoSerConfiguration> bootstrap) {
		// nothing to do yet
	}

}