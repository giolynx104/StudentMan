package vn.edu.hust.studentman

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(private val students: MutableList<StudentModel>): RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {
  class StudentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val textStudentName: TextView = itemView.findViewById(R.id.text_student_name)
    val textStudentId: TextView = itemView.findViewById(R.id.text_student_id)
    val imageEdit: ImageView = itemView.findViewById(R.id.image_edit)
    val imageRemove: ImageView = itemView.findViewById(R.id.image_remove)
  }

  var onEditClick: ((StudentModel, Int) -> Unit)? = null
  var onDeleteClick: ((StudentModel, Int) -> Unit)? = null

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
    val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_student_item,
       parent, false)
    return StudentViewHolder(itemView)
  }

  override fun getItemCount(): Int = students.size

  override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
    val student = students[position]

    holder.textStudentName.text = student.studentName
    holder.textStudentId.text = student.studentId
    
    holder.imageEdit.setOnClickListener {
      onEditClick?.invoke(student, position)
    }
    
    holder.imageRemove.setOnClickListener {
      onDeleteClick?.invoke(student, position)
    }
  }

  fun addStudent(student: StudentModel) {
    students.add(student)
    notifyItemInserted(students.size - 1)
  }

  fun updateStudent(student: StudentModel, position: Int) {
    students[position] = student
    notifyItemChanged(position)
  }

  fun removeStudent(position: Int): StudentModel {
    val removed = students.removeAt(position)
    notifyItemRemoved(position)
    return removed
  }

  fun insertStudent(student: StudentModel, position: Int) {
    students.add(position, student)
    notifyItemInserted(position)
  }
}