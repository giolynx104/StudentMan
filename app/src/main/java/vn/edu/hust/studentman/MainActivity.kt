package vn.edu.hust.studentman

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
  private lateinit var studentAdapter: StudentAdapter
  private var lastDeletedStudent: StudentModel? = null
  private var lastDeletedPosition: Int = -1

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val students = mutableListOf(
      StudentModel("Nguyễn Văn An", "SV001"),
      StudentModel("Trần Thị Bảo", "SV002"),
      StudentModel("Lê Hoàng Cường", "SV003"),
      StudentModel("Phạm Thị Dung", "SV004"),
      StudentModel("Đỗ Minh Đức", "SV005"),
      StudentModel("Vũ Thị Hoa", "SV006"),
      StudentModel("Hoàng Văn Hải", "SV007"),
      StudentModel("Bùi Thị Hạnh", "SV008"),
      StudentModel("Đinh Văn Hùng", "SV009"),
      StudentModel("Nguyễn Thị Linh", "SV010"),
      StudentModel("Phạm Văn Long", "SV011"),
      StudentModel("Trần Thị Mai", "SV012"),
      StudentModel("Lê Thị Ngọc", "SV013"),
      StudentModel("Vũ Văn Nam", "SV014"),
      StudentModel("Hoàng Thị Phương", "SV015"),
      StudentModel("Đỗ Văn Quân", "SV016"),
      StudentModel("Nguyễn Thị Thu", "SV017"),
      StudentModel("Trần Văn Tài", "SV018"),
      StudentModel("Phạm Thị Tuyết", "SV019"),
      StudentModel("Lê Văn Vũ", "SV020")
    )

    studentAdapter = StudentAdapter(students)

    findViewById<RecyclerView>(R.id.recycler_view_students).run {
      adapter = studentAdapter
      layoutManager = LinearLayoutManager(this@MainActivity)
    }

    findViewById<Button>(R.id.btn_add_new).setOnClickListener {
      showAddEditDialog()
    }

    studentAdapter.onEditClick = { student, position ->
      showAddEditDialog(student, position)
    }

    studentAdapter.onDeleteClick = { student, position ->
      showDeleteConfirmation(student, position)
    }
  }

  private fun showAddEditDialog(student: StudentModel? = null, position: Int = -1) {
    val dialogView = layoutInflater.inflate(R.layout.dialog_student, null)
    val nameEdit = dialogView.findViewById<EditText>(R.id.edit_student_name)
    val idEdit = dialogView.findViewById<EditText>(R.id.edit_student_id)

    if (student != null) {
      nameEdit.setText(student.studentName)
      idEdit.setText(student.studentId)
    }

    MaterialAlertDialogBuilder(this)
      .setTitle(if (student == null) "Thêm sinh viên" else "Sửa sinh viên")
      .setView(dialogView)
      .setPositiveButton("Lưu") { _, _ ->
        val newStudent = StudentModel(
          nameEdit.text.toString(),
          idEdit.text.toString()
        )
        if (student == null) {
          studentAdapter.addStudent(newStudent)
        } else {
          studentAdapter.updateStudent(newStudent, position)
        }
      }
      .setNegativeButton("Hủy", null)
      .show()
  }

  private fun showDeleteConfirmation(student: StudentModel, position: Int) {
    MaterialAlertDialogBuilder(this)
      .setTitle("Xóa sinh viên")
      .setMessage("Bạn có chắc chắn muốn xóa sinh viên ${student.studentName}?")
      .setPositiveButton("Xóa") { _, _ ->
        lastDeletedStudent = studentAdapter.removeStudent(position)
        lastDeletedPosition = position
        showUndoSnackbar()
      }
      .setNegativeButton("Hủy", null)
      .show()
  }

  private fun showUndoSnackbar() {
    Snackbar.make(
      findViewById(R.id.main),
      "Đã xóa sinh viên",
      Snackbar.LENGTH_LONG
    ).setAction("Hoàn tác") {
      lastDeletedStudent?.let { student ->
        studentAdapter.insertStudent(student, lastDeletedPosition)
        lastDeletedStudent = null
        lastDeletedPosition = -1
      }
    }.show()
  }
}