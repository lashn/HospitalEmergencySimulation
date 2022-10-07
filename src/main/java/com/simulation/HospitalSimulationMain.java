package com.simulation;

public class HospitalSimulationMain {

    // Problem Statement:
    // Simulate scheduler for hospital emergency ward. At a given time, the emergency ward has a
    // fixed number of doctors. All the incoming patients will be provided with a tag depending upon
    // the severity of the emergency.
    //
    // P0: Needs immediate attention
    // P1: Not critical, can wait
    //
    // A patient with a more critical problem will preempt the other patients even if they have been
    // getting attended by a doctor or waiting for a longer duration. In case, there are many patients
    // with the same priority, then they will be attended to in the order of their arrival time.
    //
    // Example:
    // <Number of doctors>
    // N = 1
    // <Patient number, Severity, TimeTaken>
    //             [1, P1, 5 minutes] -> Output: “Doctor: 1 is attending Patient: 1”
    //             [2, P1, 2 minutes] -> Output: “Please wait, you are at waiting number: 1”
    //     After 5 minutes, Output: “Doctor 1 is attending Patient: 2”
    //             [3, P0, 10 minutes] -> Output: “Critical patient, Doctor: 1 is now attending Patient: 3”
    //             [4, P1, 7 minutes] -> Output: “Please wait, you are at waiting number: 2”
    //     After 10 minutes, Output: “Doctor: 1 is attending Patient: 2” (Re-examine from the beginning i.e.
    // for 2 minutes)
    //
    // 1.  Structured and Well documented code will have additional bonus marks.
    // 2.	Code should be executable and candidates should be able to give a demo in their environment
    // 3.	Send a requirements file for all the external dependencies added to your code
    // 4.	A couple of test cases and results output.
    //
    // Approach
    // 1. Implement 2 priority queue for P0 and P1 patients
    // 2. As soon as P0 is having an element, stop consuming P1 and use P0
    // 3. Pre-emptive scheme to put and get P0 patients at first - so frequent polling to look for P0 queue size
    //     - When serving a patient, instead of Polling, we can use Thread interrupts.
    // 4. Locking mechanism to not allow multiple threads while attending patients(removing the patient from the queue)
    // 5. As no of Doctors could be N and the patients also could be arbitary number - plan is to use Threading
    //     to allow asynchronous servicing of patients with N Doctors

    public static void main(String[] args) throws InterruptedException {
        //No of doctor Threads to invoke
        int N = 3;

        //Calling the patient handler class which manages the lifecycle of Patient
        ERPatientHandler er = new ERPatientHandler();

        //Calling the doctor Thread controller class which invokes N doctor threads
        new DoctorThreadController(N, er);

        //To test the priority processing of the Doctor thread controller
        // by adding one more P0 patient while doctors are already attending to other p0 and p1 patients
        Thread.sleep(2000);
        PatientRecords P5 = new PatientRecords(5, 0, 10, 20000);
        PatientRecords P6 = new PatientRecords(6, 1, 15, 10000);
        PatientRecords P7 = new PatientRecords(7, 0, 17, 10000);
        er.sendPatients(P5);
        er.sendPatients(P6);
        er.sendPatients(P7);
    }
}
