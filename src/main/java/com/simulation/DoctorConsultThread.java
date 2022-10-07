package com.simulation;

import java.util.PriorityQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class DoctorConsultThread implements Runnable {
    public String ThreadName;
    PriorityQueue<PatientRecords> p0que;
    PriorityQueue<PatientRecords> p1que;
    public Lock lock;
    int consultTime;
    PatientRecords pr;

    DoctorConsultThread(PriorityQueue<PatientRecords> p0que, PriorityQueue<PatientRecords> p1que,
                        Lock lock, Condition p0condition) {
        this.p0que = p0que;
        this.p1que = p1que;
        this.lock = lock;
    }


    public void p0Patient() {
        //synchronized block to make sure no two threads tries to take same patient out of the queue at the same time
        lock.lock();
        this.pr = p0que.remove();
        lock.unlock();


        this.consultTime = pr.getconsultTime();
        try {
            System.out.println(ThreadName + " is attending Patient: "
                    + pr.patientNO + " with priority: " + pr.priority);
            //Doctor checking the patient till the consult time
            Thread.sleep(consultTime);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(ThreadName
                    + " completed attending Patient: " + pr.patientNO);
        }
    }


    public void p1Patient() {
        //to track the consultation time taken by p1Patient, will be used to decrement the consult time on interrupt
        long startTime = System.currentTimeMillis();
        //synchronized block to make sure no two threads tries to take same patient out of the queue at the same time
        lock.lock();
        this.pr = p1que.remove();
        lock.unlock();
        this.consultTime = pr.getconsultTime();
        try {
            System.out.println(ThreadName + " is attending Patient: "
                    + pr.patientNO + " with priority: " + pr.priority);
            //Doctor checking the patient till the consult time
            //class static counter++
            Thread.sleep(pr.consultTime);
            //class static counter--
        } catch (InterruptedException e) {
            e.printStackTrace();
            //To receive interrupt and put the current p1 patient back to queue
            System.out.println(ThreadName + "...notified Patient: "+ pr.patientNO);
            //new consult time to be used for reset of p1 patients
            this.consultTime = consultTime - (int) startTime;
        } finally {
            System.out.println(ThreadName + " completed attending Patient: "
                    + pr.patientNO);
        }
    }

    //overriding the run method of Thread with the patient processing block
    @Override
    public void run() {
        System.out.println(ThreadName + " in the hospital");
        //wait for the incoming patient
        while (true) {
            //class variable - no of doctors entered
            //check whether either of the queue is having patients to process
            while (this.p0que.size() > 0 || p1que.size() > 0) {
                System.out.println("Sending the next patient to " + ThreadName);
                //call the P0 patient processing
                while (!p0que.isEmpty()) {
                    p0Patient();
                    //if p0 queue is not empty, this thread can keep taking up available p0 threads
                }

                //call the P1 patient processing only if P0 is empty after attending P1 patient
                //this loop will start by checking of any new p0 patients
                while (p0que.isEmpty() && !p1que.isEmpty()) {
                    p1Patient();
                }
            }
        }
    }
}
