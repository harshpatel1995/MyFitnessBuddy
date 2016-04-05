package squad.myfitnessbuddy;

//This class stores set data. A workout is a list of sets
public class ExerciseSet {

    private String workoutNameStr, exerciseStr;
    private int repsInt, weightInt;


    public ExerciseSet(String workoutName, String exercise, int reps, int weight)
    {
        workoutNameStr = workoutName;
        exerciseStr = exercise;
        repsInt = reps;
        weightInt = weight;
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
