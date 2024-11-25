package vn.edu.hust.studentman

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class StudentFormActivity : AppCompatActivity() {
    private lateinit var nameEdit: EditText
    private lateinit var idEdit: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_form)

        nameEdit = findViewById(R.id.edit_student_name)
        idEdit = findViewById(R.id.edit_student_id)
        saveButton = findViewById(R.id.btn_save)

        // Get student data if in edit mode
        val student = intent.getParcelableExtra<StudentModel>("student")
        if (student != null) {
            nameEdit.setText(student.studentName)
            idEdit.setText(student.studentId)
            title = "Edit Student"
        } else {
            title = "Add New Student"
        }

        saveButton.setOnClickListener {
            val newStudent = StudentModel(
                nameEdit.text.toString(),
                idEdit.text.toString()
            )
            intent.putExtra("result_student", newStudent)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
} 