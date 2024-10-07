package com.example.recycleapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.util.Log
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.core.content.FileProvider
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.channels.FileChannel


class MainActivity : ComponentActivity() {

    private lateinit var imgview:ImageView
    private lateinit var takenImage:Bitmap
    private lateinit var cooler:String
    private lateinit var photoFile: File


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imgview = findViewById(R.id.imageView)
        var cool = 0
        val fileName =  "coolshit.txt"
        val inputString = application.assets.open(fileName).bufferedReader().use {it.readText()}
        val townList = inputString.split("\n")
        val tv: TextView = findViewById(R.id.textView)
        val capture: Button = findViewById(R.id.button);

        capture.setOnClickListener {
            val kot: EditText = findViewById(R.id.editTextNumber)
            val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            photoFile = File.createTempFile("photo.jpg", ".jpg", storageDirectory)

            if (kot.text.length > 4) {
                cooler = kot.text.toString()
                val intent = Intent(ACTION_IMAGE_CAPTURE)

                val fileProvider = FileProvider.getUriForFile(
                    this,
                    "com.example.recycleapp.fileprovider",
                    photoFile
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
                if (intent.resolveActivity(this.packageManager) != null) {
                    startActivityForResult(intent, 100)
                    cool = 1
                } else {
                    Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
                }

            } else {
                tv.text = "Enter a county"
            }

        }

        val predict:Button = findViewById((R.id.button2))
        predict.setOnClickListener {
            if (cool == 1) {

                val fileDescriptor: AssetFileDescriptor = assets.openFd("model.tflite")
                val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
                val fileChannel: FileChannel = inputStream.channel
                val startOffset = fileDescriptor.startOffset
                val declaredLength = fileDescriptor.declaredLength
                val modelFile: ByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)

                val interpreter = Interpreter(modelFile)
                val resized: Bitmap = Bitmap.createScaledBitmap(takenImage, 224, 224, true)
                val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
                val byteBuffer = TensorImage.fromBitmap(resized).buffer

                inputFeature0.loadBuffer(byteBuffer)
                val outputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 7), DataType.FLOAT32)
                interpreter.run(inputFeature0.buffer, outputFeature0.buffer)

                val outputArray = outputFeature0.floatArray

                Log.d("MainActivity", "Output: ${outputArray.joinToString(", ")}")

                val max = outputArray.indices.maxByOrNull { outputArray[it] } ?: -1

                val coolest = "This was identified as ${townList[max]}, which is "

                if (townList[max].equals("plastic")) {
                    Log.d("MainActivity", "YEEEEEEEEEE")
                    when (cooler) {
                        "Adams", "Allegheny", "Armstrong", "Beaver", "Bedford", "Berks", "Bradford", "Bucks", "Butler", "Cambria", "Centre", "Chester", "Clarion", "Wayne", "Columbia", "Crawford", "Dauphin", "Delaware", "Elk", "Erie", "Fayette", "Forest", "Fulton", "Greene", "Huntingdon", "Indiana", "Jefferson", "Lackawana", "Lawrence", "Lehigh", "Luzerne", "Lycoming", "McKean", "Montgomery", "Montour", "Northampton", "Northumberland", "Perry", "Philadelphia", "Pike", "Potter", "Schuylkill", "Snyder", "Somerset", "Susquehanna", "Sullivan", "Union", "Venango", "Warren", "Washington", "Westmoreland", "Wyoming" -> tv.setText(
                            coolest + "acceptable where you live."
                        )

                        else -> tv.text = coolest + "not acceptable where you live."
                    }
                } else if (townList[max].equals("paper")) {
                    Log.d("MainActivity", "YEEEEEEEEEE")
                    when (cooler) {
                        "Adams", "Allegheny", "Armstrong", "Beaver", "Bedford", "Berks", "Buckhorn", "Cameron", "Bradford", "Bucks", "Butler", "Cambria", "Centre", "Chester", "Clarion", "Wayne", "Columbia", "Crawford", "Dauphin", "Delaware", "Elk", "Erie", "Fayette", "Franklin", "Juniata", "Fulton", "Greene", "Huntingdon", "Indiana", "Jefferson", "Lackawana", "Lawrence", "Lehigh", "Luzerne", "Lycoming", "McKean", "Montgomery", "Montour", "Northampton", "Northumberland", "Perry", "Philadelphia", "Pike", "Potter", "Schuylkill", "Snyder", "Somerset", "Susquehanna", "Sullivan", "Union", "Venango", "Warren", "Washington", "Westmoreland", "Wyoming" -> tv.setText(
                            coolest + "acceptable where you live."
                        )

                        else -> tv.text = coolest + "not acceptable where you live."
                    }

                } else if (townList[max].equals("cardboard")) {
                    Log.d("MainActivity", "YEEEEEEEEEE")
                    when (cooler) {
                        "Adams", "Allegheny", "Buckhorn", "Bradford", "Bucks", "Butler", "Cambria", "Chester", "Wayne", "Columbia", "Delaware", "Erie", "Fayette", "Franklin", "Juniata", "Fulton", "Greene", "Huntingdon", "Indiana", "Jefferson", "Lackawana", "Lawrence", "Lehigh", "Luzerne", "McKean", "Montgomery", "Montour", "Northampton", "Perry", "Philadelphia", "Pike", "Potter", "Snyder", "Somerset", "Susquehanna", "Union", "Warren", "Washington", "Westmoreland", "York" -> tv.setText(
                            coolest + "acceptable where you live."
                        )

                        else -> tv.text = coolest + "not acceptable where you live."
                    }
                } else if (townList[max].equals("styrofoam")) {
                    Log.d("MainActivity", "YEEEEEEEEEE")
                    tv.setText(coolest + "not acceptable where you live.")
                } else if (townList[max].equals("metal")) {
                    Log.d("MainActivity", "YEEEEEEEEEE")
                    when (cooler) {
                        "Adams", "Allegheny", "Beaver", "Bedford", "Berks", "Buckhorn", "Cameron", "Bradford", "Bucks", "Cumberland", "Butler", "Cambria", "Centre", "Chester", "Clearfield", "Clarion", "Wayne", "Columbia", "Dauphin", "Delaware", "Elk", "Erie", "Fayette", "Fulton", "Greene", "Huntingdon", "Indiana", "Jefferson", "Lackawana", "Lawrence", "Lehigh", "Luzerne", "Lycoming", "McKean", "Montgomery", "Montour", "Northampton", "Northumberland", "Philadelphia", "Pike", "Schuylkill", "Snyder", "Somerset", "Susquehanna", "Sullivan", "Union", "Venango", "Warren", "Washington", "York" -> tv.setText(
                            coolest + "acceptable where you live."
                        )

                        else -> tv.setText(coolest + "not acceptable where you live.")
                    }
                } else if (townList[max].equals("furniture")) {
                    Log.d("MainActivity", "YEEEEEEEEEE")
                    when (cooler) {
                        "Adams", "Allegheny", "Bucks", "Chester", "Cumberland", "Forest", "Greene", "Lackawana", "Lawrence", "Montgomery", "Philadelphia" -> tv.setText(
                            coolest + "acceptable where you live."
                        )

                        else -> tv.text = "not acceptable where you live."
                    }
                } else if (townList[max] == "glass") {
                    Log.d("MainActivity", "YEEEEEEEEEE")
                    when (cooler) {
                        "Allegheny", "Berks", "Buckhorn", "Bradford", "Bucks", "Centre", "Clarion", "Chester", "Cumberland", "Delaware", "Fayette", "Forest", "Fulton", "Greene", "Indiana", "Jefferson", "Lackawana", "Lawrence", "Montgomery", "Montour", "Northampton", "Northumberland", "Philadelphia", "Schuylkill", "Somerset", "Sullivan", "Susquehanna", "Union", "Warren" -> tv.setText(
                            coolest + "acceptable where you live."
                        )

                        else -> tv.text = "not acceptable where you live."
                    }

                }
                interpreter.close()
            } else {
                tv.text = "Take a picture first"
            }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100 && resultCode == RESULT_OK) {
            if (photoFile.exists()) {
                takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
                imgview.setImageBitmap(takenImage)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}