package vn.edu.hust.studentman

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

class StudentListFragment : Fragment() {
    private lateinit var listView: ListView
    private val students = mutableListOf<StudentModel>()
    private lateinit var adapter: ArrayAdapter<StudentModel>
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_student_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        // Add fragment result listener
        setFragmentResultListener("student_request") { _, bundle ->
            val student = bundle.getParcelable<StudentModel>("student")
            val position = bundle.getInt("position", -1)
            
            if (student != null) {
                if (position >= 0) {
                    // Update existing student
                    students[position] = student
                } else {
                    // Add new student
                    students.add(student)
                }
                adapter.notifyDataSetChanged()
            }
        }

        // Add test data
        if (students.isEmpty()) {
            students.addAll(listOf(
                StudentModel("John Doe", "20200001"),
                StudentModel("Jane Smith", "20200002"),
                StudentModel("Bob Johnson", "20200003")
            ))
        }

        listView = view.findViewById(R.id.list_view_students)
        adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            students
        )
        listView.adapter = adapter

        registerForContextMenu(listView)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add_new -> {
                navController.navigate(R.id.action_list_to_form)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        requireActivity().menuInflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        return when (item.itemId) {
            R.id.menu_edit -> {
                val action = StudentListFragmentDirections.actionListToForm(
                    student = students[info.position],
                    position = info.position
                )
                navController.navigate(action)
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