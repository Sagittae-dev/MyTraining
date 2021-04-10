package com.example.mytraining.choosenexercise

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mytraining.R
import com.example.mytraining.newtraining.ChoosenSpecificExerciseItem
import com.example.mytraining.newtraining.TypeOfChoosenExercise
import kotlinx.android.synthetic.main.activity_choose_specific_type_exercise.*

class ChooseSpecificTypeExerciseActivity : AppCompatActivity(), ChooseSpecificExerciseRecyclerViewAdapter.OnChoosenExerciseItemClickListener {

    val exercisesListToChoose = arrayListOf<ChoosenSpecificExerciseItem>()
    var squatsList: MutableList<ChoosenSpecificExerciseItem> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_specific_type_exercise)
        val title = intent.getStringExtra("exerciseTag")
        chooseSpecificExerciseTitleTextView.text = title
        specificExercisesRecyclerView.layoutManager = LinearLayoutManager(this)
        specificExercisesRecyclerView.adapter = ChooseSpecificExerciseRecyclerViewAdapter(exercisesListToChoose, this)
        val typeExercise = intent.getStringExtra("exerciseTag")!!
        setUpExercisesLists(typeExercise)
        specificExercisesRecyclerView.adapter?.notifyDataSetChanged()

        val item = ChoosenSpecificExerciseItem("fajna", R.drawable.clock,"oqudhoq", TypeOfChoosenExercise.SQUATS)
        Log.i("int orazka", R.drawable.clock.toString())
        Log.i("dlugosc listy", exercisesListToChoose.size.toString())
        
    }

    private fun setUpExercisesLists(typeExercise: String) {
        if (typeExercise == "squats") {
            exercisesListToChoose.add(
                ChoosenSpecificExerciseItem(
                    "Back squat",
                    R.drawable.dumbel_icon_small_no_background,
                    "The back squat is the most basic strength exercise in weightlifting. Place the barbell behind your neck—retract your shoulder blades tightly and rest the bar in the meat of your upper traps.",
                    TypeOfChoosenExercise.SQUATS
                )
            )
            exercisesListToChoose.add(
                ChoosenSpecificExerciseItem(
                    "Front squat",
                    R.drawable.dumbel_icon_small_no_background,
                    "Front Squats are similar to Back Squats, however the barbell is placed across the front side of your shoulders instead of your upper back.",
                    TypeOfChoosenExercise.SQUATS
                )
            )
            exercisesListToChoose.add(
                ChoosenSpecificExerciseItem(
                    "Side squat",
                    R.drawable.dumbel_icon_small_no_background,
                    "The side squat is similar to a regular squat combined with a side step / jump at the top of each squat.",
                    TypeOfChoosenExercise.SQUATS
                )
            )
            exercisesListToChoose.add(
                ChoosenSpecificExerciseItem(
                    "Goblet squat",
                    R.drawable.dumbel_icon_small_no_background,
                    "The Goblet Squat is a lower-body exercise in which you hold a dumbbell or kettlebell with both hands in front of your chest.",
                    TypeOfChoosenExercise.SQUATS
                )
            )
            exercisesListToChoose.add(
                ChoosenSpecificExerciseItem(
                    "Smith squat",
                    R.drawable.dumbel_icon_small_no_background,
                    "The Smith machine squat is a variation of the squat and an exercise used to build the muscles of the legs. In particular, the Smith machine squat will place a lot of emphasis on the quads.",
                    TypeOfChoosenExercise.SQUATS
                )
            )
            exercisesListToChoose.add(
                ChoosenSpecificExerciseItem(
                    "Zercher squat",
                    R.drawable.dumbel_icon_small_no_background,
                    "The Zercher squat places the barbell in the crease of the elbow where the lifter 'hugs' the weight as close to their body as possible.",
                    TypeOfChoosenExercise.SQUATS
                )
            )
            exercisesListToChoose.add(
                ChoosenSpecificExerciseItem(
                    "Overhead squat",
                    R.drawable.dumbel_icon_small_no_background,
                    "The overhead squat is a total-body squatting movement that can increase upper back, shoulder, and core strength, while also reinforcing proper squatting technique.",
                    TypeOfChoosenExercise.SQUATS
                )
            )
        }
        if (typeExercise == "deadlifts") {
            exercisesListToChoose.add(
                ChoosenSpecificExerciseItem(
                    "Classic deadlift",
                    R.drawable.dumbel_icon_small_no_background,
                    "The deadlift is a weight training exercise in which a loaded barbell or bar is lifted off the ground to the level of the hips, torso perpendicular to the floor, before being placed back on the ground. It is one of the three powerlifting exercises, along with the squat and bench press.",
                    TypeOfChoosenExercise.SQUATS
                )
            )
            exercisesListToChoose.add(
                ChoosenSpecificExerciseItem(
                    "Sumo deadlift",
                    R.drawable.dumbel_icon_small_no_background,
                    "The Sumo Deadlift is a variation that emphasizes more on the use of your legs to squat the weight up rather than your hips and back. " +
                            "With this style, your hips are closer to the bar compared to a conventional deadlift with a more vertical torso, which takes the stress off of your lower back and places it on your legs.",
                    TypeOfChoosenExercise.SQUATS
                )
            )
            exercisesListToChoose.add(
                ChoosenSpecificExerciseItem(
                    "Romanian deadlift",
                    R.drawable.dumbel_icon_small_no_background,
                    "The Romanian deadlift (RDL) is a traditional barbell lift used to develop the strength of the posterior chain muscles, including the erector spinae, gluteus maximus, hamstrings and adductors.",
                    TypeOfChoosenExercise.SQUATS
                )
            )
            exercisesListToChoose.add(
                ChoosenSpecificExerciseItem(
                    "Straight-legged deadlift",
                    R.drawable.dumbel_icon_small_no_background,
                    "The straight-legged deadlift is one of the few exercises in weightlifting in which the back is actually flexed and extended rather than held in a static extended position.",
                    TypeOfChoosenExercise.SQUATS
                )
            )
            exercisesListToChoose.add(
                ChoosenSpecificExerciseItem(
                    "Trap bar deadlift",
                    R.drawable.dumbel_icon_small_no_background,
                    "The Trap Bar Deadlift is a full-body exercise that targets the hips and legs. It's a variation of the traditional Deadlift that uses a trap bar, which is a hexagon-shaped bar that surrounds the lifter.",
                    TypeOfChoosenExercise.SQUATS
                )
            )
        }
        if (typeExercise == "chestex") {
            exercisesListToChoose.add(
                ChoosenSpecificExerciseItem(
                    "Bench press",
                    R.drawable.dumbel_icon_small_no_background,
                    "The bench press is an upper-body weight training exercise in which the trainee presses a weight upwards while lying on a weight training bench.",
                    TypeOfChoosenExercise.SQUATS
                )
            )
            exercisesListToChoose.add(
                ChoosenSpecificExerciseItem(
                    "Dumbbell Bench Press",
                    R.drawable.dumbel_icon_small_no_background,
                    "Lift the dumbbells to chest height with your palms facing forwards.",
                    TypeOfChoosenExercise.SQUATS
                )
            )
            exercisesListToChoose.add(
                ChoosenSpecificExerciseItem(
                    "Incline Bench Press",
                    R.drawable.dumbel_icon_small_no_background,
                    "The Incline Bench Press is a version of the traditional Bench Press in which the bench is positioned at about a 45-degree angle.",
                    TypeOfChoosenExercise.SQUATS
                )
            )
            exercisesListToChoose.add(
                ChoosenSpecificExerciseItem(
                    "Decline Bench Press",
                    R.drawable.dumbel_icon_small_no_background,
                    "In a decline bench press, the bench is set to 15 to 30 degrees on a decline.",
                    TypeOfChoosenExercise.SQUATS
                )
            )
            exercisesListToChoose.add(
                ChoosenSpecificExerciseItem(
                    "Incline Dumbbell Bench Press",
                    R.drawable.dumbel_icon_small_no_background,
                    "In a incline dumbell bench press, the bench is set to 15 to 30 degrees on a decline and You use dumbells.",
                    TypeOfChoosenExercise.SQUATS
                )
            )
            exercisesListToChoose.add(
                ChoosenSpecificExerciseItem(
                    "Decline Dumbbell Bench Press",
                    R.drawable.dumbel_icon_small_no_background,
                    "In a decline dumbell bench press, the bench is the bench is positioned at about a 45-degree angle and You use dumbells.",
                    TypeOfChoosenExercise.SQUATS
                )
            )
            exercisesListToChoose.add(
                ChoosenSpecificExerciseItem(
                    "Dumbbell Flys",
                    R.drawable.dumbel_icon_small_no_background,
                    "There should be a slight bend at your elbow, and your palms and dumbbells should be facing each other. Your arms will be extended to the sides but not locked out.",
                    TypeOfChoosenExercise.SQUATS
                )
            )
            exercisesListToChoose.add(
                ChoosenSpecificExerciseItem(
                    "Incline Dumbbell Flys",
                    R.drawable.dumbel_icon_small_no_background,
                    "You do it like a normal dumbel flys. The bench is the bench is positioned at about a 45-degree angle. ",
                    TypeOfChoosenExercise.SQUATS
                )
            )
            exercisesListToChoose.add(
                ChoosenSpecificExerciseItem(
                    "Decline dumbbell Flys",
                    R.drawable.dumbel_icon_small_no_background,
                    "You do it like a normal dumbel flys. The bench is set to 15 to 30 degrees on a decline ",
                    TypeOfChoosenExercise.SQUATS
                )
            )
            exercisesListToChoose.add(
                ChoosenSpecificExerciseItem(
                    "Chest Dip",
                    R.drawable.dumbel_icon_small_no_background,
                    "Lean forward. Put your elbows a little bit wider than your shoulders. Move knees slightly forward, so your body is shaped like a \"C\" Contract your abs to maintain the position. Keep your head in line with your spine as you go down. ",
                    TypeOfChoosenExercise.SQUATS
                )
            )
            exercisesListToChoose.add(
                ChoosenSpecificExerciseItem(
                    "Smith Machine Bench Press",
                    R.drawable.dumbel_icon_small_no_background,
                    "Position a flat bench below the barbell of a Smith Machine. Lie on the bench face up and place your hands slightly outside of shoulder width on the barbell.",
                    TypeOfChoosenExercise.SQUATS
                )
            )
        }
            if(typeExercise == "backex"){
                exercisesListToChoose.add(
                    ChoosenSpecificExerciseItem(
                        "Wide Grip Pull Up",
                        R.drawable.dumbel_icon_small_no_background,
                        "The wide-grip pullup is an upper-body strength movement that targets your back, chest, shoulders, and arms.",
                        TypeOfChoosenExercise.SQUATS
                    )
                )
                exercisesListToChoose.add(
                    ChoosenSpecificExerciseItem(
                        "Chin Up",
                        R.drawable.dumbel_icon_small_no_background,
                        "This is a strength training exercise with the intention of strengthening muscles such as the latissimus dorsi and biceps.",
                        TypeOfChoosenExercise.SQUATS
                    )
                )
                exercisesListToChoose.add(
                    ChoosenSpecificExerciseItem(
                        "Bent Over Row",
                        R.drawable.dumbel_icon_small_no_background,
                        "This is a exercise with the intention of strengthening muscles such as the latissimus dorsi and biceps.",
                        TypeOfChoosenExercise.SQUATS
                    )
                )
                exercisesListToChoose.add(
                    ChoosenSpecificExerciseItem(
                        "One Arm Dumbbell Row",
                        R.drawable.dumbel_icon_small_no_background,
                        "This is a exercise similar to Bent Over Row but unilaterallly and with using dumbell.",
                        TypeOfChoosenExercise.SQUATS
                    )
                )
                exercisesListToChoose.add(
                    ChoosenSpecificExerciseItem(
                        "Lat Pull Down",
                        R.drawable.dumbel_icon_small_no_background,
                        "In this exercise You have to pull down the bar uses a weight machine",
                        TypeOfChoosenExercise.SQUATS
                    )
                )
                exercisesListToChoose.add(
                    ChoosenSpecificExerciseItem(
                        "Reverse Grip Lat Pull Down",
                        R.drawable.dumbel_icon_small_no_background,
                        "Exercise like pull down the bar uses a weight machine",
                        TypeOfChoosenExercise.SQUATS
                    )
                )
                exercisesListToChoose.add(
                    ChoosenSpecificExerciseItem(
                        "Good Mornings",
                        R.drawable.dumbel_icon_small_no_background,
                        "Exercise similar to back squat. Bar behind the head and move similar to deadlift.",
                        TypeOfChoosenExercise.SQUATS
                    )
                )
                exercisesListToChoose.add(
                    ChoosenSpecificExerciseItem(
                        "Barbell Shrug",
                        R.drawable.dumbel_icon_small_no_background,
                        "Stand tall, holding a bar in an overhand grip with your hands just outside your thighs. Lift your shoulders straight up, hold for one or two seconds in this elevated position, then lower them back to the start",
                        TypeOfChoosenExercise.SQUATS
                    )
                )
                exercisesListToChoose.add(
                    ChoosenSpecificExerciseItem(
                        "Dumbell Shrug",
                        R.drawable.dumbel_icon_small_no_background,
                        "Exercise like barbel shrug but with two dumbell in hands.",
                        TypeOfChoosenExercise.SQUATS
                    )
                )
            }
            if (typeExercise == "shouldersex"){
                exercisesListToChoose.add(
                    ChoosenSpecificExerciseItem(
                        "Military Press - OHP",
                        R.drawable.dumbel_icon_small_no_background,
                        "Exercise targets the deltoid muscles, shoulders and triceps. ",
                        TypeOfChoosenExercise.SQUATS
                    )
                )
                exercisesListToChoose.add(
                    ChoosenSpecificExerciseItem(
                        "Barbell Press Behind Neck",
                        R.drawable.dumbel_icon_small_no_background,
                        "Exercise similar to OHP but press bar from behind neck. ",
                        TypeOfChoosenExercise.SQUATS
                    )
                )
                exercisesListToChoose.add(
                    ChoosenSpecificExerciseItem(
                        "Dumbbell Shoulder Press",
                        R.drawable.dumbel_icon_small_no_background,
                        "Exercise similar to OHP but press dumbells. ",
                        TypeOfChoosenExercise.SQUATS
                    )
                )
                exercisesListToChoose.add(
                    ChoosenSpecificExerciseItem(
                        "Arnold Press",
                        R.drawable.dumbel_icon_small_no_background,
                        "Hold a dumbbell in each hand with your arm bent, as in the top of a biceps curl, so your palms are facing you. Now, instead of pushing straight up, spread your arms to each side laterally, then press your arms up and twist your hands so your palms face forwards.",
                        TypeOfChoosenExercise.SQUATS
                    )
                )
                exercisesListToChoose.add(
                    ChoosenSpecificExerciseItem(
                        "Dumbbell Shoulder Press",
                        R.drawable.dumbel_icon_small_no_background,
                        "Stand or sit with a dumbbell in each hand at your sides. Keep your back straight, brace your core, and then slowly lift the weights out to the side until your arms are parallel with the floor, with the elbow slightly bent.",
                        TypeOfChoosenExercise.SQUATS
                    )
                )
            }
            if (typeExercise == "armsex"){
                exercisesListToChoose.add(
                    ChoosenSpecificExerciseItem(
                        "Standing Barbell Curl - Biceps",
                        R.drawable.dumbel_icon_small_no_background,
                        "Keeping your eyes facing forwards, elbows tucked in at your sides, and your body completely still, slowly curl the bar up. Squeeze your biceps hard at the top of the movement, and then slowly lower it back to the starting position.",
                        TypeOfChoosenExercise.SQUATS
                    )
                )
                exercisesListToChoose.add(
                    ChoosenSpecificExerciseItem(
                        "Standing Dumbell Curl - Biceps",
                        R.drawable.dumbel_icon_small_no_background,
                        "Exercise similar to OHP but press dumbells. ",
                        TypeOfChoosenExercise.SQUATS
                    )
                )
                exercisesListToChoose.add(
                    ChoosenSpecificExerciseItem(
                        "Cable Tricep Extension",
                        R.drawable.dumbel_icon_small_no_background,
                        "The single-arm cable triceps extension is a single-joint isolation exercise for building the triceps. It involves driving a handle attached to a cable stack overhead to full extension.",
                        TypeOfChoosenExercise.SQUATS
                    )
                )
            }
            if (typeExercise == "buttocksex"){
                exercisesListToChoose.add(
                    ChoosenSpecificExerciseItem(
                        "Hip Thrust",
                        R.drawable.dumbel_icon_small_no_background,
                        "The Hip Thrust is a glute exercise designed to improve your strength, speed and power by teaching optimal hip extension. ",
                        TypeOfChoosenExercise.SQUATS
                    )
                )
                exercisesListToChoose.add(
                    ChoosenSpecificExerciseItem(
                        "Single leg Hip Thrust",
                        R.drawable.dumbel_icon_small_no_background,
                        "Hold one leg held at a 90 degree angle at the hip and drive the other foot into the floor to bridge your hips up while squeezing your glute. Lower your hips back to the starting position and repeat for the desired number of repetitions. Repeat on both sides.",
                        TypeOfChoosenExercise.SQUATS
                    )
                )
            }
            if (typeExercise == "bodyst") {
                exercisesListToChoose.add(
                    ChoosenSpecificExerciseItem(
                        "Plank",
                        R.drawable.dumbel_icon_small_no_background,
                        "The plank exercise is an isometric core exercise that involves maintaining a position similar to a push-up for the maximum possible time. ",
                        TypeOfChoosenExercise.SQUATS
                    )
                )
                exercisesListToChoose.add(
                    ChoosenSpecificExerciseItem(
                        "Side Plank",
                        R.drawable.dumbel_icon_small_no_background,
                        "Start on your side with your feet together and one forearm directly below your shoulder. Contract your core and raise your hips until your body is in a straight line from head to feet. Hold the position without letting your hips drop for the allotted time for each set ",
                        TypeOfChoosenExercise.SQUATS
                    )
                )
            }
    }

    override fun onItemClick(item: ChoosenSpecificExerciseItem, position: Int) {
        //todo dodac co ma sie wydarzyc po kliknieciu na cwiczenie
    }
}