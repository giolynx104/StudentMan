package vn.edu.hust.studentman

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private val students = mutableListOf<StudentModel>()
    private lateinit var adapter: ArrayAdapter<StudentModel>

    private val addStudentLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val student = result.data?.getParcelableExtra<StudentModel>("result_student")
            student?.let {
                students.add(it)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private val editStudentLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val position = result.data?.getIntExtra("position", -1) ?: -1
            val student = result.data?.getParcelableExtra<StudentModel>("result_student")
            if (position != -1 && student != null) {
                students[position] = student
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Add some test data
        students.addAll(listOf(
            StudentModel("John Doe", "20200001"),
            StudentModel("Jane Smith", "20200002"),
            StudentModel("Bob Johnson", "20200003")
        ))

        listView = findViewById(R.id.list_view_students)
        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            students
        )
        listView.adapter = adapter

        registerForContextMenu(listView)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add_new -> {
                val intent = Intent(this, StudentFormActivity::class.java)
                addStudentLauncher.launch(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        return when (item.itemId) {
            R.id.menu_edit -> {
                val intent = Intent(this, StudentFormActivity::class.java)
                intent.putExtra("student", students[info.position])
                intent.putExtra("position", info.position)
                editStudentLauncher.launch(intent)
                true
            }
            R.id.menu_remove -> {
                students.removeAt(info.position)
                adapter.notifyDataSetChanged()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
}