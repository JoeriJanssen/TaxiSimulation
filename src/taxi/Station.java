package taxi;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Class that holds the number of persons arriving by train at the station and
 * waiting for a taxi
 */
public class Station {

    private int nrOfPassengersAtStation = 0;
    private int totalNrOfPassengers = 0;
    private boolean isClosed = false;
    private final Lock myLock = new ReentrantLock();
    private final Condition emptyStation = myLock.newCondition();
    private final Condition filledStation = myLock.newCondition();
    
    public void enterStation(int nrOfPassengers) throws InterruptedException {
        myLock.lock();
        while (nrOfPassengersAtStation > 0){
            emptyStation.await();
        }
        nrOfPassengersAtStation += nrOfPassengers;
        totalNrOfPassengers += nrOfPassengers;
        filledStation.signalAll();
        System.out.println(nrOfPassengers + " passengers arrived at station");
        myLock.unlock();
    }
    /**
     * Ask for nrOfPassengers Passengers to leave the station
     *
     * @param requestedNrOfPassengers
     * @return number of passengers actually leaving
     */
    public int leaveStation(int requestedNrOfPassengers) throws InterruptedException {
        
        myLock.lock();
        try {
            while (nrOfPassengersAtStation == 0){
                filledStation.await();
            }
            int actuallyLeaving = Math.min(requestedNrOfPassengers, nrOfPassengersAtStation);
            nrOfPassengersAtStation -= actuallyLeaving;
            emptyStation.signalAll();
            return actuallyLeaving;
        } finally {
            myLock.unlock();
        }
       
    }

    public synchronized int waitingPassengers() {
        int number;
        myLock.lock();
        try {
            number = nrOfPassengersAtStation;
        } finally {
            myLock.unlock();
        }
        return number;
        
    }
    
    public void close() {
        isClosed = true;
    }
    
    public boolean isClosed() {
        return isClosed;     
    }

    public int getTotalNrOfPassengers() {
        return totalNrOfPassengers;
    }
}