package com.simulation;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DoctorThreadController {
    private final Lock lock = new ReentrantLock();
    ERPatientHandler erPatients;
    Thread[] dtThreads;

    //Constructor to start N no of Doctor threads and naming threads for better understanding
    DoctorThreadController(int noThreads, ERPatientHandler erPatients) {
        this.erPatients = erPatients;
        this.dtThreads = new Thread[noThreads];
        for (int i = 0; i < noThreads; i++) {
            //Trying to use lock mechanism to handle p0 patients on priority
            Condition p0condition = lock.newCondition();
            DoctorConsultThread dtThread = new DoctorConsultThread(erPatients.p0Que,
                    erPatients.p1Que, lock, p0condition);
            dtThreads[i] = new Thread(dtThread);
            dtThreads[i].setName("Doctor" + (i));
            dtThread.ThreadName = dtThreads[i].getName();
            dtThreads[i].start();
        }
    }

//    public void p0PatientController() {
////        Handle p0 patients - In progress
////        Thread.sleep(2000);
//        while (true) {
//            while (erPatients.p0Que.size() > 1) {
//                dtThreads[0].interrupt();
//            }
//        }
//    }

}
