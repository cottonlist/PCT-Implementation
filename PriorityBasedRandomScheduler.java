import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.Random;

class Count {
    int cnt = 0;
}

public class PriorityBasedRandomScheduler {

    // Create random priority value for each thread
    private static int randomPriorityCreator(@NotNull int[] i) {
        Random generator = new Random();
        int randomIndex = generator.nextInt(i.length);
        return i[randomIndex];
    }

    // Decide which thread to execute by checking flag and termination
    private static void flagSetting(@NotNull boolean[] isTerminated, boolean[] flag, int[] priority){
        if (isTerminated[1]){
            flag[0] = true;
        } else if (isTerminated[0]){
            flag[1] = true;
        } else {
            if (priority[0] > priority[1]){
                flag[0] = true;
                flag[1] = false;
            } else {
                flag[0] = false;
                flag[1] = true;
            }
        }
    }

    // Change priority of corresponding thread when reaching priority change point
    private static void priorityChange(int count, int priorityChangePoint, int[] priority, int bugDepth, int id){
        if (count == priorityChangePoint){
            priority[id] = bugDepth - 1;
        }
    }

    // Enter infinite loop if don't want the thread to execute
    private static void infiniteLoop(@NotNull boolean[] flag, int id){
        while(!flag[id]){
            ;
        }
    }

    public static void main(String args[]){

        // Declare an integer to account the number of instructions executed
        Count count = new Count();

        // Assign the value of bug depth
        int bugDepth = 2;

        // Declare an array with n threads
        int numberOfThreads = 2;
        int[] threadList = new int[numberOfThreads];
        for(int i=0; i<threadList.length; i++){
            threadList[i] = i+1;
        }

        // Assign the number of instructions executed
        int numberOfInstructions = 8;

        // Randomly assign a priority value to each thread
        int[] priority = new int[numberOfThreads];
        priority[0] = randomPriorityCreator(threadList)+1;
        if (priority[0] == bugDepth + numberOfThreads - 1){
            priority[1] = priority[0] - 1;
        } else {
            priority[1] = priority[0] + 1;
        }
        System.out.println("Priority of thread 0 is " + priority[0]);
        System.out.println("Priority of thread 1 is " + priority[1]);

        // Randomly insert a priority change point
        int priorityChangePoint = (int)(Math.random()*numberOfInstructions + 1);
        System.out.println("The priority change point is after " + priorityChangePoint + " instruction(s)");

        // Schedule thread with higher priority to execute first
        boolean[] flag = new boolean[numberOfThreads];
        if (priority[0] > priority[1]){
            flag[0] = true;
            flag[1] = false;
        } else {
            flag[0] = false;
            flag[1] = true;
        }

        // Create boolean array to indicate the termination of a thread
        boolean[] isTerminated = new boolean[numberOfThreads];
        for(int i = 0; i < numberOfThreads; i++){
            isTerminated[i] = false;
        }

        Thread t0 = new Thread(){
            @Override
            public void run() {

                int id = 0;

                infiniteLoop(flag, id);
                System.out.println("instruction 0-1");
                count.cnt++;
                priorityChange(count.cnt, priorityChangePoint, priority, bugDepth, id);
                flagSetting(isTerminated, flag, priority);

                infiniteLoop(flag, id);
                System.out.println("instruction 0-2");
                count.cnt++;
                priorityChange(count.cnt, priorityChangePoint, priority, bugDepth, id);
                flagSetting(isTerminated, flag, priority);

                infiniteLoop(flag, id);
                System.out.println("instruction 0-3");
                count.cnt++;
                priorityChange(count.cnt, priorityChangePoint, priority, bugDepth, id);
                flagSetting(isTerminated, flag, priority);

                infiniteLoop(flag, id);
                System.out.println("instruction 0-4");
                count.cnt++;
                isTerminated[id] = true;
                priorityChange(count.cnt, priorityChangePoint, priority, bugDepth, id);
                flag[1] = true;
            }
        };

        Thread t1 = new Thread(){
            @Override
            public void run() {
                int id = 1;

                infiniteLoop(flag, id);
                System.out.println("instruction 1-1");
                count.cnt++;
                priorityChange(count.cnt, priorityChangePoint, priority, bugDepth, id);
                flagSetting(isTerminated, flag, priority);

                infiniteLoop(flag, id);
                System.out.println("instruction 1-2");
                count.cnt++;
                priorityChange(count.cnt, priorityChangePoint, priority, bugDepth, id);
                flagSetting(isTerminated, flag, priority);

                infiniteLoop(flag, id);
                System.out.println("instruction 1-3");
                count.cnt++;
                priorityChange(count.cnt, priorityChangePoint, priority, bugDepth, id);
                flagSetting(isTerminated, flag, priority);

                infiniteLoop(flag, id);
                System.out.println("instruction 1-4");
                count.cnt++;
                isTerminated[id] = true;
                priorityChange(count.cnt, priorityChangePoint, priority, bugDepth, id);
                flag[0] = true;
            }
        };

        t0.start();
        t1.start();
    }

}
