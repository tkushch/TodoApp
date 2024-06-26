package com.example.todoapp.presentation.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.todoapp.R
import com.example.todoapp.TodoApp
import com.example.todoapp.data.repository.TodoItemsRepository
import com.example.todoapp.data.model.stringToImportance
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class AddTaskFragment() : Fragment() {
    private var deadline: LocalDateTime? = null
    private lateinit var dueDateText: TextView
    private lateinit var todoItemsRepository: TodoItemsRepository
    private var id: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        id = arguments?.getString(TASK_ID)

        todoItemsRepository = (requireActivity().application as TodoApp).todoItemsRepository

        val taskText = view.findViewById<EditText>(R.id.taskText)
        val importanceSpinner = view.findViewById<Spinner>(R.id.importanceSpinner)
        val addButton = view.findViewById<Button>(R.id.buttonSave)
        val cancelButton = view.findViewById<ImageButton>(R.id.buttonCancel)
        val deleteButton = view.findViewById<LinearLayout>(R.id.buttonDelete)
        val dueDateSwitch =
            view.findViewById<com.google.android.material.switchmaterial.SwitchMaterial>(R.id.dueDateSwitch)
        dueDateText = view.findViewById(R.id.dueDateText)
        dueDateSwitch.isChecked = false
        dueDateText.isEnabled = false

        addButton.setOnClickListener {
            val text = taskText.text.toString().trim()
            val importance = stringToImportance(importanceSpinner.selectedItem.toString())

            if (TextUtils.isEmpty(text)) {
                taskText.error = getString(R.string.enter_the_task)
            } else {

                id?.let {
                    todoItemsRepository.updateTodoItem(it, text, importance, deadline)
                } ?: run {
                    todoItemsRepository.addTodoItem(text, importance, deadline)
                }

                parentFragmentManager.popBackStack()
            }
        }

        cancelButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        deleteButton.setOnClickListener {
            id?.let {
                todoItemsRepository.removeTodoItemById(it)
            }

            parentFragmentManager.popBackStack()
        }

        dueDateSwitch.setOnCheckedChangeListener { _, isChecked ->
            dueDateText.isEnabled = isChecked
            if (!isChecked) {
                dueDateText.text = ""
                deadline = null
            } else {
                dueDateText.text = getString(R.string.choose_the_date)

            }
        }

        dueDateText.setOnClickListener {
            showDatePicker()
        }

        if (id != null && todoItemsRepository.findTodoItemById(id as String) != null) {
            val todoItem = todoItemsRepository.findTodoItemById(id as String)
            taskText.setText(todoItem?.text)
            importanceSpinner.setSelection(todoItem?.importance?.ordinal ?: 0)
            deadline = todoItem?.deadline
            dueDateSwitch.isChecked = todoItem?.deadline != null
            dueDateText.text = todoItem?.deadline?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        }

    }


    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate =
                    LocalDateTime.of(selectedYear, selectedMonth + 1, selectedDay, 0, 0)
                deadline = selectedDate

                dueDateText.text =
                    selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")).toString()
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    companion object {
        const val TASK_ID = "taskId"
        fun newInstance(taskId: String?): AddTaskFragment {
            return AddTaskFragment().apply {
                arguments = Bundle().apply {
                    putString(TASK_ID, taskId)
                }
            }
        }
    }
}


