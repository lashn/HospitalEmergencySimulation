package com.simulation;

import java.util.*;

//Used object-oriented approach to group Patients and there attributes
class PatientRecords {
    int patientNO, priority, arrivalTime, consultTime;

    PatientRecords(int patientNO, int priority, int arrivalTime, int consultTime) {
        this.patientNO = patientNO;
        this.priority = priority;
        this.arrivalTime = arrivalTime;
        this.consultTime = consultTime;
    }

    //Getter methods
    public int getPriority() {
        return priority;
    }

    public int getconsultTime() {
        return consultTime;
    }

    //Setter method to reset the consult time for a particular patient
    // when evicting a p1 patient in place of p0 patient
    public void resetConsultTime(int consultTime) {
        this.consultTime = consultTime;
    }
}

class PatientComparator implements Comparator<PatientRecords> {
    // Overriding compare()method of Comparator based on arrival time of the threads
    @Override
    public int compare(PatientRecords p1, PatientRecords p2) {
        //arrival time of new thread determines where to insert the new patient in the priority queue
        if (p1.arrivalTime > p2.arrivalTime) {
            return 1;
        }
        if (p1.arrivalTime < p2.arrivalTime) {
            return -1;
        }
        return 0;
    }
}

public class ERPatientHandler {
    PriorityQueue<PatientRecords> p0Que = new PriorityQueue<>(new PatientComparator());
    PriorityQueue<PatientRecords> p1Que = new PriorityQueue<>(new PatientComparator());

    ERPatientHandler() {
        //Initializing with a initial set of patient records
        PatientRecords P1 = new PatientRecords(1, 1, 0, 10000);
        PatientRecords P2 = new PatientRecords(2, 1, 1, 20000);
        PatientRecords P3 = new PatientRecords(3, 0, 3, 20000);
        PatientRecords P4 = new PatientRecords(4, 1, 3, 20000);

        List<PatientRecords> arrPatient = new ArrayList<>();
        Collections.addAll(arrPatient, P1, P2, P3, P4);

        //Fetch the initial patient details and send it to the patients queue
        for (PatientRecords patientRecord : arrPatient) {
            sendPatients(patientRecord);
        }
    }


    public void sendPatients(PatientRecords pr) {
        //Fetch the patient details and put it in appropriate queue based on priority
        if (pr.getPriority() == 0) {
            this.p0Que.add(pr);
            System.out.println("Incoming patientNo: " + pr.patientNO + " with priority: " + pr.priority);
            System.out.println("Please wait, you are at waiting number: " + p0Que.size());
            System.out.println("###################################################");
        } else {
            this.p1Que.add(pr);
            System.out.println("Incoming patientNo: " + pr.patientNO + " with priority: " + pr.priority);
            System.out.println("Please wait, you are at waiting number: " + (p1Que.size() + p0Que.size()));
            System.out.println("###################################################");
        }
    }

    //Helper function to iterate over a priority queue to find its order
    public void queueIterator(PriorityQueue pQue) {
        var pqItr = pQue.iterator();
        while (pqItr.hasNext()) {
            PatientRecords temp = (PatientRecords) pqItr.next();
            System.out.println("patientNO " + temp.patientNO +
                    " arrivalTime " + temp.arrivalTime + " priority " + temp.priority);
        }
    }
}


