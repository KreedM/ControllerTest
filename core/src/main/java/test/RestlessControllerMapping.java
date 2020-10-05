package test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.AdvancedController;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import de.golfgl.gdx.controllers.mapping.ConfiguredInput;
import de.golfgl.gdx.controllers.mapping.ControllerMappings;

public class RestlessControllerMapping extends ControllerMappings {
	public static int UP_DOWN = 0;
	public static int LEFT_RIGHT = 1;
	
	public static int START = 2;
	
	public static int INTERACT = 3;
	public static int ROLL = 4;
	public static int USE = 5;
	
	public static int LEFT_ITEM = 6;
	public static int RIGHT_ITEM = 7;
	
	public static final String[] ORDER_OF_MAPPING = new String[] {"Up/Down", "Left/Right", "Start", "Interact", "Roll", "Use", "Left Item Switch", "Right Item Switch"}; 
	
	private int currentBind;
	private boolean finished;
	
	private AdvancedController controller;
	
	public RestlessControllerMapping(AdvancedController controller) {
		this.controller = controller;

		addConfiguredInput(new ConfiguredInput(ConfiguredInput.Type.axis, UP_DOWN));
		addConfiguredInput(new ConfiguredInput(ConfiguredInput.Type.axis, LEFT_RIGHT));

		addConfiguredInput(new ConfiguredInput(ConfiguredInput.Type.button, START));

		addConfiguredInput(new ConfiguredInput(ConfiguredInput.Type.button, INTERACT));
		addConfiguredInput(new ConfiguredInput(ConfiguredInput.Type.button, ROLL));
		addConfiguredInput(new ConfiguredInput(ConfiguredInput.Type.button, USE));

		addConfiguredInput(new ConfiguredInput(ConfiguredInput.Type.button, LEFT_ITEM));
		addConfiguredInput(new ConfiguredInput(ConfiguredInput.Type.button, RIGHT_ITEM));

		commitConfig();
		
		if (Gdx.files.local(controller.getName().replace('/', ' ') + ".json").exists()) {			
			fillFromJson(new JsonReader().parse(Gdx.files.local(controller.getName().replace('/', ' ') + ".json")));
			commitConfig();
			
			System.out.println("Binding Finished");
			
			finished = true;
		}
		else
			System.out.println("Please bind " + ORDER_OF_MAPPING[currentBind]);
	}
	
	public boolean map() {
		if (!finished && currentBind < ORDER_OF_MAPPING.length) {
			RecordResult recordResult = recordMapping(controller, currentBind);
			
			switch (recordResult) {
			case nothing_done:
			case not_added: 
			case not_added_need_button:	break;
			case need_second_button:
				System.out.println("You used a button to bind the positive value of an axis. Please bind the negative value of " + ORDER_OF_MAPPING[currentBind]);
				break;
			case recorded: 
				System.out.println(ORDER_OF_MAPPING[currentBind++] + " was binded."); 
				
				if(currentBind < ORDER_OF_MAPPING.length)
					System.out.println("Please bind " + ORDER_OF_MAPPING[currentBind]);
			}
			
			return false;
		}
		
		else if(!finished) {		
			String fileName = (controller.getName()).replace('/', ' ') + ".json";
			String json = toJson().toJson(JsonWriter.OutputType.json);
			
			Gdx.files.local(fileName).writeString(json, false);
			
			System.out.println("Binding Finished");
			
			finished = true;
			
			return true;
		}
		
		else
			return true;
	}
}
