package elevator;

public class CommandUpAndDown implements Command {
	
	private ElevatorCar elevator;
	private Request request;
	
	public CommandUpAndDown(ElevatorCar elevator, Request request){
		this.elevator = elevator;
		this.request = request;
	}

	@Override
	public void execute() {
		elevator.processRequest(request);
	}

}
