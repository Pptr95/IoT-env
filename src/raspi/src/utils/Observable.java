package utils;

import java.util.LinkedList;

import interfaces.Event;
import interfaces.Observer;

public class Observable {
	
	private LinkedList<Observer> observers;
	
	protected Observable(){
		observers = new LinkedList<Observer>();
	}
	
	protected void notifyEvent(Event ev){
		synchronized (observers){
			for (Observer obs: observers){
				obs.notifyEvent(ev);
			}
		}
	}

	public void addObserver(Observer obs){
		synchronized (observers){
			observers.add(obs);
		}
	}

	public void removeObserver(Observer obs){
		synchronized (observers){
			observers.remove(obs);
		}
	}

}
