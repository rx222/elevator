package elevator;

public class Request {
	public long time;
	public Integer floor;
	public String direction;

	public Request(long time, Integer floor, String direction) {
		this.time = time;
		this.floor = floor;
		this.direction = direction;
	}
}
