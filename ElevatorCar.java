package elevator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ElevatorCar {
	
	public String direction = "up";
	public Boolean idle = Boolean.TRUE;
	public int curFloor = 1;
	public String door = "closed";
	public List<Request> upQueue = new ArrayList<Request>();
	public List<Request> downQueue = new ArrayList<Request>();
	public List<Request> curQueue = new ArrayList<Request>();
	private Thread processingThread;
	
	public synchronized void processRequest(Request request){
		if(idle){
			curQueue.add(request);
			processingThread = new Thread(new Process());
			processingThread.start();
		}else{
			if(request.direction.equalsIgnoreCase("up")){
				if(direction.equalsIgnoreCase("down")){
					addRequestToUpQueue(request);
				}else{
					if(request.floor >= curFloor){
						addRequestToCurQueueUp(request);
					}else{
						addRequestToUpQueue(request);
					}
				}
			}else{
				if(direction.equalsIgnoreCase("up")){
					addRequestToDownQueue(request);
				}else{
					if(request.floor <= curFloor){
						addRequestToCurQueueDown(request);
					}else{
						addRequestToDownQueue(request);
					}
				}
			}
			if(curQueue.isEmpty()){
				preProcessNextQueue();
			}
		}
	}
	
	private void addRequestToUpQueue(Request request){
		upQueue.add(request);
		int n = upQueue.size();
		for(int i=0; i<n; i++){
			Request task1 = upQueue.get(i);
			Request task2 = upQueue.get(n-1);
			if(task2.floor < task1.floor){
				upQueue.set(i, task2);
				upQueue.set(n-1, task1);
			}
		}
	}
	
	private void addRequestToDownQueue(Request request){
		downQueue.add(request);
		int n = downQueue.size();
		for(int i=0; i<n; i++){
			Request task1 = downQueue.get(i);
			Request task2 = downQueue.get(n-1);
			if(task2.floor > task1.floor){
				downQueue.set(i, task2);
				downQueue.set(n-1, task1);
			}
		}
	}
	
	private void addRequestToCurQueueUp(Request request){
		curQueue.add(request);
		int n = curQueue.size();
		for(int i=0; i<n; i++){
			Request task1 = curQueue.get(i);
			Request task2 = curQueue.get(n-1);
			if(task2.floor < task1.floor){
				curQueue.set(i, task2);
				curQueue.set(n-1, task1);
			}
		}
	}
	
	private void addRequestToCurQueueDown(Request request){
		curQueue.add(request);
		int n = curQueue.size();
		for(int i=0; i<n; i++){
			Request task1 = curQueue.get(i);
			Request task2 = curQueue.get(n-1);
			if(task2.floor > task1.floor){
				curQueue.set(i, task2);
				curQueue.set(n-1, task1);
			}
		}
	}
	
	private void preProcessNextQueue() {
		if (getLowestTimeUpQueue() > getLowestTimeDownQueue()) {
			direction = "up";
			curQueue = upQueue;
			upQueue.clear();
		} else {
			this.direction = "down";
			curQueue = downQueue;
			downQueue.clear();;
		}
	}
	
	private long getLowestTimeUpQueue() {
		long lowest = Long.MAX_VALUE;
		for (Request r : upQueue) {
			if (r.time < lowest)
				lowest = r.time;
		}
		return lowest;
	}

	private long getLowestTimeDownQueue() {
		long lowest = Long.MAX_VALUE;
		for (Request r : downQueue) {
			if (r.time < lowest)
				lowest = r.time;
		}
		return lowest;
	}
	
	public synchronized void process(){
		idle = Boolean.FALSE;
		for (Iterator<Request> iterator = curQueue.iterator(); iterator.hasNext();) {
			Request request = iterator.next();
			Integer floor = request.floor;
			Integer distance = Math.abs(floor - curFloor);
			if(floor > curFloor){
				direction = "up";
			}else{
				direction = "down";
			}
			door = "closed";
			
			while(distance > 0){
				try {
//				assume 1s/floor
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} 
				if(direction.equalsIgnoreCase("up")){
					curFloor++;
				}else{
					curFloor--;
				}
				distance--;
			}
			door = "opened";
			System.out.printf("Arrived");
			System.out.printf("%s\n",curFloor);
			iterator.remove();
			try {
//				assume 0.5s/floor
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
			door = "closed";
		}
		
		idle = Boolean.TRUE;
	}
	
	public class Process implements Runnable {
		@Override
		public void run() {
			process();
		}
	}
	
}
