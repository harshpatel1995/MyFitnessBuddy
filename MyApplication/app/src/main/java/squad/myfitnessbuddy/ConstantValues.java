package squad.myfitnessbuddy;

//This class should be used to hold system wide constant for the app
public class ConstantValues {


    public static final String cCREATE_OR_OPEN_SAVED_WORKOUTS_DATABASE_SQL =
            "CREATE TABLE IF NOT EXISTS savedWorkouts (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL UNIQUE, exercises TEXT NOT NULL)";

    public static final String cCREATE_OR_OPEN_WORKOUT_LOGS_DATABASE_SQL =
            "CREATE TABLE IF NOT EXISTS logs (_id INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT NOT NULL, exercise TEXT NOT NULL, reps INTEGER CHECK(reps>0), weight INTEGER CHECK(weight>0))";

    public static final String cSP_FIRST_NAME = "First Name";
    public static final String cSP_LAST_NAME = "Last Name";
    public static final String cSP_AGE = "Age";
    public static final String cSP_HEIGHT = "Height";
    public static final String cSP_WEIGHT = "Weight";
    public static final String cSP_GENDER = "Gender";
    public static final String cSP_BMI = "BMI";
    public static final String cSP_SEDENTARY = "Sedentary";
    public static final String cSP_LIGHTLY_ACTIVE = "Lightly Active";
    public static final String cSP_MODERATELY_ACTIVE = "Moderately Active";
    public static final String cSP_VERY_ACTIVE = "Very Active";
    public static final String cSP_EXTREMELY_ACTIVE = "Extremely Active";

    public static final String cSP_FROM_USER_PROFILE = "fromUserProfile";
    public static final String cSP_PREVIEW_WORKOUT = "Preview Workout";
    public static final String cSP_STARTED_WORKOUT = "Started Workout";
    public static final String cSP_IS_PREVIEW_FOR_PREDEFINED = "Preview For Predefined Workout";


}
