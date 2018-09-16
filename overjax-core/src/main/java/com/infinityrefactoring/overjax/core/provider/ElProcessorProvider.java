package com.infinityrefactoring.overjax.core.provider;

import static com.infinityrefactoring.overjax.config.ConfigType.EL_PROCESSOR;

import javax.annotation.PostConstruct;
import javax.el.ELProcessor;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import com.infinityrefactoring.overjax.annotation.Config;

/**
 * @author Thomás Sousa Silva (ThomasSousa96)
 */
@RequestScoped
public class ElProcessorProvider {

	@Inject
	@Config(EL_PROCESSOR)
	private Event<ELProcessor> elProcessorEvent;

	private ELProcessor elProcessor;

	public ElProcessorProvider() {
		elProcessor = new ELProcessor();
	}

	@Produces
	public ELProcessor getElProcessor() {
		return elProcessor;
	}

	@PostConstruct
	public void postConstruct() {
		elProcessorEvent.fire(elProcessor);
	}

}
