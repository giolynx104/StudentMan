package vn.edu.hust.studentman

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

class StudentFormFragment : Fragment() {
    private lateinit var nameEdit: EditText
    private lateinit var idEdit: EditText
    private lateinit var saveButton: Button
    private lateinit var navController: NavController
    private val args: StudentFormFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_student_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        nameEdit = view.findViewById(R.id.edit_student_name)
        idEdit = view.findViewById(R.id.edit_student_id)
        saveButton = view.findViewById(R.id.btn_save)

        // Set existing data if editing
        args.student?.let { student ->
            nameEdit.setText(student.studentName)
            idEdit.setText(student.studentId)
        }

        saveButton.setOnClickListener {
            val newStudent = StudentModel(
                nameEdit.text.toString(),
                idEdit.text.toString()
            )
            // Return result using Fragment Result API
            setFragmentResult("student_request", bundleOf(
                "student" to newStudent,
                "position" to args.position
            ))
            navController.navigateUp()
        }
    }
} 