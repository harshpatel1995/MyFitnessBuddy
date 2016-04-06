package squad.myfitnessbuddy;

//This class stores set data. A workout is a list of sets
public class ExerciseSet implements Comparable<ExerciseSet>
{

    private String workoutNameStr, exerciseStr;
    private int repsInt, weightInt;


    public ExerciseSet(String workoutName, String exercise, int reps, int weight)
    {
        workoutNameStr = workoutName;
        exerciseStr = exercise;
        repsInt = reps;
        weightInt = weight;
    }


    @Override
    public int compareTo(ExerciseSet setObj)
    //compares by workout, then exercise, then weight, then reps
    {
        //workout name less than
        if(this.getWorkoutName().compareTo(setObj.getWorkoutName()) < 0)
        {
            return -1;
        }
        //workout name greater than
       else if(this.getWorkoutName().compareTo(setObj.getWorkoutName()) > 0)
        {
            return 1;
        }
        else{
            //exercise less than
            if(this.getExercise().compareTo(setObj.getExercise()) < 0)
            {
                return -1;
            }
            //exercise greater than
            else if(this.getExercise().compareTo(setObj.getExercise()) > 0)
            {
                return 1;
            }
            else{
                 //weight less than
                 if(this.getWeight() < setObj.getWeight())
                 {
                     return -1;
                 }
                 //weight greater than
                else if(this.getWeight() > setObj.getWeight())
                 {
                     return 1;
                 }
                else  {
                     //reps less than
                     if(this.getReps() < setObj.getReps())
                     {
                         return -1;
                     }
                     //reps greater than
                     else if(this.getReps() > setObj.getReps())
                     {
                         return 1;
                     }
                     else{
                         return 0;
                     }
                }

             }

        }

    }

    public String getWorkoutName() {
        return workoutNameStr;
    }

    public void setWorkoutName(String workoutName) {
        workoutNameStr = workoutName;
    }


    public String getExercise() {
        return exerciseStr;
    }

    public void setExerciseS(String exercise) {
        exerciseStr = exercise;
    }

    public int getReps() {
        return repsInt;
    }

    public void setReps(int reps) {
        repsInt = reps;
    }

    public int getWeight() {
        return weightInt;
    }

    public void setWeight(int weight) {
        weightInt = weight;
    }




}
