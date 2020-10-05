package test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.AdvancedController;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;

import de.golfgl.gdx.controllers.mapping.MappedControllerAdapter;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class ControllerTest extends ApplicationAdapter {
	private float currTime;
	private boolean finished;
	
	private AdvancedController controller;
	private RestlessControllerMapping mapping;
	
	public void create() {
		controller = (AdvancedController) Controllers.getControllers().get(0);
		
		mapping = new RestlessControllerMapping(controller);
	}
	
	public void render() {
		currTime += Gdx.graphics.getDeltaTime();
		
		if (currTime >= 0.1f) {
			if(!finished && mapping.map()) {
				controller.addListener(new MappedControllerAdapter(mapping) {
					public boolean configuredAxisMoved(Controller controller, int axisId, float value) {
						System.out.println(RestlessControllerMapping.ORDER_OF_MAPPING[axisId] + ": " + value);
						
						return true;
					}
					
					public boolean configuredButtonDown(Controller controller, int buttonId) {
						System.out.println(RestlessControllerMapping.ORDER_OF_MAPPING[buttonId]);
						
						return true;
					}
				});
				
				finished = true;
			}
			
			currTime -= 0.1f;
		}
	}
}