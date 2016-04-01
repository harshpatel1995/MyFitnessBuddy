package squad.myfitnessbuddy;

//This class should be used to hold system wide constant for the app
public class ConstantValues {


    public static final String cCREATE_OR_OPEN_SAVED_WORKOUTS_DATABASE_SQL =
            "CREATE TABLE IF NOT EXISTS savedWorkouts (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL UNIQUE, exercises TEXT NOT NULL)";


}
